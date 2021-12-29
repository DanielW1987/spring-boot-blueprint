package rocks.danielw.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rocks.danielw.persistence.user.UserEntity;

import java.util.Date;

@Component
@PropertySource(value = "classpath:application.yml")
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

  private final JwtProperties jwtProperties;

  public String generateJwtToken(UserDetails userDetails) {
    return Jwts.builder()
        .setSubject((userDetails.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtProperties.getExpirationInMs()))
        .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser()
        .setSigningKey(jwtProperties.getSecret())
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser()
          .setSigningKey(jwtProperties.getSecret())
          .parseClaimsJws(authToken);
      return true;
    }
    catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } 
    catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } 
    catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } 
    catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
