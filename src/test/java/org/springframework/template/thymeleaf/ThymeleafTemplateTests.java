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
package org.springframework.template.thymeleaf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class ThymeleafTemplateTests {

	private static final String HTML_TEMPLATE = """
				Hello, <th:block th:text="${name}"></th:block>!
			""";

	private static final String TEXT_TEMPLATE = """
				Hello, [(${name})]!
			""";

	private static final String FRAGMENT_TEMPLATE = """
		<html>
			<body>
				<th:block th:fragment="hello">
					Hello, <th:block th:text="${name}"></th:block>!
				</th:block>
			</body>
		</html>
			""";

	private SpringTemplateEngine engine = new SpringTemplateEngine();

	@BeforeEach
	public void setUp() {
		engine.setTemplateResolver(new StringTemplateResolver());
	}

	@Test
	public void testRenderHtml() throws Exception {
		ThymeleafTemplate template = new ThymeleafTemplate(engine, new TemplateSpec(HTML_TEMPLATE, "text/html"));

		Map<String, Object> context = new HashMap<>();
		context.put("name", "World");

		String result = template.render(context);

		assertEquals("Hello, World!", result.trim());
	}

	@Test
	public void testRenderFragment() throws Exception {
		ThymeleafTemplate template = new ThymeleafTemplate(engine, new TemplateSpec(FRAGMENT_TEMPLATE, Set.of("hello"), "text/html", null));

		Map<String, Object> context = new HashMap<>();
		context.put("name", "World");

		String result = template.render(context);

		assertEquals("Hello, World!", result.trim());
	}

	@Test
	public void testRenderText() throws Exception {
		ThymeleafTemplate template = new ThymeleafTemplate(engine, new TemplateSpec(TEXT_TEMPLATE, "text/plain"));

		Map<String, Object> context = new HashMap<>();
		context.put("name", "World");

		String result = template.render(context);

		assertEquals("Hello, World!", result.trim());
	}

}