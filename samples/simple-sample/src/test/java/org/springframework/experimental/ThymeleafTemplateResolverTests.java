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
package org.springframework.experimental;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.template.Template;
import org.springframework.template.thymeleaf.ThymeleafTemplateResolver;
import org.springframework.util.MimeTypeUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafTemplateResolverTests {

	private ThymeleafTemplateResolver resolver;

	@BeforeEach
	public void setUp() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(new ClassLoaderTemplateResolver());
		resolver = new ThymeleafTemplateResolver(engine);
	}

	@Test
	public void testNotResolved() throws Exception {
		Template template = resolver.resolve("test");
		assertThat(template).isNull();
	}

	@Test
	public void testNotResolvedFragment() throws Exception {
		Template template = resolver.resolve("fragments :: garbage");
		assertThat(template).isNotNull();
		// Template exists but fragment does not. This is not an error?
		assertThat(template.render(Map.of())).isEmpty();
	}

	@Test
	public void testMimeTypeNotResolved() throws Exception {
		resolver.setType(MimeTypeUtils.TEXT_HTML);
		Template template = resolver.resolve("hello", MimeTypeUtils.APPLICATION_JSON, Locale.getDefault());
		assertThat(template).isNull();
	}

	@Test
	public void testResolveAndRender() throws Exception {
		Template template = resolver.resolve("hello");
		assertThat(template).isNotNull();

		Map<String, Object> context = new HashMap<>();
		context.put("name", "World");

		String result = template.render(context);

		assertEquals("Hello, World!", result);
	}

	@Test
	public void testResolveAndRenderFragment() throws Exception {
		Template template = resolver.resolve("fragments :: hello");
		assertThat(template).isNotNull();

		Map<String, Object> context = new HashMap<>();
		context.put("name", "World");

		String result = template.render(context).trim();

		assertEquals("Hello, World!", result);
	}
}