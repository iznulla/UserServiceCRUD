package school21.spring.service.application;

import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school21.spring.service.JdbcManager.HikDataSource;
import school21.spring.service.config.ApplicationConfig;
import school21.spring.service.repositories.UsersRepositoryJdbcImpl;
import school21.spring.service.services.UserServiceImpl;

public class Main {

  public static void main(String[] args) {
//    DataSource dataSource = HikDataSource.getDs();
//    UsersRepositoryJdbcImpl usersRepositoryJdbc = new UsersRepositoryJdbcImpl(dataSource);
//    usersRepositoryJdbc.createSchema();
    ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    UserServiceImpl userServiceImpl = context.getBean("userServiceImpl", UserServiceImpl.class);
    userServiceImpl.signUp("iamfarrukh2000@gmail.com");
  }
}