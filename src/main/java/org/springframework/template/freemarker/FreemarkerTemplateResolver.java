package org.springframework.template.freemarker;

import java.io.IOException;
import java.util.Locale;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.thymeleaf.ITemplateEngine;

import freemarker.core.ParseException;
import freemarker.template.Configuration;

public class FreemarkerTemplateResolver implements TemplateResolver {

	private final Configuration configuration;

	private String prefix = "";
	private String suffix = ".ftlh";

	public FreemarkerTemplateResolver(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		try {
			return new FreemarkerTemplate(configuration.getTemplate(prefix + path + suffix));
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}catch (IOException e) {
			return null;
		}
	}


}
