package school21.spring.service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school21.spring.service.config.TestApplicationConfig;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;


public class UserServiceImplTest {

  private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestApplicationConfig.class);
  private final UserService userService =
      context.getBean("userServiceTemplate", UserService.class);
  UsersRepository usersRepository = context.getBean("usersRepositoryJdbcTemplateImpl", UsersRepository.class);


  @ParameterizedTest
  @ValueSource(strings = {"iamfarrukh2000@gmail.com", "lichero@list.ru", "barbarastrazed@uu.uu"})
  void signUpTest(String email) {
    String password = userService.signUp(email);
    User user = usersRepository.findByEmail(email).orElse(null);
    assert user != null;
    assertEquals(user.getPassword(), password);
  }


}
