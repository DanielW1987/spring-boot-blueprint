package rocks.danielw.web.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocks.danielw.service.AuthenticationService;
import rocks.danielw.web.api.SignInRequest;
import rocks.danielw.web.api.SignInResponse;
import rocks.danielw.web.api.SignUpRequest;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationRestController {
  
  private final AuthenticationService authenticationService;
  
  @PostMapping("/sign-in")
  public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) {
    SignInResponse response = authenticationService.signIn(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
    authenticationService.registerUser(request);
    return ResponseEntity.ok().build();
  }
}
