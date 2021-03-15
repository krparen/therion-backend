package com.azoft.therion.mobile.exception;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginBlockedException extends ErrorCodeException {

  public LoginBlockedException(String s, ErrorCode errorCode, OffsetDateTime blockedUntil) {
    super(s, errorCode);
    this.blockedUntil = blockedUntil;
  }

  public LoginBlockedException(String s, Throwable throwable, ErrorCode errorCode, OffsetDateTime blockedUntil) {
    super(s, throwable, errorCode);
    this.blockedUntil = blockedUntil;
  }

  private OffsetDateTime blockedUntil;
}
