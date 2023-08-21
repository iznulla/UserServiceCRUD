package school21.spring.service.JdbcManager;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DriverManagerDs {
  private final static DriverManagerDataSource dataSource = new DriverManagerDataSource();

  static {
    dataSource.setUrl("jdbc:postgresql://localhost:5432/ormbase");
    dataSource.setUsername("merylpor");
    dataSource.setPassword("1");
  }

  public static DriverManagerDataSource getDrMnDs() { return dataSource;}
}
