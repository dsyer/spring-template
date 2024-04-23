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
package org.springframework.template.webmvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MultiViewResolver implements ViewResolver, Ordered {

	private final TemplateResolver resolver;
	private final MimeType type;

	public MultiViewResolver(TemplateResolver resolver) {
		this(resolver, MediaType.ALL);
	}

	public MultiViewResolver(TemplateResolver resolver, MimeType type) {
		this.resolver = resolver;
		this.type = type;
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 10;
	}

	@Override
	public View resolveViewName(String name, Locale locale) throws Exception {
		if (name.contains(",")) {
			List<Template> templates = new ArrayList<>();
			String[] names = name.split(",");
			for (int i = 0; i < names.length; i++) {
				names[i] = names[i].trim();
			}
			for (String template : names) {
				Template value = resolver.resolve(template, type, locale);
				if (value == null) {
					return null;
				}
				templates.add(value);
			}
			return new MultiView(templates);
		} else {
			return null;
		}
	}
	
}

class MultiView implements View {

	private final List<Template> templates;

	public MultiView(List<Template> templates) {
		this.templates = templates;
	}

	@Override
	public String getContentType() {
		return MediaType.TEXT_HTML_VALUE;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		for (Template template : templates) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) model;
			response.getWriter().write(template.render(map));
			response.getWriter().write("\n\n");
		}
	}

}