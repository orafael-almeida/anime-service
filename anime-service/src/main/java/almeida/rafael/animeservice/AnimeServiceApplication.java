package almeida.rafael.animeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import almeida.rafael.animeservice.config.ConnectionConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(value = ConnectionConfigurationProperties.class)
public class AnimeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimeServiceApplication.class, args);
	}

}
