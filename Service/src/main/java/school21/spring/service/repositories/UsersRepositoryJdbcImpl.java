package school21.spring.service.repositories;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school21.spring.service.JdbcManager.JdbcUtils;
import school21.spring.service.annotations.OrmColumnId;
import school21.spring.service.annotations.OrmEntity;
import school21.spring.service.models.User;


@Component
public class UsersRepositoryJdbcImpl implements UsersRepository {


  private final DataSource dataSource;

  @Autowired
  public UsersRepositoryJdbcImpl(@Qualifier("hikariDataSource") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public User findById(Long id) {
    User user = new User();
    OrmEntity ormEntity = user.getClass().getAnnotation(OrmEntity.class);
    String query = String.format("SELECT * FROM %s WHERE id=?;", ormEntity.table());
    try (PreparedStatement ps = dataSource.getConnection().prepareStatement(query)) {
      ps.setLong(1, id);
      ResultSet resultSet = ps.executeQuery();
      Field[] fields = user.getClass().getDeclaredFields();
      if (resultSet.next()) {
        long uId = resultSet.getLong("id");
        for (int i = 1; i < fields.length; i++) {
          fields[i].setAccessible(true);
          fields[i].set(user, resultSet.getObject(i + 1).toString());
        }
        user.setIdentifier(uId);
        return user;
      } else {
        throw new NullPointerException("User Not Found");
      }
    } catch (SQLException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<User> findAll() {
    User user = new User();
    OrmEntity ormEntity = user.getClass().getAnnotation(OrmEntity.class);
    List<User> list = new ArrayList<>();
    String query = String.format("SELECT * FROM %s;", ormEntity.table());
    try (PreparedStatement ps = dataSource.getConnection().prepareStatement(query)) {
      ResultSet resultSet = ps.executeQuery();
      Field[] fields = user.getClass().getDeclaredFields();
      while (resultSet.next()) {
        User nUser = new User();
        for (int i = 1; i < fields.length; i++) {
          fields[0].setAccessible(true);
          fields[0].set(nUser, resultSet.getLong(1));
          fields[i].setAccessible(true);
          fields[i].set(nUser, resultSet.getObject(i + 1));
          list.add(nUser);
        }
      }
    } catch (SQLException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return list;
  }

  @Override
  public void save(User entity) {
    String query = getQueryDetails(entity, "save");
    try (PreparedStatement ps = dataSource.getConnection()
        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
      Field[] fields = entity.getClass().getDeclaredFields();
      for (int i = 1; i < fields.length; i++) {
        fields[i].setAccessible(true);
        ps.setObject(i, fields[i].get(entity));
      }
      if (ps.executeUpdate() == 0) {
        throw new RuntimeException("Object is not added");
      }
      ResultSet resultSet = ps.getGeneratedKeys();
      resultSet.next();
      entity.setIdentifier(resultSet.getLong("id"));
    } catch (SQLException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(User entity) {
    String query = getQueryDetails(entity, "update");
    try (PreparedStatement ps = dataSource.getConnection().prepareStatement(query)) {
      Field[] fields = entity.getClass().getDeclaredFields();
      fields[0].setAccessible(true);
      ps.setObject(fields.length, fields[0].get(entity));
      for (int i = 1; i < fields.length; i++) {
        fields[i].setAccessible(true);
        ps.setObject(i, fields[i].get(entity));
      }
      if (ps.executeUpdate() == 0) {
        throw new RuntimeException("Object is not added");
      }
    } catch (SQLException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    System.out.println(query);
  }

  @Override
  public void delete(Long id) {
    String query = "DELETE FROM users WHERE id=?;";
    try (PreparedStatement ps = dataSource.getConnection().prepareStatement(query)) {
      ps.setLong(1, id);
      if (ps.executeUpdate() == 0) {
        throw new RuntimeException("Object is not deleted");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Optional<User> findByEmail(String email) {
    User user = new User();
    OrmEntity ormEntity = user.getClass().getAnnotation(OrmEntity.class);
    String query = String.format("SELECT * FROM %s WHERE email=?;", ormEntity.table());
    try (PreparedStatement ps = dataSource.getConnection().prepareStatement(query)) {
      ps.setString(1, email);
      Field[] fields = user.getClass().getDeclaredFields();
      ResultSet resultSet = ps.executeQuery();
      if (resultSet.next()) {
        long uId = resultSet.getLong("id");
        for (int i = 1; i < fields.length; i++) {
          fields[i].setAccessible(true);
          fields[i].set(user, resultSet.getObject(i + 1).toString());
        }
        user.setIdentifier(uId);
        return Optional.of(user);
      }
    } catch (SQLException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }


  public void createSchema() {
    try {
      Path schemaUrl = Paths.get("target/classes/schema.sql").normalize();
      if (!Files.exists(schemaUrl)) {
        throw new RuntimeException("File schema.sql not found. Compile the project using "
            + "'mvn clean compile' to generate the file.");
      }
      String schema = Files.lines(schemaUrl).collect(Collectors.joining("\n"));
      PreparedStatement statement = JdbcUtils.preStatement(schema);
      statement.execute();
      statement.close();
      System.out.println(schema);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }

  private String getQueryDetails(Object obj, String method) {
    StringBuilder query = new StringBuilder();
    StringBuilder values = new StringBuilder();
    Field[] fields = obj.getClass().getDeclaredFields();
    OrmEntity ormEntity = obj.getClass().getAnnotation(OrmEntity.class);
    if (method.equals("save")) {
      query.append(String.format("INSERT INTO %s "
          + "(", ormEntity.table()));
      for (int i = 1; i < fields.length; ++i) {
        query.append(String.format("%s", fields[i].getName()));
        values.append("?");
        if (i != fields.length - 1) {
          query.append(",");
          values.append(",");
        }
      }
      query.append(") VALUES (").append(values).append(");");
    } else if (method.equals("update")) {
      query.append(String.format("UPDATE %s SET  ", ormEntity.table()));
      if (obj.getClass().getAnnotation(OrmColumnId.class) != null) {
        OrmColumnId ormColumnId = obj.getClass().getAnnotation(OrmColumnId.class);
        query.append(ormColumnId.id());
      }
      for (int i = 1; i < fields.length; ++i) {
        query.append(String.format("%s=?", fields[i].getName()));
        if (i != fields.length - 1) {
          query.append(",");
        }
      }
      query.append(" WHERE id=?;");
    }
    return query.toString();
  }
}
