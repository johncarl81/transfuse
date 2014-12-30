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
package org.androidtransfuse.analysis;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.ASTVoidType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.analysis.astAnalyzer.ManualSuperAspect;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ManualSuperGenerator implements Generation {

    public static final String SUPER_NAME = "SUPER";
    private static final String SUPER_CALLER_CLASS_NAME = "SuperCaller";

    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;
    private final ASTElementFactory astElementFactory;
    private final ASTMethod registrationMethod;

    @org.androidtransfuse.annotations.Factory
    public interface Factory {

        ManualSuperGenerator build(ASTMethod registrationMethod);
    }

    @Inject
    public ManualSuperGenerator(UniqueVariableNamer namer, ClassGenerationUtil generationUtil, ASTElementFactory astElementFactory, ASTMethod registrationMethod) {
        this.namer = namer;
        this.generationUtil = generationUtil;
        this.astElementFactory = astElementFactory;
        this.registrationMethod = registrationMethod;
    }

    @Override
    public void schedule(final ComponentBuilder builder, final ComponentDescriptor descriptor) {

        builder.add(registrationMethod, GenerationPhase.POSTINJECTION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                ImmutableSet<ManualSuperAspect.Method> methods = FluentIterable.from(builder.getExpressionMap().keySet())
                        .transformAndConcat(new Function<InjectionNode, Set<ManualSuperAspect.Method>>() {
                            @Override
                            public Set<ManualSuperAspect.Method> apply(InjectionNode injectionNode) {
                                if (injectionNode.containsAspect(ManualSuperAspect.class)) {
                                    ManualSuperAspect aspect = injectionNode.getAspect(ManualSuperAspect.class);
                                    return aspect.getMethods();
                                }
                                return ImmutableSet.of();
                            }
                        })
                        .toSet();

                try {
                    if(!methods.isEmpty()) {
                        JDefinedClass superClass = builder.getDefinedClass()._class(JMod.PUBLIC, SUPER_CALLER_CLASS_NAME);

                        builder.getDefinedClass().field(JMod.PUBLIC, superClass, SUPER_NAME, JExpr._new(superClass));

                        for (ManualSuperAspect.Method method : methods) {
                            ASTMethod astMethod = astElementFactory.findMethod(descriptor.getType(), method.getName(), method.getParameters().toArray(new ASTType[method.getParameters().size()]));

                            if (astMethod != null) {
                                writeSuperCallingMethod(builder.getDefinedClass(), superClass, astMethod);
                            }
                            //todo: validation?
                        }
                    }
                } catch (JClassAlreadyExistsException e) {
                    throw new TransfuseAnalysisException("SUPER Class already exists", e);
                }
            }

            private void writeSuperCallingMethod(JDefinedClass target, JDefinedClass superClass, ASTMethod astMethod) {
                JMethod superCallingMethod = superClass.method(JMod.PUBLIC, generationUtil.ref(astMethod.getReturnType()), astMethod.getName());

                JBlock body = superCallingMethod.body();
                JInvocation invocation = target.staticRef("super").invoke(astMethod.getName());
                if(astMethod.getReturnType() == ASTVoidType.VOID){
                    body.add(invocation);
                }
                else{
                    body._return(invocation);
                }

                for (ASTParameter parameter : astMethod.getParameters()) {
                    JVar param = superCallingMethod.param(generationUtil.ref(parameter.getASTType()), namer.generateName(parameter.getASTType()));
                    invocation.arg(param);
                }
            }
        });

    }
}
