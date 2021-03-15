package com.azoft.therion.mobile.exception;

import lombok.Data;

@Data
public class ErrorResponse {
  private ErrorCode errorCode;
  private String message;
  private Integer status;

  public static ErrorResponse fromException(ErrorCodeException exception) {
    final ErrorCode errorCode = exception.getErrorCode();

    final ErrorResponse result = new ErrorResponse();
    result.setMessage(exception.getMessage());
    result.setErrorCode(errorCode);

    if (exception.getHttpStatus() != null) {
      result.setStatus(exception.getHttpStatus().value());
    } else {
      result.setStatus(errorCode.getHttpStatus().value());
    }

    return result;
  }
}
