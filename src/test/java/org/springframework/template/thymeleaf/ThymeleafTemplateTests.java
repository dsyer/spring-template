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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class ThymeleafTemplateTests {

	private static final String TEMPLATE = """
				<th:block th:text="'Hello, ' + ${name} + '!'"></th:block>
			""";
	private SpringTemplateEngine engine = new SpringTemplateEngine();

	@BeforeEach
	public void setUp() {
		engine.setTemplateResolver(new StringTemplateResolver());
	}

	@Test
	public void testRender() throws Exception {
		ThymeleafTemplate template = new ThymeleafTemplate(engine, new TemplateSpec(TEMPLATE, "text/html"));

		Map<String, Object> context = new HashMap<>();
		context.put("name", "World");

		String result = template.render(context);

		assertEquals("Hello, World!", result.trim());
	}

}