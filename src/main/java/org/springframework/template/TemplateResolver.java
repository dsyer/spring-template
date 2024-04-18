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

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

/**
 * A contract for resolving templates based on the provided path, MIME type, and locale.
 */
public interface TemplateResolver {

	/**
	 * Resolves a template based on the given path, MIME type, and locale.
	 *
	 * @param path   the path of the template
	 * @param type   the MIME type of the template
	 * @param locale the locale for which the template should be resolved
	 * @return the resolved template
	 */
	Template resolve(String path, MimeType type, Locale locale);

	/**
	 * Resolves a template based on the given path using the default MIME type and locale.
	 *
	 * @param path the path of the template
	 * @return the resolved template
	 */
	default Template resolve(String path) {
		return resolve(path, MimeTypeUtils.ALL, Locale.getDefault());
	}

}