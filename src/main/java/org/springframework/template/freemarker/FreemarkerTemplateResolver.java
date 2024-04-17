package org.springframework.template.freemarker;

import java.io.IOException;
import java.util.Locale;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;

public class FreemarkerTemplateResolver implements TemplateResolver {

	private final Configuration configuration;

	private String prefix = "";
	private String suffix = ".ftlh";
	private MimeType type = MimeTypeUtils.ALL;

	public FreemarkerTemplateResolver(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setType(MimeType type) {
		this.type = type;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		if (!this.type.isCompatibleWith(type)) {
			return null;
		}
		try {
			return new FreemarkerTemplate(configuration.getTemplate(prefix + path + suffix, locale));
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}catch (IOException e) {
			return null;
		}
	}


}
