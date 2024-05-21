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
package org.springframework.template.string;

import java.util.Locale;
import java.util.Map;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public class StringTemplateResolver implements TemplateResolver {

	private MimeType type = MimeTypeUtils.ALL;

	public void setType(MimeType type) {
		this.type = type;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		if (!this.type.isCompatibleWith(type)) {
			return null;
		}
		return new LookupTemplate(path);
	}

	private static class LookupTemplate implements Template {

		private final String path;

		public LookupTemplate(String path) {
			this.path = path;
		}

		@Override
		public String render(Map<String, Object> context) {
			Object model = context.get(path);
			if (model == null) {
				throw new IllegalArgumentException("Model does not contain key: " + path);
			}
			return new StringTemplate(model).render(context);
		}
	}
}
