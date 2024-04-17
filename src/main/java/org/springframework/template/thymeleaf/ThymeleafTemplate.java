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
