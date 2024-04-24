package org.springframework.experimental;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.template.Template;
import org.springframework.template.TemplateResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FragmentController {

	private final TemplateResolver resolver;
	private final ApplicationContext context;

	public FragmentController(TemplateResolver resolver, ApplicationContext context) {
		this.resolver = resolver;
		this.context = context;
	}

	@GetMapping("/fragment/{path}")
	public Template fragment(@PathVariable String path, @RequestParam Map<String,String> params, Map<String, Object> map, Locale locale) {
		String model = params.get("model");
		Parameterized bean = context.getBean(model, Parameterized.class);
		map.put(model, bean);
		return resolver.resolve(path, MediaType.TEXT_HTML, locale);
	}
	
	
}
