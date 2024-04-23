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
package org.springframework.template.st;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.template.Template;
import org.springframework.util.MimeTypeUtils;

public class STTemplateResolverTests {

	private STTemplateResolver resolver;

	@BeforeEach
	public void setUp() throws Exception {
		resolver = new STTemplateResolver();
	}

	@Test
	public void testMimeTypeNotResolved() throws Exception {
		resolver.setType(MimeTypeUtils.TEXT_HTML);
		Template template = resolver.resolve("test", MimeTypeUtils.APPLICATION_JSON, Locale.getDefault());
		assertThat(template).isNull();
	}

	@Test
	public void testResolveAndRender() throws Exception {
		Template template = resolver.resolve("test");
		assertThat(template).isNotNull();

		Map<String, Object> context = new HashMap<>();
		context.put("name", "World");

		String result = template.render(context);

		assertEquals("Hello, World!", result);
	}

}