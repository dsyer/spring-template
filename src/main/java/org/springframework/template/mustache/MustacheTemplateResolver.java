package org.springframework.template.mustache;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;

import com.samskivert.mustache.Mustache.Compiler;

public class MustacheTemplateResolver implements TemplateResolver {

	private final Compiler compiler;
	private final ResourceLoader loader = new DefaultResourceLoader();

	private String prefix = "";
	private String suffix = ".mustache";

	public MustacheTemplateResolver(Compiler compiler) {
		this.compiler = compiler;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		Resource resource = loader.getResource(prefix + path + suffix);
		if (resource == null) {
			return null;
		}
		try {
			return new MustacheTemplate(compiler.compile(new InputStreamReader(resource.getInputStream())));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
