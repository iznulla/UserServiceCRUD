package school21.spring.service.services;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;

@Component
@Setter
public class UserServiceImpl implements UserService {

  @Autowired
  @Qualifier("usersRepositoryJdbcTemplateImpl")
  private UsersRepository usersRepository;

  @Autowired
  @Qualifier("passwordGenerator")
  private String password;



  @Override
  public String signUp(String email) {
      User newUser = new User(email, password);
      usersRepository.save(newUser);
    return password;
  }
}
