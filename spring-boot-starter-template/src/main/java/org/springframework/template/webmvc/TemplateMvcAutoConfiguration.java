package org.springframework.template.webmvc;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.template.TemplateResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@ConditionalOnWebApplication
@AutoConfigureBefore({ WebMvcAutoConfiguration.class })
public class TemplateMvcAutoConfiguration implements WebMvcConfigurer {

	@Bean
	public MultiViewResolver multiViewResolver(TemplateResolver engine) {
		return new MultiViewResolver(engine);
	}
	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		handlers.add(new TemplateReturnValueHandler());
	}

}
