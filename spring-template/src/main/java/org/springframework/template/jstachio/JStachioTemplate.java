/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.springframework.template.jstachio;

import java.util.Map;

import io.jstach.jstachio.JStachio;
import io.jstach.jstachio.Output.StringOutput;
import io.jstach.jstachio.context.ContextJStachio;
import io.jstach.jstachio.context.ContextNode;

/**
 * Represents a JStachio template that can be rendered with a given model.
 */
public class JStachioTemplate implements org.springframework.template.Template {

	private final Object model;
	private final ContextJStachio jstachio;

	/**
	 * Constructs a new JStachioTemplate with the specified JStachio instance and model.
	 *
	 * @param jstachio the JStachio instance to use for rendering the template
	 * @param model the model object to be used as the context for rendering the template
	 */
	public JStachioTemplate(JStachio jstachio, Object model) {
		this.jstachio = ContextJStachio.of(jstachio);
		this.model = model;
	}

	/**
	 * Renders the template with the given context and returns the rendered output as a string.
	 *
	 * @param context the context map containing the variables to be used during rendering
	 * @return the rendered output as a string
	 */
	@Override
	public String render(Map<String, Object> context) {
		StringBuilder output = new StringBuilder();
		jstachio.execute(model, ContextNode.of(context::get), new StringOutput(output));
		return output.toString();
	}

}
