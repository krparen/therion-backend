package com.azoft.therion.mobile.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
  BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED),
  LOGIN_BLOCKED(HttpStatus.UNAUTHORIZED),
  PHONE_EXISTS(HttpStatus.BAD_REQUEST),
  CODE_SENDING_COOLDOWN(HttpStatus.BAD_REQUEST),

  /**
   * Время жизни токена, которое отслеживается в бд, истекло.
   */
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),

  /**
   * Общая ошибка при провале аутентификации
   */
  AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED);

  @Getter
  private final HttpStatus httpStatus;

  ErrorCode(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
}
