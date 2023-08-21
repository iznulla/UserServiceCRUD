package school21.spring.service.JdbcManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikDataSource {
  private static final HikariConfig config = new HikariConfig();
  private static final HikariDataSource ds;

  static {
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/ormbase");
    config.setUsername("merylpor");
    config.setPassword("1");
    config.setMaximumPoolSize(4);
    ds = new HikariDataSource(config);
  }

  public static HikariDataSource getDs() { return ds;}

}
