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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.util.ServletRequestPathUtils;

class WebMvcConfigurationPostProcessorTest {

	@Test
	void testDefaultView() throws Exception {
		WebMvcConfigurationPostProcessor processor = new WebMvcConfigurationPostProcessor();
		RequestToViewNameTranslator translator = (RequestToViewNameTranslator) processor
				.postProcessAfterInitialization(new DefaultRequestToViewNameTranslator(), "bean");
		assertThat(translator).isNotNull();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		ServletRequestPathUtils.parseAndCache(request);
		assertThat(translator.getViewName(request)).isEqualTo("index");
	}

	@Test
	void testOverrideView() throws Exception {
		WebMvcConfigurationPostProcessor processor = new WebMvcConfigurationPostProcessor();
		RequestToViewNameTranslator translator = (RequestToViewNameTranslator) processor
				.postProcessAfterInitialization(new DefaultRequestToViewNameTranslator(), "bean");
		assertThat(translator).isNotNull();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.setAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE, new HandlerMethod(new Object(), ReflectionUtils.findMethod(getClass(), "index")));
		ServletRequestPathUtils.parseAndCache(request);
		assertThat(translator.getViewName(request)).isEqualTo("foo");
	}

	@Test
	void testMatchView() throws Exception {
		WebMvcConfigurationPostProcessor processor = new WebMvcConfigurationPostProcessor();
		RequestToViewNameTranslator translator = (RequestToViewNameTranslator) processor
				.postProcessAfterInitialization(new DefaultRequestToViewNameTranslator(), "bean");
		assertThat(translator).isNotNull();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addHeader("bar", "true");
		request.setAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE, new HandlerMethod(new Object(), ReflectionUtils.findMethod(getClass(), "other")));
		ServletRequestPathUtils.parseAndCache(request);
		assertThat(translator.getViewName(request)).isEqualTo("bar");
	}

	@Test
	void testFallbackView() throws Exception {
		WebMvcConfigurationPostProcessor processor = new WebMvcConfigurationPostProcessor();
		RequestToViewNameTranslator translator = (RequestToViewNameTranslator) processor
				.postProcessAfterInitialization(new DefaultRequestToViewNameTranslator(), "bean");
		assertThat(translator).isNotNull();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.setAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE, new HandlerMethod(new Object(), ReflectionUtils.findMethod(getClass(), "other")));
		ServletRequestPathUtils.parseAndCache(request);
		assertThat(translator.getViewName(request)).isEqualTo("foo");
	}

	@View(name = "foo")
	void index() {}

	@View(name = "bar", headers = "bar=true")
	@View(name = "foo")
	void other() {}

}