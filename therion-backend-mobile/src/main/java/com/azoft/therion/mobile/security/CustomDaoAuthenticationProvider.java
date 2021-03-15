package com.azoft.therion.mobile.security;

import com.azoft.therion.mobile.entity.MobileUser;
import com.azoft.therion.mobile.exception.ErrorCode;
import com.azoft.therion.mobile.exception.ErrorCodeException;
import com.azoft.therion.mobile.exception.LoginBlockedException;
import com.azoft.therion.mobile.service.MobileUserService;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

  @Value("${application.mobile-pass-ttl-in-minutes}")
  private int mobilePassTtl;

  @Autowired
  private MobileUserService mobileUserService;


  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                UsernamePasswordAuthenticationToken authentication) {

    logger.info("entering additional auth checks");
    Long userId = ((UserPrincipal) userDetails).getId();
    MobileUser mobileUser = mobileUserService.getOne(userId);

    checkIfLoginBlocked(mobileUser);

    try {
      super.additionalAuthenticationChecks(userDetails, authentication);
    } catch (BadCredentialsException e) {
      logger.info("login failed because of password mismatch, userId = " + userId);
      mobileUserService.handleFailedLogin(userId);
      throw new ErrorCodeException("Access denied", e, ErrorCode.BAD_CREDENTIALS);
    }

    checkIfPasswordExpired(mobileUser);
  }

  @Override
  protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                       UserDetails user) {

    logger.info("entering create success authentication");
    Long userId = ((UserPrincipal) user).getId();
    mobileUserService.deleteFailedLoginData(userId);
    return super.createSuccessAuthentication(principal, authentication, user);
  }

  private void checkIfPasswordExpired(MobileUser mobileUser) {
    if (isMobilePassExpired(mobileUser.getPasswordSentAt())) {
      String message = "Password expired";
      logger.error(message + "; userId = " + mobileUser.getId());
      mobileUserService.handleFailedLogin(mobileUser.getId());
      throw new ErrorCodeException(message, ErrorCode.BAD_CREDENTIALS);
    }
  }

  private void checkIfLoginBlocked(MobileUser mobileUser) {
    if (mobileUser.isLoginBlocked()) {
      String message = "user with id = " + mobileUser.getId() +" trying to login, but login is blocked until " + mobileUser.getLoginBlockedUntil();
      logger.error(message);
      throw new LoginBlockedException(message, ErrorCode.LOGIN_BLOCKED, mobileUser.getLoginBlockedUntil());
    }
  }

  private boolean isMobilePassExpired(OffsetDateTime passwordSentAt) {
    OffsetDateTime ttlBorder = OffsetDateTime.now().minusMinutes(mobilePassTtl);
    return passwordSentAt.isBefore(ttlBorder);
  }


}
