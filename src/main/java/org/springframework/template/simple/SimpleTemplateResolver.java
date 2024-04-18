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
package org.springframework.template.simple;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

/**
 * A template resolver implementation that resolves templates using a simple approach.
 * Templates are resolved by combining a prefix, path, and suffix to form the resource location.
 * The resolved template is then used to create a new instance of the {@link SimpleTemplate} class.
 */
public class SimpleTemplateResolver implements TemplateResolver {

	private final ResourceLoader loader;

	private MimeType type = MimeTypeUtils.ALL;

	public SimpleTemplateResolver() {
		this(new DefaultResourceLoader());
	}

	public void setType(MimeType type) {
		this.type = type;
	}

	public SimpleTemplateResolver(ResourceLoader loader) {
		this.loader = loader;
	}

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		try {
			Resource resource = loader.getResource(path);
			if (resource == null || !this.type.isCompatibleWith(type)) {
				return null;
			}
			String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
			return new SimpleTemplate(template);
		}catch (IOException e) {
			return null;
		}
	}


}
