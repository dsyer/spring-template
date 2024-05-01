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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class HyperTextResponseHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
	private final ViewResolver views;
	private final ObjectFactory<LocaleResolver> locales;
	private final ObjectMapper objectMapper;

	public HyperTextResponseHandlerMethodReturnValueHandler(ViewResolver views,
			ObjectFactory<LocaleResolver> locales,
			ObjectMapper objectMapper) {
		this.views = views;
		this.locales = locales;
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getParameterType().equals(HyperTextResponse.class);
	}

	@Override
	public void handleReturnValue(Object returnValue,
			MethodParameter returnType,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {

		HyperTextResponse htmxResponse = (HyperTextResponse) returnValue;
		mavContainer.setView(toView(htmxResponse));

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>(htmxResponse.getHeaders());
		addDetailHeaders(htmxResponse, headers);
		addHeaders(headers, webRequest.getNativeResponse(HttpServletResponse.class));
	}

	private View toView(HyperTextResponse htmxResponse) {

		Assert.notNull(htmxResponse, "HtmxResponse must not be null!");

		return (model, request, response) -> {
			Locale locale = locales.getObject().resolveLocale(request);
			ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
			for (ModelAndView modelAndView : htmxResponse.getViews()) {
				View view = modelAndView.getView();
				if (view == null) {
					view = views.resolveViewName(modelAndView.getViewName(), locale);
				}
				for (String key : model.keySet()) {
					if (!modelAndView.getModel().containsKey(key)) {
						modelAndView.getModel().put(key, model.get(key));
					}
				}
				Assert.notNull(view, "Template '" + modelAndView + "' could not be resolved");
				view.render(modelAndView.getModel(), request, wrapper);
			}
			wrapper.copyBodyToResponse();
		};
	}

	private void addDetailHeaders(HyperTextResponse htmxResponse, MultiValueMap<String, String> headers) {
		for (String name : htmxResponse.getHeaders().keySet()) {
			headers.add(name, jsonHeaders(htmxResponse.getDetailHeaders().get(name)));
		}
	}

	private String jsonHeaders(Map<String, Object> map) {
		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Unable to serialize " + map, e);
		}
	}

	private void addHeaders(MultiValueMap<String, String> headers, HttpServletResponse response) {
		for (String name : headers.keySet()) {
			addHeader(response, name, headers.get(name));
		}
	}

	private void addHeader(HttpServletResponse response, String headerName, List<String> values) {
		if (values.isEmpty()) {
			return;
		}

		String value = values.stream()
				.collect(Collectors.joining(","));

		response.setHeader(headerName, value);
		return;
	}

}
