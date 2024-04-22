package org.springframework.template.webmvc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.template.Template;
import org.springframework.template.simple.SimpleTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

		@GetMapping("/stream")
		public SseEmitter stream() {
			SseEmitter emitter = new SseEmitter();
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(() -> {
				try {
					for (int i=0; i<10; i++) {
						Thread.sleep(2000L);
						emitter.send(event.render(Map.of("count", i, "date", System.currentTimeMillis())));
					}
					emitter.complete();
				} catch (Exception e) {
					emitter.completeWithError(e);
				}
			});
			executor.shutdown();
			return emitter;
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
