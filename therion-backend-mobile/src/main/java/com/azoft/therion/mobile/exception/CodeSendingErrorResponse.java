package com.azoft.therion.mobile.exception;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeSendingErrorResponse extends ErrorResponse {
  private OffsetDateTime codeSendingAvailableAt;

  public static CodeSendingErrorResponse fromCodeSendingException(CodeSendingException exception) {
    final ErrorCode errorCode = exception.getErrorCode();

    final CodeSendingErrorResponse result = new CodeSendingErrorResponse();
    result.setMessage(exception.getMessage());
    result.setErrorCode(errorCode);
    result.setCodeSendingAvailableAt(exception.getCodeSendingAvailableAt());

    if (exception.getHttpStatus() != null) {
      result.setStatus(exception.getHttpStatus().value());
    } else {
      result.setStatus(errorCode.getHttpStatus().value());
    }

    return result;
  }
}
