package com.azoft.therion.mobile.service;

import com.azoft.therion.mobile.entity.MobileToken;
import com.azoft.therion.mobile.exception.AuthenticationErrorCodeException;
import com.azoft.therion.mobile.exception.ErrorCode;
import com.azoft.therion.mobile.repository.MobileTokenRepository;
import java.time.OffsetDateTime;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MobileTokenService {

  @Autowired
  private MobileTokenRepository tokenRepository;

  @Transactional
  public MobileToken findActiveToken(Long userId, String token) {
    MobileToken tokenEntity = tokenRepository.findFirstByMobileUserIdAndTokenAndExpireAtIsAfter(userId, token, OffsetDateTime.now());

    if (tokenEntity == null) {
      String message = "token not found or expired";
      log.error(message + ", userId = {}, token = {}", userId, token);
      throw new AuthenticationErrorCodeException(message, ErrorCode.TOKEN_EXPIRED);
    }

    return tokenEntity;
  }

  public MobileToken save(MobileToken token) {
    return tokenRepository.save(token);
  }
}
