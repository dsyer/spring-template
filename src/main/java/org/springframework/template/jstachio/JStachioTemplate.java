package org.springframework.template.jstachio;

import java.util.Map;

import io.jstach.jstachio.JStachio;
import io.jstach.jstachio.Output.StringOutput;
import io.jstach.jstachio.context.ContextJStachio;
import io.jstach.jstachio.context.ContextNode;

public class JStachioTemplate implements org.springframework.template.Template {

	private final Object model;
	private final ContextJStachio jstachio;

	public JStachioTemplate(JStachio jstachio, Object model) {
		this.jstachio = ContextJStachio.of(jstachio);
		this.model = model;
	}

	@Override
	public String render(Map<String, Object> context) {
		StringBuilder output = new StringBuilder();
		jstachio.execute(model, ContextNode.of(context::get), new StringOutput(output));
		return output.toString();
	}

}
