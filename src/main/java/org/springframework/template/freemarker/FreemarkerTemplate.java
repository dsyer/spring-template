package org.springframework.template.freemarker;

import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class FreemarkerTemplate implements org.springframework.template.Template {

	private final Template template;
	private final Configuration configuration;

	public FreemarkerTemplate(Template template) {
		this.configuration = template.getConfiguration();
		this.template = template;
	}

	@Override
	public String render(Map<String, Object> model) {
		SimpleHash hash = buildTemplateModel(model);
		StringWriter writer = new StringWriter();
		try {
			template.process(hash, writer);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return writer.toString();
	}

	protected SimpleHash buildTemplateModel(Map<String, Object> model) {
		SimpleHash hash = new SimpleHash(configuration.getObjectWrapper());
		hash.putAll(model);
		return hash;
	}

}
