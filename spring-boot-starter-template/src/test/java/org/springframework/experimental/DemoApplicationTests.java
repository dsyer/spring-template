package org.springframework.experimental;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.template.engine=thymeleaf")
class DemoApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, new String[] { "--spring.template.engine=thymeleaf" });
	}

	@Test
	void contextLoads() {
	}

}

@SpringBootApplication
class DemoApplication {}