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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.template.path.PathGenerator;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

public class STTemplateResolver implements TemplateResolver {

	private final ResourceLoader loader;

	private MimeType type = MimeTypeUtils.ALL;
	private PathGenerator paths = PathGenerator.infix("templates/", ".st");

	private char start = '<';
	private char end = '>';

	public STTemplateResolver(ResourceLoader loader) {
		this.loader = loader;
	}

	public STTemplateResolver() {
		this(new DefaultResourceLoader());
	}

	public void setType(MimeType type) {
		this.type = type;
	}

	public void setStart(char start) {
		this.start = start;
	}

	public void setEnd(char end) {
		this.end = end;
	}

	/**
	 * Sets the path generator for resolving template paths.
	 *
	 * @param paths the path generator to set
	 */
	public void setPaths(PathGenerator paths) {
		this.paths = paths;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		if (!this.type.isCompatibleWith(type)) {
			return null;
		}
		for (String key : paths.generate(path)) {
			try {
				Resource resource = loader.getResource(key);
				if (resource == null || !this.type.isCompatibleWith(type)) {
					return null;
				}
				String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
				return new STTemplate(template, locale, start, end);
			} catch (IOException e) {
			}
		}
		return null;
	}

}
