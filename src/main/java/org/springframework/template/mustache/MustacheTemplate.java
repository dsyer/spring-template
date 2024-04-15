package org.springframework.template.mustache;

import java.util.Map;

import com.samskivert.mustache.Template;

public class MustacheTemplate implements org.springframework.template.Template {

	private final Template template;

	public MustacheTemplate(Template template) {
		this.template = template;
	}

	@Override
	public String render(Map<String, Object> context) {
		return template.execute(context);
	}

}
