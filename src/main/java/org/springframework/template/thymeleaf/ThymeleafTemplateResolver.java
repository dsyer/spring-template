package org.springframework.template.thymeleaf;

import java.util.Locale;
import java.util.Set;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.ExpressionContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.standard.expression.FragmentExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
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
		for (ITemplateResolver resolver : engine.getTemplateResolvers()) {
			PathSpec spec = PathSpec.from(engine, path, locale);
			path = prefix + spec.name() + suffix;
			TemplateResolution template = resolver.resolveTemplate(engine.getConfiguration(), null, path, null);
			if (template != null && template.getTemplateResource().exists()) {
				// TODO: use mime type
				return new ThymeleafTemplate(engine, new TemplateSpec(path, spec.selectors(), "text/html", null), locale);
			}
		}
		return null;
	}

	private record PathSpec(String name, Set<String> selectors) {
		static PathSpec from(TemplateEngine engine, String path, Locale locale) {
			final String templateName;
			final Set<String> markupSelectors;
			if (!path.contains("::")) {
				// No fragment specified at the template name

				templateName = path;
				markupSelectors = null;

			} else {

				ExpressionContext context = new ExpressionContext(engine.getConfiguration(), locale);

				final IStandardExpressionParser parser = StandardExpressions
						.getExpressionParser(engine.getConfiguration());

				final FragmentExpression fragmentExpression;
				try {
					// By parsing it as a standard expression, we might profit from the expression
					// cache
					fragmentExpression = (FragmentExpression) parser.parseExpression(context,
							"~{" + path + "}");
				} catch (final TemplateProcessingException e) {
					throw new IllegalArgumentException(
							"Invalid template name specification: '" + path + "'");
				}

				final FragmentExpression.ExecutedFragmentExpression fragment = FragmentExpression
						.createExecutedFragmentExpression(context, fragmentExpression);

				templateName = FragmentExpression.resolveTemplateName(fragment);
				markupSelectors = FragmentExpression.resolveFragments(fragment);
			}
			return new PathSpec(templateName, markupSelectors);
		}
	}
}
