package org.springframework.experimental;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController {

	private final Application application;

	public SampleController(Application application) {
		this.application = application;
	}

	@GetMapping(path = "/")
	String index(Map<String, Object> model, @RequestHeader(name = "hx-request", required = false) String hx) {
		menu(model, "home");
		model.put("message", "Welcome");
		model.put("time", new Date());
		if ("true".equals(hx)) {
			return "index::main,layout::menu";
		}
		return "index";
	}

	@GetMapping(path = "/greet")
	String greet(Map<String, Object> model, @RequestHeader(name = "hx-request", required = false) String hx) {
		menu(model, "greet");
		model.put("greeting", "Hello World");
		model.put("time", new Date());
		if ("true".equals(hx)) {
			return "greet::main,layout::menu";
		}
		return "greet";
	}

	@GetMapping(path = "/menu")
	String menu(Map<String, Object> model, @RequestParam(defaultValue = "home") String active) {
		application.accept(Map.of("active", active));
		model.put("app", application);
		return "layout::menu";
	}

	@GetMapping(path = "/logo")
	String logo() {
		return "layout :: logo";
	}

	@PostMapping(path = "/greet")
	String name(Map<String, Object> model, @RequestParam String name,
			@RequestHeader(name = "hx-request", required = false) String hx) {
		greet(model, hx);
		model.put("greeting", "Hello " + name);
		model.put("name", name);
		if ("true".equals(hx)) {
			return "greet::main,layout::menu";
		}
		return "greet";
	}

}
