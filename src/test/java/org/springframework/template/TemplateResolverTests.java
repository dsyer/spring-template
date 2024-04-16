package org.springframework.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Date;
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

	@Test
	public void testResolveTypeUncap() {
		TestTemplate template = (TestTemplate) resolver.resolve(new Foo());
		assertThat(template.path).isEqualTo("foo");
	}

	@Test
	public void testResolveTypeKebab() {
		resolver = new TestTemplateResolver("foo-bar");
		TestTemplate template = (TestTemplate) resolver.resolve(new FooBar());
		System.err.println(new FooBar().getClass().getName());
		assertThat(template.path).isEqualTo("foo-bar");
	}

	@Test
	public void testResolveType() {
		resolver = new TestTemplateResolver("Foo");
		TestTemplate template = (TestTemplate) resolver.resolve(new Foo());
		assertThat(template.path).isEqualTo("Foo");
	}

	@Test
	public void testResolveTypeAsResource() {
		resolver = new TestTemplateResolver("java/util/Date");
		TestTemplate template = (TestTemplate) resolver.resolve(new Date());
		assertThat(template.path).isEqualTo("java/util/Date");
	}

	@Test
	public void testResolveFullType() {
		resolver = new TestTemplateResolver(Foo.class.getName());
		TestTemplate template = (TestTemplate) resolver.resolve(new Foo());
		assertThat(template.path).isEqualTo(Foo.class.getName());
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

	static class Foo {}

	static class FooBar {}

}