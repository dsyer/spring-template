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
package org.springframework.template.mustache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.template.Template;
import org.springframework.template.path.PathGenerator;
import org.springframework.template.path.PathGeneratorTemplateResolver;
import org.springframework.util.MimeTypeUtils;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Compiler;

public class MustacheTemplateResolverTests {

	private MustacheTemplateResolver base;
	private PathGeneratorTemplateResolver resolver;

	@BeforeEach
	public void setUp() {
		Compiler compiler = Mustache.compiler();
		base = new MustacheTemplateResolver(compiler);
		resolver = new PathGeneratorTemplateResolver(base, PathGenerator.infix("templates/", ".mustache"));
	}

	@Test
	public void testMimeTypeNotResolved() throws Exception {
		base.setType(MimeTypeUtils.TEXT_HTML);
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

}