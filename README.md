A template abstraction for Spring for rendering text templates in a variety of formats, borrowing heavily from [Spring Restdocs](https://github.com/spring-projects/spring-restdocs) where similar concepts are found for generating snippets of documentation in tests.

Two central interfaces are the template, which can render with a provided context:

```java
public interface Template {
	String render(Map<String, Object> context);
}
```

and a template resolver that maps String paths to templates:

```java
public interface TemplateResolver {

	Template resolve(String path, MimeType type, Locale locale);

	default Template resolve(String path) {
		return resolve(path, MimeTypeUtils.ALL, Locale.getDefault());
	}

}
```

Implementations are provided for a variety of template formats, including:

* [FreeMarker](https://freemarker.apache.org/)
* [Mustache](https://github.com/samskivert/jmustache) (via JMustache)
* [Thymeleaf](https://thymeleaf.org/)
* Antlr [String Templates](https://github.com/antlr/stringtemplate4)
* `SimpleTemplate` is a dependency-free option with `${placeholder}` syntax
* `StringTemplate` is also dependency-free, and renders by simply calling `toString()` on the context object
* [JStachio](https://github.com/jstachio/jstachio) (reflection-free Mustache templates)

The "string" and "jstachio" options both render Java objects directly, so they don't require a template file (although it is an option with JStachio). The others require a template file on the classpath or other resource location, which is resolved by a `TemplateResolver`. A `TemplateResolver` is provided for "string" and "jstachio" templates that use a convention that the template is itself part of the model, 
which is artificial, but ensures compatibility with the other implementations.