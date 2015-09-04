/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.experiment;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.MethodDescriptorBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class ComponentBuilder {

    private class MethodMetaData{
        private final Map<GenerationPhase, List<ComponentMethodGenerator>> generators = new HashMap<GenerationPhase, List<ComponentMethodGenerator>>();
        private final Map<GenerationPhase, List<ComponentMethodGenerator>> lazyGenerators = new HashMap<GenerationPhase, List<ComponentMethodGenerator>>();
        private final ASTMethod methodDefinition;

        private MethodMetaData(ASTMethod methodDefinition) {
            this.methodDefinition = methodDefinition;
        }

        public void putLazy(GenerationPhase phase, ComponentMethodGenerator partGenerator) {
            if(!lazyGenerators.containsKey(phase)){
                lazyGenerators.put(phase, new ArrayList<ComponentMethodGenerator>());
            }
            lazyGenerators.get(phase).add(partGenerator);
        }

        public void put(GenerationPhase phase, ComponentMethodGenerator partGenerator) {
            if(!generators.containsKey(phase)){
                generators.put(phase, new ArrayList<ComponentMethodGenerator>());
            }
            generators.get(phase).add(partGenerator);
        }

        public void build() {
            if(!generators.isEmpty()){
                MethodDescriptor descriptor = buildMethod();
                for (GenerationPhase phase : GenerationPhase.values()) {
                    if(lazyGenerators.containsKey(phase)){
                        for (ComponentMethodGenerator componentMethodGenerator : lazyGenerators.get(phase)) {
                            componentMethodGenerator.generate(descriptor, descriptor.getBody());
                        }
                    }
                    if(generators.containsKey(phase)){
                        for (ComponentMethodGenerator componentMethodGenerator : generators.get(phase)) {
                            componentMethodGenerator.generate(descriptor, descriptor.getBody());
                        }
                    }
                }
            }

        }
        private MethodDescriptor buildMethod() {
            JMethod method = getDefinedClass().method(JMod.PUBLIC, generationUtil.ref(methodDefinition.getReturnType()), methodDefinition.getName());
            method.annotate(Override.class);

            MethodDescriptorBuilder methodDescriptorBuilder = new MethodDescriptorBuilder(method, methodDefinition);

            //parameters
            for (ASTParameter astParameter : methodDefinition.getParameters()) {
                JVar param = method.param(generationUtil.ref(astParameter.getASTType()), variableNamer.generateName(astParameter.getASTType()));
                methodDescriptorBuilder.putParameter(astParameter, new TypedExpression(astParameter.getASTType(), param));
            }

            return methodDescriptorBuilder.build();
        }
    }

    private final Map<InjectionNode, TypedExpression> expressionMap = new HashMap<InjectionNode, TypedExpression>();
    private final ClassGenerationUtil generationUtil;
    private final ComponentDescriptor descriptor;
    private final UniqueVariableNamer variableNamer;

    private final Set<ComponentPartGenerator> generators = new HashSet<ComponentPartGenerator>();
    private final Map<MethodSignature, MethodMetaData> methodData = new HashMap<MethodSignature, MethodMetaData>();
    private JDefinedClass definedClass = null;
    private JVar scopes;

    @Inject
    public ComponentBuilder(ClassGenerationUtil generationUtil,
                            ComponentDescriptor descriptor,
                            UniqueVariableNamer variableNamer) {
        this.generationUtil = generationUtil;
        this.descriptor = descriptor;
        this.variableNamer = variableNamer;
    }

    public void build(){
        for (MethodSignature methodSignature : descriptor.getGenerateFirst()) {
            if(methodData.containsKey(methodSignature)) {
                methodData.get(methodSignature).build();
            }
        }

        for (Map.Entry<MethodSignature, MethodMetaData> entry : methodData.entrySet()) {
            if(!descriptor.getGenerateFirst().contains(entry.getKey())){
                entry.getValue().build();
            }
        }

        for (ComponentPartGenerator generator : generators) {
            generator.generate(descriptor);
        }
    }

    public void add(ComponentPartGenerator partGenerator) {
        generators.add(partGenerator);
    }

    public void addLazy(ASTMethod method, GenerationPhase phase, ComponentMethodGenerator partGenerator){
        MethodSignature methodSignature = new MethodSignature(method);
        if(!methodData.containsKey(methodSignature)){
            methodData.put(methodSignature, new MethodMetaData(method));
        }
        methodData.get(methodSignature).putLazy(phase, partGenerator);
    }

    public void add(ASTMethod method, GenerationPhase phase, ComponentMethodGenerator partGenerator){
        MethodSignature methodSignature = new MethodSignature(method);
        if(!methodData.containsKey(methodSignature)){
            methodData.put(methodSignature, new MethodMetaData(method));
        }
        methodData.get(methodSignature).put(phase, partGenerator);
    }

    public JDefinedClass getDefinedClass() {
        if (definedClass == null && descriptor.getType() != null) {
            try {
                definedClass = generationUtil.defineClass(descriptor.getPackageClass());
                definedClass._extends(generationUtil.ref(descriptor.getType()));
            } catch (JClassAlreadyExistsException e) {
                throw new TransfuseAnalysisException("Class Already Exists ", e);
            }
        }
        return definedClass;
    }

    public AnalysisContext getAnalysisContext() {
        return descriptor.getAnalysisContext();
    }

    public void setScopes(JVar scopes) {
        this.scopes = scopes;
    }

    public JVar getScopes() {
        return scopes;
    }

    public Map<InjectionNode, TypedExpression> getExpressionMap() {
        return expressionMap;
    }
}
