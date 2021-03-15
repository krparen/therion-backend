package com.azoft.therion.mobile.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorCodeException extends RuntimeException {

  private ErrorCode errorCode;
  private HttpStatus httpStatus;

  public ErrorCodeException(String s, ErrorCode errorCode) {
    super(s);
    this.errorCode = errorCode;
  }

  public ErrorCodeException(String s, Throwable throwable, ErrorCode errorCode) {
    super(s, throwable);
    this.errorCode = errorCode;
  }
}
