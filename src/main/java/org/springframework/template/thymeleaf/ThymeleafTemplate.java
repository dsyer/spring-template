package org.springframework.template.thymeleaf;

import java.util.Map;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;

public class ThymeleafTemplate implements org.springframework.template.Template {

	private final TemplateSpec template;
	private final ITemplateEngine engine;

	public ThymeleafTemplate(ITemplateEngine engine, TemplateSpec template) {
		this.engine = engine;
		this.template = template;
	}

	@Override
	public String render(Map<String, Object> context) {
		return engine.process(template, new Context(null, context));
	}

}
