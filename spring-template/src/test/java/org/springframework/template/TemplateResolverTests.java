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
package org.springframework.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.util.MimeType;

public class TemplateResolverTests {

	private TemplateResolver resolver = new TestTemplateResolver("foo");

	@Test
	public void testResolvePath() {
		TestTemplate template = (TestTemplate) resolver.resolve("foo");
		assertThat(template.path).isEqualTo("foo");
	}

	@Test
	public void testCannotResolvePath() {
		Template template = resolver.resolve("bar");
		assertThat(template).isNull();
	}

	private final static class TestTemplateResolver implements TemplateResolver {

		private Set<String> found;

		public TestTemplateResolver(String ...values) {
			found = new HashSet<>(Arrays.asList(values));
		}

		@Override
		public Template resolve(String path, MimeType type, Locale locale) {
			return found.contains(path) ? new TestTemplate(path) : null;
		}
	}

	static class TestTemplate implements Template {

		String path;

		TestTemplate(String path) {
			this.path = path;
		}

		@Override
		public String render(Map<String, Object> context) {
			return "";
		}
		
	}

}
