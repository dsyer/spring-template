/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.template.webmvc.hypertext;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.condition.CompositeRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class HyperTextRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		HyperTextMapping methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, HyperTextMapping.class);
		return createCondition(methodAnnotation);
	}

	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		HyperTextMapping typeAnnotation = AnnotatedElementUtils.findMergedAnnotation(handlerType,
				HyperTextMapping.class);
		return createCondition(typeAnnotation);
	}

	private RequestCondition<?> createCondition(HyperTextMapping hxRequest) {
		if (hxRequest != null) {
			var conditions = new ArrayList<RequestCondition<?>>();
			for (String header : hxRequest.headers()) {
				conditions.add(new HeadersRequestCondition(header));
			}
			return new CompositeRequestCondition(conditions.toArray(RequestCondition[]::new));
		}

		return null;
	}

}
