package school21.spring.service.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import school21.spring.service.models.User;

public class UserMapper implements RowMapper<User> {

  @Override
  public User mapRow(ResultSet resultSet, int i) throws SQLException {
    final int id = resultSet.getInt("id");
    final String email = resultSet.getString("email");
    final String password = resultSet.getString("password");
    User user = new User(email, password);
    user.setIdentifier(Long.parseLong(String.valueOf(id)));
    return user;
  }
}
