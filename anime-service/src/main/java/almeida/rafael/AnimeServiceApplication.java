package almeida.rafael;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import almeida.rafael.animeservice.config.ConnectionConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
public class AnimeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimeServiceApplication.class, args);
	}

}
