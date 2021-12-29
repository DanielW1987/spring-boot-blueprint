package rocks.danielw.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.danielw.persistence.user.UserEntity;
import rocks.danielw.persistence.user.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseInitializer implements ApplicationRunner {

  @PersistenceContext
  private final EntityManager entityManager;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    List<UserEntity> currentExistingUser = entityManager.createQuery("select u from users u", UserEntity.class).getResultList();
    if (currentExistingUser.isEmpty()) {
      createDefaultUser();
    }
  }

  private void createDefaultUser() {
    UserEntity user = UserEntity.builder()
        .email("user@example.com")
        .encryptedPassword("$2a$10$gkktnlsTPO3QpF9rw2y5De4Z3jbpdedMAhw1oEjVg9wGHLxGxgrwy")
        .enabled(true)
        .roles(Set.of(UserRole.ROLE_USER))
        .build();
    
    UserEntity administrator = UserEntity.builder()
        .email("admin@example.com")
        .encryptedPassword("$2a$10$gkktnlsTPO3QpF9rw2y5De4Z3jbpdedMAhw1oEjVg9wGHLxGxgrwy")
        .enabled(true)
        .roles(Set.of(UserRole.ROLE_ADMIN, UserRole.ROLE_USER))
        .build();

    entityManager.persist(user);
    entityManager.persist(administrator);
  }
}
