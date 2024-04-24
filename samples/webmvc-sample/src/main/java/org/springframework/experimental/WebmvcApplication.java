package org.springframework.experimental;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.template.TemplateResolver;
import org.springframework.template.path.PathGenerator;
import org.springframework.template.thymeleaf.ThymeleafTemplateResolver;
import org.springframework.template.thymeleaf.WebContextFactory;
import org.springframework.template.webmvc.MultiViewResolver;
import org.springframework.template.webmvc.TemplateReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;

@SpringBootApplication
public class WebmvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebmvcApplication.class, args);
	}

	@Bean
	public TemplateResolver templateResolver(TemplateEngine engine) {
		ThymeleafTemplateResolver resolver = new ThymeleafTemplateResolver(engine, new WebContextFactory());
		resolver.setPaths(PathGenerator.identity());
		return resolver;
	}

	@Bean
	public MultiViewResolver multiViewResolverultiViewResolver(TemplateResolver engine) {
		return new MultiViewResolver(engine);
	}

	@Bean
	public WebMvcConfigurer webConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
				handlers.add(new TemplateReturnValueHandler());
			}
		};

	}

}
