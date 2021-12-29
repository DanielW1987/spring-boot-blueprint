package rocks.danielw.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

  private String secret;
  private long expirationInMs;

}
