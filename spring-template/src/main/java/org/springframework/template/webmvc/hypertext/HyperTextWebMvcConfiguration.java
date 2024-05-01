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

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.util.Assert;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HyperTextWebMvcConfiguration implements WebMvcConfigurer {

	private final ObjectFactory<ViewResolver> resolver;
	private final ObjectFactory<LocaleResolver> locales;
	private final ObjectMapper objectMapper;

	public HyperTextWebMvcConfiguration(ObjectFactory<ViewResolver> resolver,
			ObjectFactory<LocaleResolver> locales,
			ObjectMapper objectMapper) {
		Assert.notNull(resolver, "ViewResolver must not be null!");
		Assert.notNull(locales, "LocaleResolver must not be null!");

		this.resolver = resolver;
		this.locales = locales;
		this.objectMapper = objectMapper;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new HyperTextRequestHandlerMethodArgumentResolver());
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		handlers.add(new HyperTextResponseHandlerMethodReturnValueHandler(resolver.getObject(), locales, objectMapper));
	}
}
