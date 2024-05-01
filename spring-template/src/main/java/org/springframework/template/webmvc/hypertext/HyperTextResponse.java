/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.template.webmvc.hypertext;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

public class HyperTextResponse {

	private final Set<ModelAndView> views = new HashSet<>();
	private final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	private final Map<String, Map<String, Object>> detailHeaders = new LinkedHashMap<>();

	public Collection<ModelAndView> getViews() {
		return Collections.unmodifiableCollection(views);
	}

	public MultiValueMap<String, String> getHeaders() {
		return HttpHeaders.readOnlyHttpHeaders(headers);
	}

	public Map<String, Map<String, Object>> getDetailHeaders() {
		return Collections.unmodifiableMap(detailHeaders);
	}

	public HyperTextResponse addView(ModelAndView view) {
		views.add(view);
		return this;
	}

	public HyperTextResponse addHeader(String name, String value) {
		headers.add(name, value);
		return this;
	}

	public HyperTextResponse addDetailHeader(String name, String key, Object value) {
		detailHeaders.computeIfAbsent(name, k -> new LinkedHashMap<>()).put(key, value);
		return this;
	}

}
