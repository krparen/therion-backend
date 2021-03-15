package com.azoft.therion.mobile.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  /**
   * С помощью этого инжекта перенаправляем ошибки при логине / чтении из токена
   */
  @Qualifier("handlerExceptionResolver")
  @Autowired
  private HandlerExceptionResolver resolver;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException exception) throws IOException, ServletException {
    resolver.resolveException(request, response, null, exception);
  }
}
