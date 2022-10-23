package rocks.danielw.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignInResponse {
  
  private String email;
  private List<String> roles;
  private String token;
  
}
