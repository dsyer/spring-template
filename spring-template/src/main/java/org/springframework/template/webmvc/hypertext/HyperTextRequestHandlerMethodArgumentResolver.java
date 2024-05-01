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

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

public class HyperTextRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final String[] headerNames;

	public HyperTextRequestHandlerMethodArgumentResolver() {
		this("Hx-Request", "X-Up-Context", "X-Turbo-Request-Id");
	}

	public HyperTextRequestHandlerMethodArgumentResolver(String ...headerNames) {
		this.headerNames = headerNames;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(HyperTextRequest.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		return createHyperTextRequest(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
	}

	protected HyperTextRequest createHyperTextRequest(HttpServletRequest request) {
		for (String headerName : headerNames) {
			String hxRequestHeader = request.getHeader(headerName);
			if (hxRequestHeader != null) {
				return new HyperTextRequest();
			}
		}
		return HyperTextRequest.empty();
	}

}
