package org.springframework.template.webmvc;

import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.template.TemplateResolver;
import org.springframework.template.webmvc.hypertext.HyperTextRequestMappingHandlerMapping;
import org.springframework.template.webmvc.hypertext.HyperTextWebMvcConfiguration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfiguration
@ConditionalOnWebApplication
@AutoConfigureBefore({ WebMvcAutoConfiguration.class })
public class TemplateMvcAutoConfiguration implements WebMvcConfigurer, WebMvcRegistrations {

	@Bean
	public MultiViewResolver multiViewResolver(TemplateResolver engine) {
		return new MultiViewResolver(engine);
	}

	@Bean
	public HyperTextWebMvcConfiguration hyperTextConfigurer(
			@Qualifier("viewResolver") ObjectFactory<ViewResolver> resolver,
			ObjectFactory<LocaleResolver> locales, ObjectMapper objectMapper) {
		return new HyperTextWebMvcConfiguration(resolver, locales, objectMapper);
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		handlers.add(new TemplateReturnValueHandler());
	}

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new HyperTextRequestMappingHandlerMapping();
	}

}
