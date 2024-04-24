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
import org.springframework.template.path.PathGenerator;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.ExpressionContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.standard.expression.FragmentExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

/**
 * A template resolver implementation for Thymeleaf.
 * This class implements the TemplateResolver interface and provides methods to
 * set the prefix, suffix, and type of templates to be resolved.
 * The resolve method resolves the template path, type, and locale to find the
 * appropriate template using the configured template engine and template
 * resolvers.
 * If a matching template is found, it returns a ThymeleafTemplate object
 * representing the resolved template.
 * If no matching template is found, it returns null.
 */
public class ThymeleafTemplateResolver implements TemplateResolver {

	private final TemplateEngine engine;
	private final ContextFactory factory;

	private MimeType type = MimeTypeUtils.ALL;
	private TemplateMode mode;
	private PathGenerator paths = PathGenerator.infix("templates/", ".html");

	public ThymeleafTemplateResolver(TemplateEngine engine) {
		this(engine, ContextFactory.DEFAULT);
	}

	public ThymeleafTemplateResolver(TemplateEngine engine, ContextFactory factory) {
		this.engine = engine;
		this.factory = factory;
	}

	public void setType(MimeType type) {
		this.type = type;
	}

	/**
	 * Sets the path generator for resolving template paths.
	 *
	 * @param paths the path generator to set
	 */
	public void setPaths(PathGenerator paths) {
		this.paths = paths;
	}

	/**
	 * Forces all resolved templates to have the same Thymeleaf template mode.
	 * Default is null, which results in Thymeleaf assigning a template mode based
	 * on the resource path (e.g. <code>*.html</code> means <code>HTML</code>).
	 * 
	 * @param mode the template mode to assign to resolved templates
	 */
	public void setMode(TemplateMode mode) {
		this.mode = mode;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		for (ITemplateResolver resolver : engine.getTemplateResolvers()) {
			PathSpec spec = PathSpec.from(engine, path, locale);
			for (String key : paths.generate(spec.name())) {
				TemplateResolution template = resolver.resolveTemplate(engine.getConfiguration(), null, key, null);
				if (template != null && template.getTemplateResource().exists() && this.type.isCompatibleWith(type)) {
					if (this.mode != null) {
						return new ThymeleafTemplate(engine, factory, new TemplateSpec(key, spec.selectors(), this.mode, null),
								locale);
					}
					return new ThymeleafTemplate(engine, factory, new TemplateSpec(key, spec.selectors(), type.toString(), null),
							locale);
				}
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
