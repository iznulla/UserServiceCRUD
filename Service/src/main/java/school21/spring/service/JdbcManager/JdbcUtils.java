package school21.spring.service.JdbcManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcUtils {
  static final DataSource ds = HikDataSource.getDs();
  static final Connection con;

  static {
    try {
      con = ds.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static PreparedStatement preStatement(String query) throws SQLException {
    return con.prepareStatement(query);
  }
}
