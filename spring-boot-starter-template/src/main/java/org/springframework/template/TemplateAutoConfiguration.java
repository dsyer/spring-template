package org.springframework.template;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.template.freemarker.FreemarkerTemplateResolver;
import org.springframework.template.path.PathGenerator;
import org.springframework.template.st.STTemplateResolver;
import org.springframework.template.thymeleaf.ThymeleafTemplateResolver;
import org.springframework.template.thymeleaf.WebContextFactory;
import org.stringtemplate.v4.ST;
import org.thymeleaf.TemplateEngine;

import freemarker.core.Configurable;

@AutoConfiguration
@AutoConfigureBefore({ ThymeleafAutoConfiguration.class, FreeMarkerAutoConfiguration.class })
public class TemplateAutoConfiguration {

	@Configuration
	@ConditionalOnClass(TemplateEngine.class)
	@ConditionalOnProperty(prefix = "spring.template", name = "engine", havingValue = "thymeleaf", matchIfMissing = true)
	static class ThymeleafConfiguration {
		@Bean
		public TemplateResolver thymeleafTemplateResolver(TemplateEngine engine) {
			ThymeleafTemplateResolver resolver = new ThymeleafTemplateResolver(engine, new WebContextFactory());
			resolver.setPaths(PathGenerator.identity());
			return resolver;
		}
	}

	@Configuration
	@ConditionalOnClass(Configurable.class)
	@ConditionalOnProperty(prefix = "spring.template", name = "engine", havingValue = "freemarker", matchIfMissing = true)
	static class FreemarkerConfiguration {
		@Bean
		public TemplateResolver freemarkerTemplateResolver(freemarker.template.Configuration configuration) {
			FreemarkerTemplateResolver resolver = new FreemarkerTemplateResolver(configuration);
			return resolver;
		}
	}

	@Configuration
	@ConditionalOnClass(ST.class)
	@ConditionalOnProperty(prefix = "spring.template", name = "engine", havingValue = "st", matchIfMissing = true)
	static class STConfiguration {
		@Bean
		public TemplateResolver stTemplateResolver(ResourceLoader loader) {
			STTemplateResolver resolver = new STTemplateResolver(loader);
			return resolver;
		}
	}

}
