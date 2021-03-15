package com.azoft.therion.mobile.security;
import com.azoft.therion.mobile.entity.MobileToken;
import io.jsonwebtoken.*;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  @Value("${application.jwt.secret}")
  private String jwtSecret;

  /**
   * Это - фейковое время жизни, примерно 200 лет :)
   * Оно такое большое, чтобы токен сам по себе никогда не просрочился.
   * Актуальное время жизни токена храним в бд в {@link MobileToken}
   */
  private long jwtExpirationInMs = 6048000000000L;

  public String generateToken(Authentication authentication) {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(userPrincipal.getId()))
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .setId(UUID.randomUUID().toString()) // добавляем чтобы токены точно не дублировались
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public Long getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      logger.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty.");
    }
    return false;
  }
}
