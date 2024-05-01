package org.springframework.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.RequestScope;

@SpringBootApplication
public class WebmvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebmvcApplication.class, args);
	}

	@Bean
	@RequestScope
	@ConfigurationProperties(prefix = "app")
	public Application app() {
		return new Application();
	}

}
