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
package org.androidtransfuse.experiment;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.MethodDescriptorBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class ComponentBuilder {

    private final ClassGenerationUtil generationUtil;
    private final ComponentDescriptor descriptor;
    private final UniqueVariableNamer variableNamer;

    private final Set<ComponentPartGenerator> generators = new HashSet<ComponentPartGenerator>();
    private final Map<GenerationPhase, Map<MethodSignature, List<ComponentMethodGenerator>>> phaseGenerators = new HashMap<GenerationPhase, Map<MethodSignature, List<ComponentMethodGenerator>>>();
    private final Map<MethodSignature, ASTMethod> methodSignatureDefinitions = new HashMap<MethodSignature, ASTMethod>();
    private final Map<MethodSignature, MethodDescriptor> definedMethods = new HashMap<MethodSignature, MethodDescriptor>();
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

    public void buildPhase(GenerationPhase phase) {

        // In order of GenerationPhase enum cardinal value
        if(phaseGenerators.containsKey(phase)) {
            for (MethodSignature methodSignature : phaseGenerators.get(phase).keySet()) {
                if (phaseGenerators.get(phase).containsKey(methodSignature)) {
                    for (ComponentMethodGenerator componentMethodGenerator : phaseGenerators.get(phase).get(methodSignature)) {
                        MethodDescriptor methodDescriptor = getMethod(methodSignature);
                        componentMethodGenerator.generate(methodDescriptor, methodDescriptor.getMethod().body());
                    }
                }
            }
        }
    }

    public void build(){
        Set<ComponentPartGenerator> buildGenerators = new HashSet<ComponentPartGenerator>(generators);
        generators.clear();

        for (ComponentPartGenerator generator : buildGenerators) {
            generator.generate(descriptor);
        }
    }

    public void add(ComponentPartGenerator partGenerator) {
        generators.add(partGenerator);
    }

    public void add(ASTMethod method, GenerationPhase phase, ComponentMethodGenerator partGenerator) {
        MethodSignature methodSignature = new MethodSignature(method);
        if (!phaseGenerators.containsKey(phase)) {
            phaseGenerators.put(phase, new HashMap<MethodSignature, List<ComponentMethodGenerator>>());
        }
        if (!phaseGenerators.get(phase).containsKey(methodSignature)) {
            phaseGenerators.get(phase).put(methodSignature, new ArrayList<ComponentMethodGenerator>());
        }
        if(!methodSignatureDefinitions.containsKey(methodSignature)){
            methodSignatureDefinitions.put(methodSignature, method);
        }
        phaseGenerators.get(phase).get(methodSignature).add(partGenerator);
    }

    public JDefinedClass getDefinedClass() {
        if (definedClass == null) {
            try {
                definedClass = generationUtil.defineClass(descriptor.getPackageClass());
                definedClass._extends(generationUtil.ref(descriptor.getType()));
            } catch (JClassAlreadyExistsException e) {
                throw new TransfuseAnalysisException("Class Already Exists ", e);
            }
        }
        return definedClass;
    }

    private MethodDescriptor getMethod(MethodSignature methodSignature) {
        if (!definedMethods.containsKey(methodSignature)) {
            ASTMethod methodDefinition = methodSignatureDefinitions.get(methodSignature);
            JMethod method = getDefinedClass().method(JMod.PUBLIC, generationUtil.ref(methodDefinition.getReturnType()), methodDefinition.getName());
            method.annotate(Override.class);

            MethodDescriptorBuilder methodDescriptorBuilder = new MethodDescriptorBuilder(method, methodDefinition);

            //parameters
            for (ASTParameter astParameter : methodDefinition.getParameters()) {
                JVar param = method.param(generationUtil.ref(astParameter.getASTType()), variableNamer.generateName(astParameter.getASTType()));
                methodDescriptorBuilder.putParameter(astParameter, new TypedExpression(astParameter.getASTType(), param));
            }

            definedMethods.put(methodSignature, methodDescriptorBuilder.build());
        }
        return definedMethods.get(methodSignature);
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
}
