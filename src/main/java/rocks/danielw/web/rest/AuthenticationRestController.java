package rocks.danielw.web.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocks.danielw.service.AuthenticationService;
import rocks.danielw.web.api.LoginRequest;
import rocks.danielw.web.api.LoginResponse;
import rocks.danielw.web.api.SignupRequest;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationRestController {
  
  private final AuthenticationService authenticationService;
  
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest request) {
    LoginResponse response = authenticationService.authenticateUser(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
    authenticationService.registerUser(request);
    return ResponseEntity.ok().build();
  }
}
