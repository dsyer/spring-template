package org.springframework.template.thymeleaf;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public class WebContextFactory implements ContextFactory {

	@Override
	public IContext create(Map<String, Object> context, Locale locale) {
		if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs) {
			JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(attrs.getRequest().getServletContext());
			IServletWebExchange exchange = application.buildExchange(attrs.getRequest(), attrs.getResponse());
			return new WebContext(exchange, locale, context);
		}
		return new Context(locale, context);
	}
	
}
