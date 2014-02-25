/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public class ComponentBuilder {

    ConfigurationRepository repository;
    Class<? extends Annotation> componentAnnotation;
    String componentType;

    public ComponentBuilder(ConfigurationRepository repository, Class<? extends Annotation> componentAnnotation) {
        this.repository = repository;
        this.componentAnnotation = componentAnnotation;
    }

    public MethodBuilder method(String methodName, String... parameters){
        return new MethodBuilder(methodName, parameters);
    }

    public ComponentBuilder extending(String className) {
        if(componentType != null){
            //todo: throw Plugin Exception
        }
        componentType = className;
        return this;
    }

    public MappingBuilder mapping(ASTType type) {
        return new MappingBuilder(type);
    }

    public class MethodBuilder {

        private final String methodName;
        private final String[] parameters;

        public MethodBuilder(String methodName, String[] parameters) {
            this.methodName = methodName;
            this.parameters = parameters;
        }

        public MethodBuilder event(Class<? extends Annotation> eventAnnotation) {
            repository.addEvent(componentAnnotation, componentType, new EventMapping(methodName, parameters, eventAnnotation));
            return this;
        }
    }

    public class MappingBuilder {
        private ASTType type;

        public MappingBuilder(ASTType type) {
            this.type = type;
        }

        public void to(InjectionNodeBuilder builder){
            repository.addMapping(componentAnnotation, componentType, new InjectionMapping(type, builder));
        }
    }

}
