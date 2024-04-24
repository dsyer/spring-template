package org.springframework.template.webmvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.template.Template;
import org.springframework.template.thymeleaf.ThymeleafTemplateResolver;
import org.springframework.template.thymeleaf.WebContextFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class WebThymeleafTemplateResolverTests {

	private ThymeleafTemplateResolver resolver;

	@BeforeEach
	public void setUp() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(new ClassLoaderTemplateResolver());
		resolver = new ThymeleafTemplateResolver(engine, new WebContextFactory());
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest(), new MockHttpServletResponse()));
	}

	@AfterEach
	public void tearDown() {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	public void testResolveAndRenderWebContext() throws Exception {
		Template template = resolver.resolve("web");
		assertThat(template).isNotNull();

		Map<String, Object> context = new HashMap<>();
		context.put("url", "/world");
		context.put("name", "world");

		String result = template.render(context);

		assertEquals("Hello, <a href=\"/world\">world</a>!", result);
	}

}
