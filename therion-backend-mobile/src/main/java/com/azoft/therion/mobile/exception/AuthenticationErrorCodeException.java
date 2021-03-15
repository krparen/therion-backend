package com.azoft.therion.mobile.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;


@Getter
@Setter
public class AuthenticationErrorCodeException extends AuthenticationException {

  private ErrorCode errorCode;
  private HttpStatus httpStatus;

  public AuthenticationErrorCodeException(String s, ErrorCode errorCode) {
    super(s);
    this.errorCode = errorCode;
  }

  public AuthenticationErrorCodeException(String s, Throwable throwable, ErrorCode errorCode) {
    super(s, throwable);
    this.errorCode = errorCode;
  }
}
