package school21.spring.service.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import school21.spring.service.repositories.UsersRepository;
import school21.spring.service.repositories.UsersRepositoryJdbcImpl;
import school21.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;
import school21.spring.service.services.UserService;
import school21.spring.service.services.UserServiceImpl;

@Configuration
@ComponentScan("school21.spring.service")
public class TestApplicationConfig {


  @Bean
  public DataSource dsConnect() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
        .addScripts("schema.sql").build();
  }

  @Bean
  public UsersRepository usersRepositoryJdbcImpl() {
    return new UsersRepositoryJdbcImpl(dsConnect());
  }

  @Bean
  public UsersRepository usersRepositoryJdbcTemplateImpl() {
    return new UsersRepositoryJdbcTemplateImpl(dsConnect());
  }

  @Bean
  public UserService userServiceTemplate() {
    UserServiceImpl userServiceImpl = new UserServiceImpl();
    userServiceImpl.setUsersRepository(usersRepositoryJdbcTemplateImpl());
    return userServiceImpl;
  }

  @Bean
  public UserService userService() {
    UserServiceImpl userServiceImpl = new UserServiceImpl();
    userServiceImpl.setUsersRepository(usersRepositoryJdbcImpl());
    return userServiceImpl;
  }
}
