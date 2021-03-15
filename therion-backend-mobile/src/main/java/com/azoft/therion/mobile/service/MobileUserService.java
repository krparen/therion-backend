package com.azoft.therion.mobile.service;

import com.azoft.therion.mobile.entity.MobileUser;
import com.azoft.therion.mobile.exception.CodeSendingException;
import com.azoft.therion.mobile.exception.ErrorCode;
import com.azoft.therion.mobile.repository.MobileUserRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MobileUserService {

  @Value("${application.login-block-duration-in-minutes}")
  private long badLoginBlockInMinutes;

  @Value("${application.max-bad-logins}")
  private int maxBadLogins;

  @Value("${application.mobile-code-length}")
  private int mobileCodeLength;

  @Value("${application.mobile-code-period-in-seconds}")
  private int mobileCodePeriodInSeconds;

  @Autowired
  private MobileUserRepository mobileUserRepo;
  @Autowired
  private SmsService smsService;
  @Autowired
  PasswordEncoder passwordEncoder;

  @Transactional
  public MobileUser getOne(Long id) {
    return mobileUserRepo.getOne(id);
  }

  @Transactional
  public List<MobileUser> findByIdIn(List<Long> userIds) {
    return mobileUserRepo.findByIdIn(userIds);
  }

  @Transactional
  public Optional<MobileUser> findByPhone(String phone) {
    return mobileUserRepo.findByPhone(phone);
  }

  @Transactional
  public Boolean existsByPhone(String phone) {
    return mobileUserRepo.existsByPhone(phone);
  }

  @Transactional
  public MobileUser save(MobileUser mobileUser) {
    return mobileUserRepo.save(mobileUser);
  }

  @Transactional
  public void sendAuthorizationCode(String phone) {

    findByPhone(phone).ifPresent(user -> {

      checkPasswordSendingCooldown(user);

      //String rawPassword = RandomStringUtils.randomNumeric(mobileCodeLength);
      String rawPassword = "1111";
      smsService.sendSms(phone, "code: " + rawPassword);
      user.setPassword(passwordEncoder.encode(rawPassword));
      user.setPasswordSentAt(OffsetDateTime.now());
      save(user);
    });
  }

  /**
   * Удаляет данные о неуспешных логинах при успешном логине
   */
  @Transactional
  public void deleteFailedLoginData(Long userId) {
    MobileUser user = getOne(userId);

    user.setPassword(null);

    if (user.getBadLoginAmount() != null || user.getFirstBadLoginDate() != null) {
      user.setBadLoginAmount(null);
      user.setFirstBadLoginDate(null);
    }

    save(user);
  }

  @Transactional
  public void handleFailedLogin(Long userId) {
    MobileUser user = getOne(userId);

    OffsetDateTime firstBadLoginDate = user.getFirstBadLoginDate();

    if (user.isLoginBlocked()) {
      return;
    }


    if (isFirstBadLoginNullOrOld(firstBadLoginDate)) {
      log.info("login failed for user with id = {} , remembering first bad login", userId);
      user.setBadLoginAmount(1);
      user.setFirstBadLoginDate(OffsetDateTime.now());
      save(user);
    } else {

      user.increaseBadLoginAmount();
      log.info("login failed for user with id = {} , remembering {} bad login", userId, user.getBadLoginAmount());
      if (user.getBadLoginAmount() >= maxBadLogins) {
        log.info("setting login block for user with id = {} because of too much failed logins", userId);
        user.setLoginBlockedUntil(OffsetDateTime.now().plusMinutes(badLoginBlockInMinutes));
        save(user);
      }
    }
  }

  private boolean isFirstBadLoginNullOrOld(OffsetDateTime firstBadLoginDate) {

    if (firstBadLoginDate == null) {
      return true;
    }

    OffsetDateTime now = OffsetDateTime.now();

    return now.isAfter(firstBadLoginDate.plusMinutes(badLoginBlockInMinutes));
  }

  private void checkPasswordSendingCooldown(MobileUser mobileUser) {

    if (mobileUser.getPasswordSentAt() == null) {
      return;
    }

    OffsetDateTime codeSendingAvailableAt = mobileUser.getPasswordSentAt().plusSeconds(mobileCodePeriodInSeconds);

    if (OffsetDateTime.now().isBefore(codeSendingAvailableAt)) {
      log.error("user with id = {} requesting auth code before it's available. code availability date: {}",
          mobileUser.getId(), codeSendingAvailableAt);
      String message = "code sending is on cooldown";
      throw new CodeSendingException(message, ErrorCode.CODE_SENDING_COOLDOWN, codeSendingAvailableAt);
    }

  }
}
