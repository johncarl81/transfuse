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

import org.androidtransfuse.adapter.ASTStringType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ComponentBuilder {

    ConfigurationRepository repository;
    Class<? extends Annotation> componentAnnotation;
    ASTType componentType;

    public ComponentBuilder(ConfigurationRepository repository, Class<? extends Annotation> componentAnnotation) {
        this.repository = repository;
        this.componentAnnotation = componentAnnotation;
    }

    public MethodBuilder method(String methodName){
        return new MethodBuilder(methodName, Collections.EMPTY_LIST);
    }

    public MethodBuilder method(String methodName, String firstParameter, String... parameters){
        List<ASTType> parameterList = new ArrayList<ASTType>();
        parameterList.add(new ASTStringType(firstParameter));
        if(parameters != null){
            for (String parameter : parameters) {
                parameterList.add(new ASTStringType(parameter));
            }
        }
        return new MethodBuilder(methodName, parameterList);
    }

    public MethodBuilder method(String methodName, ASTType firstParameter, ASTType... parameters){
        List<ASTType> parameterList = new ArrayList<ASTType>();
        parameterList.add(firstParameter);
        if(parameters != null){
            parameterList.addAll(Arrays.asList(parameters));
        }
        return new MethodBuilder(methodName, parameterList);
    }

    public ComponentBuilder extending(String className) {
        if(componentType != null){
            //todo: throw Plugin Exception
        }
        componentType = new ASTStringType(className);
        return this;
    }

    public ComponentBuilder extending(ASTType componentType){
        if(componentType != null){
            //todo: throw Plugin Exception
        }
        this.componentType = componentType;
        return this;
    }

    public MappingBuilder mapping(ASTType type) {
        return new MappingBuilder(type);
    }


    public void callThroughEvent(Class<?> callThroughEventClass) {
        repository.addCallThroughEvent(componentAnnotation, componentType, callThroughEventClass);
    }

    public void listener(ASTType listenerType, String listenerMethod) {
        repository.addListener(componentAnnotation, componentType, listenerType, listenerMethod);
    }

    public class MethodBuilder {

        private final String methodName;
        private final List<ASTType> parameters;

        private MethodBuilder(String methodName, List<ASTType> parameters) {
            this.methodName = methodName;
            this.parameters = parameters;
        }

        public MethodBuilder event(Class<? extends Annotation> eventAnnotation) {
            repository.addEvent(componentAnnotation, componentType, new EventMapping(methodName, parameters, eventAnnotation));
            return this;
        }

        public MethodBuilder superCall(){
            repository.addSuperCall(componentAnnotation, componentType, methodName, parameters);
            return this;
        }

        public MethodBuilder registration() {
            repository.addRegistration(componentAnnotation, componentType, methodName, parameters);
            return this;
        }
    }

    public class MappingBuilder {
        private ASTType type;

        private MappingBuilder(ASTType type) {
            this.type = type;
        }

        public void to(InjectionNodeBuilder builder){
            repository.addMapping(componentAnnotation, componentType, new InjectionMapping(type, builder));
        }
    }
}
