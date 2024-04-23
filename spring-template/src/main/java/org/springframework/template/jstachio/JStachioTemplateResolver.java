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
package org.springframework.template.jstachio;

import java.util.Locale;
import java.util.Map;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import io.jstach.jstachio.JStachio;

/**
 * A template resolver for JStachio templates using a naming convention that the
 * path is the same as the key in the context pointing to the model.
 */
public class JStachioTemplateResolver implements TemplateResolver {

	private final JStachio jstachio;

	private MimeType type = MimeTypeUtils.ALL;

	/**
	 * Constructs a new instance of the JStachioTemplateResolver class with the
	 * specified configuration.
	 *
	 * @param configuration the JStachio instance to use
	 */
	public JStachioTemplateResolver(JStachio jstachio) {
		this.jstachio = jstachio;
	}

	/**
	 * Sets the MIME type of the template.
	 *
	 * @param type the MIME type of the template
	 */
	public void setType(MimeType type) {
		this.type = type;
	}

	/**
	 * Resolves the template with the specified path, MIME type, and locale.
	 *
	 * @param path   the path of the template
	 * @param type   the MIME type of the template
	 * @param locale the locale of the template
	 * @return the resolved template, or null if the MIME type is not compatible
	 * @throws IllegalStateException if an error occurs while resolving the template
	 */
	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		if (!this.type.isCompatibleWith(type)) {
			return null;
		}
		return new LookupTemplate(jstachio, path);
	}

	private static class LookupTemplate implements Template {

		private final JStachio jstachio;
		private final String path;

		public LookupTemplate(JStachio jstachio, String path) {
			this.jstachio = jstachio;
			this.path = path;
		}

		@Override
		public String render(Map<String, Object> context) {
			Object model = context.get(path);
			if (model == null) {
				throw new IllegalArgumentException("Model does not contain key: " + path);
			}
			return new JStachioTemplate(jstachio, model).render(context);
		}
	}
}
