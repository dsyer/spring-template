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
package org.springframework.template.st;

import java.util.Locale;
import java.util.Map;

import org.stringtemplate.v4.ST;

/**
 * Implementation of the {@link org.springframework.template.Template Template} interface for ST templates.
 */
public class STTemplate implements org.springframework.template.Template {

	private final String template;
	private final Locale locale;

	/**
	 * Constructs a new instance of the ST class with the specified template.
	 *
	 * @param template the string template
	 */
	public STTemplate(String template, Locale locale) {
		this.template = template;
		this.locale = locale;
	}

	/**
	 * Constructs a new instance of the class with the specified template.
	 *
	 * @param template the string template
	 */
	public STTemplate(String template) {
		this(template, Locale.getDefault());
	}

	/**
	 * Renders the ST template using the provided model.
	 *
	 * @param model the model containing the data to be rendered
	 * @return the rendered template as a string
	 */
	@Override
	public String render(Map<String, Object> model) {
		ST template = new ST(this.template);
		for (String key : model.keySet()) {
			template.add(key, model.get(key));
		}
		return template.render(locale);
	}

}
