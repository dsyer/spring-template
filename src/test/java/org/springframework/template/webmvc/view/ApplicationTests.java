package org.springframework.template.webmvc.view;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.template.Template;
import org.springframework.template.simple.SimpleTemplate;
import org.springframework.template.webmvc.TemplateReturnValueHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import reactor.core.publisher.Flux;

@WebMvcTest(ApplicationTests.Application.class)
public class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void index() throws Exception {
		this.mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World!")));
	}

	@Test
	public void hello() throws Exception {
		this.mockMvc.perform(get("/hello"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Yo, World!")));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@SpringBootApplication
	@Controller
	public static class Application {

		private Template index;
		private Template hello;
		private Template event;

		public Application(Template index, Template hello, Template event) {
			this.index = index;
			this.hello = hello;
			this.event = event;
		}

		@GetMapping("/")
		public Template index(Map<String, Object> model) {
			model.put("name", "World");
			return index;
		}

		@GetMapping("/hello")
		public Template hello(Map<String, Object> model) {
			model.put("name", "World");
			return hello;
		}

		@GetMapping(path = "/stream", produces = "text/event-stream")
		public Flux<String> stream() {
			return Flux.interval(Duration.ofSeconds(2))
					.map(count -> event.render(Map.of("count", count, "date", System.currentTimeMillis())));
		}

		@Bean
		public WebMvcConfigurer webConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
					handlers.add(new TemplateReturnValueHandler());
				}
			};

		}

	}

	@Configuration
	static class ExtraConfiguration {

		@Bean
		public Template index() {
			return new SimpleTemplate("""
					<!DOCTYPE html>
					<html>
					<head>
						<title>Index</title>
					</head>
					<body>
						<h1>Hello, ${name}!</h1>
					</body>
					</html>
					""");
		}

		@Bean
		public Template hello() {
			return new SimpleTemplate("""
					<!DOCTYPE html>
					<html>
					<head>
						<title>Hello</title>
					</head>
					<body>
						<h1>Yo, ${name}!</h1>
					</body>
					</html>
					""");
		}

		@Bean
		public Template event() {
			return new SimpleTemplate("""
					<div>
						<span>${count}: ${date}</span>
					</div>""");
		}
	}

}
