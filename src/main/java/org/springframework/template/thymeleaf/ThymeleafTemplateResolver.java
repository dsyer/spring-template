/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.springframework.template.thymeleaf;

import java.util.Locale;
import java.util.Set;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.ExpressionContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.standard.expression.FragmentExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

/**
 * A template resolver implementation for Thymeleaf.
 * This class implements the TemplateResolver interface and provides methods to set the prefix, suffix, and type of templates to be resolved.
 * The resolve method resolves the template path, type, and locale to find the appropriate template using the configured template engine and template resolvers.
 * If a matching template is found, it returns a ThymeleafTemplate object representing the resolved template.
 * If no matching template is found, it returns null.
 */
public class ThymeleafTemplateResolver implements TemplateResolver {

	private final TemplateEngine engine;

	private String prefix = "templates/";
	private String suffix = ".html";
	private MimeType type = MimeTypeUtils.ALL;

	public ThymeleafTemplateResolver(TemplateEngine engine) {
		this.engine = engine;
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
		for (ITemplateResolver resolver : engine.getTemplateResolvers()) {
			PathSpec spec = PathSpec.from(engine, path, locale);
			path = prefix + spec.name() + suffix;
			TemplateResolution template = resolver.resolveTemplate(engine.getConfiguration(), null, path, null);
			if (template != null && template.getTemplateResource().exists() && this.type.isCompatibleWith(type)) {
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
