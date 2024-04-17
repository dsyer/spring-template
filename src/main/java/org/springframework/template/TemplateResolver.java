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
package org.springframework.template;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

public interface TemplateResolver {

	Template resolve(String path, MimeType type, Locale locale);

	default Template resolve(String path) {
		return resolve(path, MimeTypeUtils.ALL, Locale.getDefault());
	}

	default Template resolve(Object model) {
		return resolve(model.getClass());
	}

	default Template resolve(Class<?> type) {
		Template template = null;
		for (String name : PathUtils.variants(type.getSimpleName())) {
			template = resolve(name);
			if (template != null) {
				break;
			}
		}
		if (template == null) {
			for (String name : PathUtils.paths(type.getName())) {
				template = resolve(name);
				if (template != null) {
					break;
				}
			}
		}
		return template;
	}

}

class PathUtils {

	private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");

	public static String toKebabCase(String name) {
		Matcher matcher = CAMEL_CASE_PATTERN.matcher(name);
		StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			matcher.appendReplacement(result, matcher.group(1) + '-' + StringUtils.uncapitalize(matcher.group(2)));
		}
		matcher.appendTail(result);
		return result.toString().toLowerCase(Locale.ENGLISH);
	}

	public static String[] paths(String name) {
		return new String[] { name.replace(".", "/"), name };
	}

	public static String[] variants(String name) {
		return new String[] { StringUtils.uncapitalize(name), name, toKebabCase(StringUtils.uncapitalize(name)) };
	}

}