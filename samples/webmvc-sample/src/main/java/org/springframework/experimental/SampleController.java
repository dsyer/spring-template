package org.springframework.experimental;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.template.webmvc.View;
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
	@View(name = "index::main,layout::menu", headers = "hx-request=true")
	void index(Map<String, Object> model) {
		menu(model, "home");
		model.put("message", "Welcome");
		model.put("time", new Date());
	}

	@GetMapping(path = "/greet")
	@View(name = "greet::main,layout::menu", headers = "hx-request=true")
	@View(name = "greet")
	void greet(Map<String, Object> model) {
		menu(model, "greet");
		model.put("greeting", "Hello World");
		model.put("time", new Date());
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
	@View(name = "greet::main,layout::menu", headers = "hx-request=true")
	@View(name = "greet")
	void name(Map<String, Object> model, @RequestParam String name) {
		greet(model);
		model.put("greeting", "Hello " + name);
		model.put("name", name);
	}

}
