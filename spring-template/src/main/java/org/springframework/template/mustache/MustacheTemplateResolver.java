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
import org.springframework.template.path.PathGenerator;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import com.samskivert.mustache.Mustache.Compiler;

/**
 * A template resolver for Mustache templates.
 * This class implements the TemplateResolver interface and provides the
 * functionality to resolve Mustache templates.
 */
public class MustacheTemplateResolver implements TemplateResolver {

	private final Compiler compiler;
	private final ResourceLoader loader = new DefaultResourceLoader();

	private MimeType type = MimeTypeUtils.ALL;
	private PathGenerator paths = PathGenerator.infix("templates/", ".mustache");

	/**
	 * Constructs a new MustacheTemplateResolver with the specified compiler.
	 *
	 * @param compiler the compiler used to compile Mustache templates
	 */
	public MustacheTemplateResolver(Compiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * Sets the MIME type for the resolved templates.
	 *
	 * @param type the MIME type for the resolved templates
	 */
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
	 * Resolves a Mustache template based on the given path, MIME type, and locale.
	 *
	 * @param path   the path of the template
	 * @param type   the MIME type of the template
	 * @param locale the locale for the template
	 * @return the resolved template, or null if the template cannot be resolved
	 */
	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		if (!this.type.isCompatibleWith(type)) {
			return null;
		}
		for (String key : paths.generate(path)) {
			Resource resource = loader.getResource(key);
			if (resource != null && resource.exists()) {
				try {
					return new MustacheTemplate(compiler.compile(new InputStreamReader(resource.getInputStream())));
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		return null;
	}

}
