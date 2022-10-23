package rocks.danielw.config.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static rocks.danielw.config.Profiles.SAMPLE_DATA_MODE;

@Component
@Profile(SAMPLE_DATA_MODE)
@RequiredArgsConstructor
@Slf4j
public class SampleDataProvider implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

  private static final String SQL_FILE = "db/SampleData.sql";

  private final DataSource dataSource;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (!event.getApplicationContext().equals(applicationContext)) {
      return;
    }

    log.info("Populating datasource using SQL file {}!", SQL_FILE);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.setScripts(new ClassPathResource(SQL_FILE));
    DatabasePopulatorUtils.execute(populator, dataSource);
  }
}
