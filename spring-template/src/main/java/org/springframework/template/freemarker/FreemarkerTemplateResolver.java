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
package org.springframework.template.freemarker;

import java.io.IOException;
import java.util.Locale;

import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.template.path.PathGenerator;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;

public class FreemarkerTemplateResolver implements TemplateResolver {

	private final Configuration configuration;

	private MimeType type = MimeTypeUtils.ALL;
	private PathGenerator paths = PathGenerator.infix("templates/", ".ftlh");

	public FreemarkerTemplateResolver(Configuration configuration) {
		this.configuration = configuration;
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

	@Override
	public Template resolve(String path, MimeType type, Locale locale) {
		if (!this.type.isCompatibleWith(type)) {
			return null;
		}
		for (String key : paths.generate(path)) {
			try {
				return new FreemarkerTemplate(configuration.getTemplate(key, locale));
			} catch (ParseException e) {
				throw new IllegalStateException(e);
			} catch (IOException e) {
			}
		}
		return null;
	}

}
