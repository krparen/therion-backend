package com.azoft.therion.mobile.exception;

import com.azoft.therion.mobile.security.JwtAuthenticationEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;

@ControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(LoginBlockedException.class)
  public ResponseEntity<LoginBlockedErrorResponse> handleLoginBlockedException(LoginBlockedException ex) {
    final LoginBlockedErrorResponse response = LoginBlockedErrorResponse.fromLoginBlockedException(ex);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @ExceptionHandler(CodeSendingException.class)
  public ResponseEntity<CodeSendingErrorResponse> handleCodeSendingException(CodeSendingException ex) {
    final CodeSendingErrorResponse response = CodeSendingErrorResponse.fromCodeSendingException(ex);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @ExceptionHandler(ErrorCodeException.class)
  public ResponseEntity<ErrorResponse> handleErrorCodeException(ErrorCodeException ex) {
    final ErrorResponse response = ErrorResponse.fromException(ex);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  /**
   * Перенаправили сюда обработку ошибок при присылании токена из Spring Security.
   * См. {@link JwtAuthenticationEntryPoint}
   * @param ex - перенаправляемое исключение.
   * @return
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
    final ErrorResponse response = new ErrorResponse();
    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setErrorCode(ErrorCode.AUTHENTICATION_FAILED);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @ExceptionHandler(AuthenticationErrorCodeException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationErrorCodeException(AuthenticationErrorCodeException ex) {
    final ErrorResponse response = new ErrorResponse();
    response.setErrorCode(ex.getErrorCode());
    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
}
