package org.springframework.experimental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.template.TemplateResolver;
import org.springframework.template.path.PathGenerator;
import org.springframework.template.thymeleaf.ThymeleafTemplateResolver;
import org.thymeleaf.TemplateEngine;

@SpringBootApplication
public class SimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleApplication.class, args);
	}

	@Bean
	public TemplateResolver templateResolver(TemplateEngine engine) {
		ThymeleafTemplateResolver resolver = new ThymeleafTemplateResolver(engine);
		resolver.setPaths(PathGenerator.identity());
		return resolver;
	}

}
