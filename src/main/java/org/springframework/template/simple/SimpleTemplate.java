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

import java.util.HashMap;
import java.util.Map;

import org.springframework.template.Template;

/**
 * A simple implementation of the Template interface.
 * This class provides a basic template rendering functionality by replacing placeholders in the template string with corresponding values from a given context.
 */
public class SimpleTemplate implements Template {

	private static final int MAX_COUNT = 10;
	private final String template;

	public SimpleTemplate(String template) {
		this.template = template;
	}

	/**
	 * Renders the template by replacing placeholders with values from the given context.
	 *
	 * @param context the context containing key-value pairs for replacing placeholders
	 * @return the rendered template as a string
	 */
	@Override
	public String render(Map<String, Object> context) {
		String result = template;
		Map<String, String> values = extractValues(context);
		int count = 0;
		while (result.contains("${") && result.contains("}") && count++ < MAX_COUNT) {
			for (String key : values.keySet()) {
				String value = values.get(key);
				result = result.replace("${" + key + "}", value);
			}
		}
		return result;
	}

	private Map<String, String> extractValues(Map<String, Object> context) {
		HashMap<String, String> map = new HashMap<>();
		for (Map.Entry<String, Object> entry : context.entrySet()) {
			if (entry.getValue() instanceof Map) {
				String prefix = entry.getKey() + ".";
				@SuppressWarnings("unchecked")
				Map<String, String> nested = extractValues((Map<String, Object>) entry.getValue());
				for (Map.Entry<String, String> nestedEntry : nested.entrySet()) {
					map.put(prefix + nestedEntry.getKey(), nestedEntry.getValue());
				}
			} else {
				map.put(entry.getKey(), entry.getValue().toString());
			}
		}
		return map;
	}
}
