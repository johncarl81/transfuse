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
package org.androidtransfuse.analysis;

import com.google.common.base.Joiner;
import org.androidtransfuse.EventMapping;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.generators.MethodCallbackGenerator;
import org.androidtransfuse.experiment.generators.SuperGenerator;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ComponentAnalysis {

    private final ConfigurationRepositoryImpl repository;
    private final ASTElementFactory astElementFactory;
    private final ASTClassFactory astClassFactory;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final SuperGenerator.SuperGeneratorFactory superGeneratorFactory;

    @Inject
    public ComponentAnalysis(ConfigurationRepositoryImpl repository, ASTElementFactory astElementFactory, ASTClassFactory astClassFactory, ComponentBuilderFactory componentBuilderFactory, SuperGenerator.SuperGeneratorFactory superGeneratorFactory){
        this.repository = repository;
        this.astElementFactory = astElementFactory;
        this.astClassFactory = astClassFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.superGeneratorFactory = superGeneratorFactory;
    }


    public void setupGenerators(ComponentDescriptor descriptor, ASTType componentType, Class<? extends Annotation> componentAnnotation) {
        Registration registration = repository.getRegistration(componentType, componentAnnotation);
        ASTMethod registrationMethod = getASTMethod(componentType, registration.getMethodName(), registration.getParameters());
        descriptor.getGenerateFirst().add(new MethodSignature(registrationMethod));
        addSuperCalls(descriptor, componentType, componentAnnotation);
        addEvents(descriptor, componentType, componentAnnotation, registrationMethod);
    }

    private void addSuperCalls(ComponentDescriptor descriptor, ASTType componentType, Class<? extends Annotation> componentAnnotation) {
        for (SuperCallMapping superCallMapping : repository.getSuperCalls(componentType, componentAnnotation)) {
            descriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod(componentType, superCallMapping.getMethodName(), superCallMapping.getParameters())));
        }
    }

    public void addEvents(ComponentDescriptor descriptor, ASTType componentType, Class<? extends Annotation> componentAnnotation, ASTMethod registrationMethod) {
        for (EventMapping eventMapping : repository.getEvents(componentType, componentAnnotation)) {
            descriptor.getGenerators().add(buildEventMethod(componentType, eventMapping, registrationMethod));
        }
    }

    private MethodCallbackGenerator buildEventMethod(ASTType componentType, EventMapping eventMapping, ASTMethod registrationMethod){
        ASTMethod method = getASTMethod(componentType, eventMapping.getMethodName(), eventMapping.getMethodArguments());

        if(method == null){
            throw new TransfuseAnalysisException("Unable to find method with signature: " +
                    componentType + "." +
                    eventMapping.getMethodName() + "(" + Joiner.on(", ").join(eventMapping.getMethodArguments()) + ")");
        }

        ASTType eventAnnotation = astClassFactory.getType(eventMapping.getAnnotation());

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, method, registrationMethod);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, List<ASTType> args) {
        return astElementFactory.findMethod(type, methodName, args.toArray(new ASTType[args.size()]));
    }
}
