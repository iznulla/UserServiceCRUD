package school21.spring.service.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("school21")
@PropertySource("classpath:db.properties")
@Import(PasswordGenerateConfig.class)
public class ApplicationConfig {

  @Value("${db.url}")
  private String url;


  @Bean
  public HikariConfig hikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(url);
    return hikariConfig;
  }

  @Bean
  public HikariDataSource hikariDataSource() {
    return new HikariDataSource(hikariConfig());
  }

  @Bean
  public DriverManagerDataSource driverManagerDataSource() {
    DriverManagerDataSource dms = new DriverManagerDataSource();
    dms.setUrl(url);
    return dms;
  }

}




