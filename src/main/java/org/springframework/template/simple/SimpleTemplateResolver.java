package org.springframework.template.simple;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.util.StreamUtils;

public class SimpleTemplateResolver implements TemplateResolver {

	private final ResourceLoader loader;

	private String prefix = "";
	private String suffix = ".tmpl";

	public SimpleTemplateResolver() {
		this(new DefaultResourceLoader());
	}

	public SimpleTemplateResolver(ResourceLoader loader) {
		this.loader = loader;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		try {
			Resource resource = loader.getResource(prefix + path + suffix);
			if (resource == null) {
				return null;
			}
			String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
			return new SimpleTemplate(template);
		}catch (IOException e) {
			return null;
		}
	}


}
