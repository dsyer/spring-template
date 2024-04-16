package org.springframework.template;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

public interface TemplateResolver {

	Template resolve(String path, MimeType type, Locale locale);

	default Template resolve(String path) {
		return resolve(path, MimeTypeUtils.ALL, Locale.getDefault());
	}

	default Template resolve(Object model) {
		return resolve(model.getClass());
	}

	default Template resolve(Class<?> type) {
		Template template = null;
		for (String name : PathUtils.variants(type.getSimpleName())) {
			template = resolve(name);
			if (template != null) {
				break;
			}
		}
		if (template == null) {
			for (String name : PathUtils.paths(type.getName())) {
				template = resolve(name);
				if (template != null) {
					break;
				}
			}
		}
		return template;
	}

}

class PathUtils {

	private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");

	public static String toKebabCase(String name) {
		Matcher matcher = CAMEL_CASE_PATTERN.matcher(name);
		StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			matcher.appendReplacement(result, matcher.group(1) + '-' + StringUtils.uncapitalize(matcher.group(2)));
		}
		matcher.appendTail(result);
		return result.toString().toLowerCase(Locale.ENGLISH);
	}

	public static String[] paths(String name) {
		return new String[] { name.replace(".", "/"), name };
	}

	public static String[] variants(String name) {
		return new String[] { StringUtils.uncapitalize(name), name, toKebabCase(StringUtils.uncapitalize(name)) };
	}

}