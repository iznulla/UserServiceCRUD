package school21.spring.service.repositories;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import school21.spring.service.models.User;

@Component
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository{

  private final NamedParameterJdbcTemplate jdbc;
  @Autowired
  public UsersRepositoryJdbcTemplateImpl(@Qualifier("driverManagerDataSource") DataSource ds) {
    jdbc = new NamedParameterJdbcTemplate(ds);
  }


  @Override
  public User findById(Long id) {
    String sql = "select * from users where id=:id";
    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
    try {
      return jdbc.queryForObject(sql, namedParameters, new UserMapper());
    } catch (EmptyResultDataAccessException e) {
      e.fillInStackTrace();
    }
    return null;
  }

  @Override
  public List<User> findAll() {
    return jdbc.query("select * from users;", new UserMapper());
  }

  @Override
  public void save(User entity) {
    SqlParameterSource namedParameters = new MapSqlParameterSource()
        .addValue("email", entity.getEmail()).addValue(
            "password", entity.getPassword());
    String sql = "INSERT INTO users (email, password) VALUES (:email, :password);";
    if (jdbc.update(sql, namedParameters) == 0) {
      throw new RuntimeException("Object is not added");
    }
  }

  @Override
  public void update(User entity) {
    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("email", entity.getEmail())
        .addValue("id", entity.getIdentifier());
    String sql = "UPDATE users SET email=:email WHERE id=:id;";
    if (jdbc.update(sql, namedParameters) == 0) {
      throw new RuntimeException("Object is not added");
    }
  }

  @Override
  public void delete(Long id) {
    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
    String sql = "delete from users where id=:id";
    if (jdbc.update(sql, namedParameters) == 0) {
      throw new RuntimeException("Object is not deleted");
    }
  }

  @Override
  public Optional<User> findByEmail(String email) {
    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("email", email);
    String sql = "select * from users where email=:email";
    try {
      User user = jdbc.queryForObject(sql, namedParameters, new UserMapper());
      assert user != null;
      return Optional.of(user);
    } catch (EmptyResultDataAccessException e) {
      e.fillInStackTrace();
    }
    return Optional.empty();
  }
}
