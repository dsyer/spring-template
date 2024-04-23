package org.springframework.template.webmvc.converter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.template.Template;
import org.springframework.template.simple.SimpleTemplate;
import org.springframework.template.webmvc.TemplateMessageConverter;
import org.springframework.template.webmvc.TemplateModel;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
	@RestController
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
		public TemplateModel index() {
			return new TemplateModel(index).add("name", "World");
		}

		@GetMapping("/hello")
		public TemplateModel hello() {
			return new TemplateModel(hello).add("name", "World");
		}

		@GetMapping(path = "/stream", produces = "text/event-stream")
		public Flux<TemplateModel> stream() {
			return Flux.interval(Duration.ofSeconds(2))
					.map(count -> new TemplateModel(event).add("count", count).add("date", System.currentTimeMillis()));
		}

		@Bean
		public WebMvcConfigurer webConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
					converters.add(0, new TemplateMessageConverter());
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
