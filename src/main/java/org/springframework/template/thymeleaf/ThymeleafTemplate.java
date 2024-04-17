package org.springframework.template.thymeleaf;

import java.util.Locale;
import java.util.Map;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;

public class ThymeleafTemplate implements org.springframework.template.Template {

	private final TemplateSpec template;
	private final ITemplateEngine engine;
	private final Locale locale;

	public ThymeleafTemplate(ITemplateEngine engine, TemplateSpec template) {
		this(engine, template, Locale.getDefault());
	}

	public ThymeleafTemplate(ITemplateEngine engine, TemplateSpec template, Locale locale) {
		this.engine = engine;
		this.template = template;
		this.locale = locale;
	}

	@Override
	public String render(Map<String, Object> context) {
		return engine.process(template, new Context(locale, context));
	}

}
