package rocks.danielw.persistence.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "users")
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "email", unique = true, nullable = false, length = 50)
  private String email;

  @Column(name = "password", nullable = false, length = 120)
  private String encryptedPassword;

  @Column(name = "account_expired")
  private boolean accountExpired;

  @Column(name = "account_locked")
  private boolean accountLocked;

  @Column(name = "credentials_expired")
  private boolean credentialsExpired;

  @Column(name = "enabled")
  private boolean enabled;
  
  @Column(name = "user_roles", nullable = false)
  @ElementCollection(fetch = EAGER)
  @Enumerated(STRING)
  protected Set<UserRole> roles;

  @Builder
  public UserEntity(@NonNull String email, @NonNull String encryptedPassword, @NonNull Set<UserRole> roles, boolean enabled) {
    this.email = email;
    this.encryptedPassword = encryptedPassword;
    this.roles = roles;
    this.enabled = enabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return encryptedPassword;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !accountExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !accountLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !credentialsExpired;
  }
}
