package almeida.rafael.animeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
  private final ConnectionConfigurationProperties configurationProperties;

  @Bean
  @Primary
  public Connection connectionMySql() {
    return new Connection(configurationProperties.url(), configurationProperties.username(),
        configurationProperties.password());
  }

  @Bean
  @Profile("mongo")
  public Connection connectionMongoDB() {
    return new Connection(configurationProperties.url(), configurationProperties.username(),
        configurationProperties.password());
  }
}
