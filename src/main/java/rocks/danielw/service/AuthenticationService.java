package rocks.danielw.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rocks.danielw.persistence.user.UserEntity;
import rocks.danielw.persistence.user.UserRepository;
import rocks.danielw.persistence.user.UserRole;
import rocks.danielw.security.JwtUtils;
import rocks.danielw.web.api.SignInRequest;
import rocks.danielw.web.api.SignInResponse;
import rocks.danielw.web.api.SignUpRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;

  @Transactional
  public SignInResponse signIn(SignInRequest request) {
    UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
    checkCredentials(request.getEmail(), request.getPassword());
    String token = jwtUtils.generateJwtToken(userDetails);
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return SignInResponse.builder()
        .email(userDetails.getUsername())
        .roles(roles)
        .token(token)
        .build();
  }

  @Transactional
  public void registerUser(SignUpRequest request) {
    checkIfUserAlreadyExists(request.getEmail());

    UserEntity user = new UserEntity(
        request.getEmail(),
        passwordEncoder.encode(request.getPassword()),
        Set.of(UserRole.ROLE_USER),
        true
    );

    userRepository.save(user);
  }

  private void checkIfUserAlreadyExists(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistsException(String.format("User with email %s already exists.", email));
    }
  }
  
  private void checkCredentials(String email, String password) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
