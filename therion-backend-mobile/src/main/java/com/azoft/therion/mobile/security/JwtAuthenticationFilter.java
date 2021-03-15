package com.azoft.therion.mobile.security;

import com.azoft.therion.mobile.entity.MobileToken;
import com.azoft.therion.mobile.exception.AuthenticationErrorCodeException;
import com.azoft.therion.mobile.service.MobileTokenService;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Value("${application.jwt.token-ttl-in-minutes}")
  private long tokenTtlInMinutes;

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private MobileTokenService mobileTokenService;
  @Autowired
  private JwtAuthenticationEntryPoint authenticationEntryPoint;

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);

      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

        Long userId = tokenProvider.getUserIdFromJWT(jwt);
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        updateTokenTtl(jwt, userId);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (AuthenticationException ex) {

      if (!(ex instanceof AuthenticationErrorCodeException)) {
        logger.error("Could not set user authentication in security context", ex);
      }
      SecurityContextHolder.clearContext();
      authenticationEntryPoint.commence(request, response, ex);
      return;

    }

    filterChain.doFilter(request, response);
  }

  /**
   * Продлевает срок жизни токена. Если такого токена нет, или он истёк - будет исключение
   * внутри {@link MobileTokenService#findActiveToken(Long, String)}
   * @param jwt
   * @param userId
   */
  private void updateTokenTtl(String jwt, Long userId) {
    MobileToken tokenEntity = mobileTokenService.findActiveToken(userId, jwt);
    tokenEntity.setExpireAt(OffsetDateTime.now().plusMinutes(tokenTtlInMinutes));
    mobileTokenService.save(tokenEntity);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }
}
