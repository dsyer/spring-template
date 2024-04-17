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
package org.springframework.template.mustache;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import com.samskivert.mustache.Mustache.Compiler;

public class MustacheTemplateResolver implements TemplateResolver {

	private final Compiler compiler;
	private final ResourceLoader loader = new DefaultResourceLoader();

	private String prefix = "";
	private String suffix = ".mustache";
	private MimeType type = MimeTypeUtils.ALL;

	public MustacheTemplateResolver(Compiler compiler) {
		this.compiler = compiler;
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
		Resource resource = loader.getResource(prefix + path + suffix);
		if (resource == null || !this.type.isCompatibleWith(type)) {
			return null;
		}
		try {
			return new MustacheTemplate(compiler.compile(new InputStreamReader(resource.getInputStream())));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
