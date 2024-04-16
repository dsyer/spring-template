package org.springframework.template.thymeleaf;

import java.util.Locale;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

public class ThymeleafTemplateResolver implements TemplateResolver {

	private final TemplateEngine engine;

	private String prefix = "";
	private String suffix = ".html";

	public ThymeleafTemplateResolver(TemplateEngine engine) {
		this.engine = engine;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		path = prefix + path + suffix;
		for (ITemplateResolver resolver : engine.getTemplateResolvers()) {
			TemplateResolution template = resolver.resolveTemplate(engine.getConfiguration(), null, path, null);
			if (template !=null && template.getTemplateResource().exists()) {
				// TODO: use mime type
				// TODO: look for fragment in path
				return new ThymeleafTemplate(engine, new TemplateSpec(path, "text/html"));
			}
		}
		return null;
	}

}
