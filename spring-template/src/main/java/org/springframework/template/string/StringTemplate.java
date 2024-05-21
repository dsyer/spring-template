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

import java.util.Map;

/**
 * Implementation of the {@link org.springframework.template.Template Template}
 * interface for simple <code>toString()</code> based rendering. Useful, for
 * example, when native Java string templates are available and can be referred
 * to from the <code>toString()</code> method.
 */
public class StringTemplate implements org.springframework.template.Template {

	private final Object template;

	/**
	 * Constructs a new instance of the specified template.
	 *
	 * @param template the template (whose <code>toString()</code> method will be
	 *                 called)
	 */
	public StringTemplate(Object template) {
		this.template = template;
	}

	/**
	 * Renders the template ignoring the provided model.
	 *
	 * @param model the model containing the data (which are ignored)
	 * @return the rendered template as a string
	 */
	@Override
	public String render(Map<String, Object> model) {
		return template.toString();
	}

}
