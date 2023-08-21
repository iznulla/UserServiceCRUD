package school21.spring.service.config;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("school21")
public class PasswordGenerateConfig {

  @Bean
  public CharacterData characterData() {
    return EnglishCharacterData.LowerCase;
  }

  @Bean
  public CharacterRule characterRule() {
    CharacterRule cR = new CharacterRule(characterData());
    cR.setNumberOfCharacters(2);
    return cR;
  }
  @Bean
  public String passwordGenerator() {
    PasswordGenerator generator = new PasswordGenerator();
    return generator.generatePassword(8, characterRule());
  }
}
