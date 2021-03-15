package com.azoft.therion.mobile.exception;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginBlockedErrorResponse extends ErrorResponse {
  private OffsetDateTime blockedUntil;

  public static LoginBlockedErrorResponse fromLoginBlockedException(LoginBlockedException exception) {
    final ErrorCode errorCode = exception.getErrorCode();

    final LoginBlockedErrorResponse result = new LoginBlockedErrorResponse();
    result.setMessage(exception.getMessage());
    result.setErrorCode(errorCode);
    result.setBlockedUntil(exception.getBlockedUntil());

    if (exception.getHttpStatus() != null) {
      result.setStatus(exception.getHttpStatus().value());
    } else {
      result.setStatus(errorCode.getHttpStatus().value());
    }

    return result;
  }
}
