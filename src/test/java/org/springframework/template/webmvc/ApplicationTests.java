package org.springframework.template.webmvc;

import java.time.Duration;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.template.Template;
import org.springframework.template.simple.SimpleTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

public class ApplicationTests {

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
		public String index() {
			return index.render(Map.of("name", "World"));
		}

		@GetMapping("/hello")
		public String hello() {
			return hello.render(Map.of("name", "World"));
		}

		@GetMapping(path = "/stream", produces = "text/event-stream")
		public Flux<String> stream() {
			return Flux.interval(Duration.ofSeconds(2))
					.map(count -> event.render(Map.of("count", count, "date", System.currentTimeMillis())));
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
