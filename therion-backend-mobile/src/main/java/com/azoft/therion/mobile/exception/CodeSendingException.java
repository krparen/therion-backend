package com.azoft.therion.mobile.exception;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeSendingException extends ErrorCodeException {

  public CodeSendingException(String s, ErrorCode errorCode, OffsetDateTime codeSendingAvailableAt) {
    super(s, errorCode);
    this.codeSendingAvailableAt = codeSendingAvailableAt;
  }

  public CodeSendingException(String s, Throwable throwable, ErrorCode errorCode, OffsetDateTime codeSendingAvailableAt) {
    super(s, throwable, errorCode);
    this.codeSendingAvailableAt = codeSendingAvailableAt;
  }

  private OffsetDateTime codeSendingAvailableAt;
}
