package org.springframework.experimental;

import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.template.TemplateResolver;
import org.springframework.template.path.PathGenerator;
import org.springframework.template.thymeleaf.ThymeleafTemplateResolver;
import org.springframework.template.thymeleaf.WebContextFactory;
import org.springframework.template.webmvc.MultiViewResolver;
import org.springframework.template.webmvc.TemplateReturnValueHandler;
import org.springframework.template.webmvc.hypertext.HyperTextRequestMappingHandlerMapping;
import org.springframework.template.webmvc.hypertext.HyperTextWebMvcConfiguration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.thymeleaf.TemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	@Bean
	public TemplateResolver templateResolver(TemplateEngine engine) {
		ThymeleafTemplateResolver resolver = new ThymeleafTemplateResolver(engine, new WebContextFactory());
		resolver.setPaths(PathGenerator.identity());
		return resolver;
	}

	@Bean
	public MultiViewResolver multiViewResolver(TemplateResolver engine) {
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

	@Bean
	public HyperTextWebMvcConfiguration hyperTextConfigurer(@Qualifier("viewResolver") ObjectFactory<ViewResolver> resolver,
			ObjectFactory<LocaleResolver> locales, ObjectMapper objectMapper) {
		return new HyperTextWebMvcConfiguration(resolver, locales, objectMapper);
	}

	@Bean
	public WebMvcRegistrations webMvcRegistrations() {
		return new WebMvcRegistrations() {
			public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
				return new HyperTextRequestMappingHandlerMapping();
			}
		};
	}

}
