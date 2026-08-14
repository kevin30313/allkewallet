package com.kevin30313.alkewallet;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AlkewalletApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AlkewalletApplication.class);

		// Load .env variables as default properties
		// This ensures OS Environment Variables (Cloud Run/Secret Manager) take priority
		// while .env acts as a local fallback.
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		Map<String, Object> dotenvProperties = new HashMap<>();
		dotenv.entries().forEach(entry -> dotenvProperties.put(entry.getKey(), entry.getValue()));
		app.setDefaultProperties(dotenvProperties);

		app.run(args);
	}

}
