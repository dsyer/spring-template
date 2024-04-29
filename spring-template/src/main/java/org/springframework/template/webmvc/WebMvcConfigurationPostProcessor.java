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
package org.springframework.template.webmvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import jakarta.servlet.http.HttpServletRequest;

public class WebMvcConfigurationPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RequestToViewNameTranslator) {
			bean = new HeaderRequestToViewNameTranslator((RequestToViewNameTranslator) bean);
		}
		return bean;
	}

	private static class HeaderRequestToViewNameTranslator implements RequestToViewNameTranslator {

		private RequestToViewNameTranslator delegate;

		public HeaderRequestToViewNameTranslator(RequestToViewNameTranslator delegate) {
			this.delegate = delegate;
		}

		@Override
		public String getViewName(HttpServletRequest request) throws Exception {
			String name = delegate.getViewName(request);
			if (name == null || name.trim().isEmpty()) {
				name = "index";
			}
			if (request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE) instanceof HandlerMethod method) {
				ViewMaps maps = method.getMethodAnnotation(ViewMaps.class);
				String defaultName = name;
				boolean match = false;
				for (View translator : maps == null ? new View[] { method.getMethodAnnotation(View.class) }
						: maps.value()) {
					if (translator != null) {
						if (translator.headers().length == 0) {
							defaultName = translator.name();
						}
						for (String header : translator.headers()) {
							String key = header.contains("=") ? header.substring(0, header.indexOf('=')).trim()
									: header;
							String value = header.contains("=") ? header.substring(header.indexOf('=') + 1).trim()
									: null;
							if (request.getHeader(key) != null
									&& (value == null || request.getHeader(key).equals(value))) {
								match = true;
							} else {
								match = false;
								break;
							}
						}
						if (match) {
							name = translator.name();
							break;
						}
					}
				}
				if (!match) {
					name = defaultName;
				}
			}
			return name;
		}
	}
}
