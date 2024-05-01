package org.springframework.test;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.template.webmvc.hypertext.HyperTextRequest;
import org.springframework.template.webmvc.hypertext.HyperTextMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController {

	private final Application application;

	public SampleController(Application application) {
		this.application = application;
	}

	@GetMapping(path = "/")
	String index(Map<String, Object> model) {
		menu(model, "home");
		model.put("message", "Welcome");
		model.put("time", new Date());
		return "index";
	}

	@HyperTextMapping(headers = "hx-request=true")
	@GetMapping(path = "/")
	String indexHyper(Map<String, Object> model) {
		index(model);
		return "index::main,layout::menu";
	}

	@GetMapping(path = "/greet")
	String greet(Map<String, Object> model, HyperTextRequest hx) {
		menu(model, "greet");
		model.put("greeting", "Hello World");
		model.put("time", new Date());
		return hx.isActive() ? "greet::main,layout::menu" : "greet";
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
	String name(Map<String, Object> model, @RequestParam String name, HyperTextRequest hx) {
		greet(model, hx);
		model.put("greeting", "Hello " + name);
		model.put("name", name);
		return hx.isActive() ? "greet::main,layout::menu" : "greet";
	}

}
