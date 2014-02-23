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
package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTStringType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.analysis.astAnalyzer.NonConfigurationAspect;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.ClassNamer;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class NonConfigurationInstanceGenerator implements ExpressionVariableDependentGenerator {

    private final UniqueVariableNamer variableNamer;
    private final ClassNamer classNamer;
    private final ClassGenerationUtil generationUtil;
    private final InvocationBuilder invocationBuilder;
    private final TypedExpressionFactory typeExpressionFactory;

    @Inject
    public NonConfigurationInstanceGenerator(UniqueVariableNamer variableNamer, ClassNamer classNamer, ClassGenerationUtil generationUtil, InvocationBuilder invocationBuilder, TypedExpressionFactory typeExpressionFactory) {
        this.variableNamer = variableNamer;
        this.classNamer = classNamer;
        this.generationUtil = generationUtil;
        this.invocationBuilder = invocationBuilder;
        this.typeExpressionFactory = typeExpressionFactory;
    }

    @Override
    public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor, JExpression scopesExpression) {

        try {
            List<InjectionNode> nonConfigurationComponents = buildNonConfigurationComponents(expressionMap);

            if (!nonConfigurationComponents.isEmpty()) {

                //generate holder type
                JDefinedClass nonConfigurationInstance = definedClass._class(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, classNamer.numberedClassName(new PackageClass(null, "NonConfigurationInstance")).build().getClassName());

                JMethod constructor = nonConfigurationInstance.constructor(JMod.PRIVATE);
                Map<FieldInjectionPoint, JFieldVar> fieldMap = configureConstructor(constructor, nonConfigurationInstance, nonConfigurationComponents);

                //add on create init
                //super.getLastNonConfigurationInstance()
                JBlock body = methodDescriptor.getMethod().body();
                JVar bodyVar = body.decl(nonConfigurationInstance, variableNamer.generateName(nonConfigurationInstance), JExpr.cast(nonConfigurationInstance, JExpr.invoke("getLastNonConfigurationInstance")));
                JBlock conditional = body._if(bodyVar.ne(JExpr._null()))._then();

                //assign variables
                for (InjectionNode nonConfigurationComponent : nonConfigurationComponents) {

                    NonConfigurationAspect aspect = nonConfigurationComponent.getAspect(NonConfigurationAspect.class);
                    for (FieldInjectionPoint nonConfigurationField : aspect.getFields()) {
                        TypedExpression fieldExpression = typeExpressionFactory.build(nonConfigurationField.getInjectionNode().getASTType(), JExpr.ref(bodyVar, fieldMap.get(nonConfigurationField)));
                        conditional.add(
                                invocationBuilder.buildFieldSet(
                                        new ASTStringType(definedClass.name()),
                                        fieldExpression,
                                        nonConfigurationField,
                                        expressionMap.get(nonConfigurationComponent).getExpression()));
                    }
                }

                //add to onRetainNonConfigurationInstance
                JMethod onNonConfigInst = definedClass.method(JMod.PUBLIC, Object.class, "onRetainNonConfigurationInstance");
                onNonConfigInst.annotate(Override.class);

                JBlock methodBody = onNonConfigInst.body();


                JInvocation construction = JExpr._new(nonConfigurationInstance);
                JVar instanceDecl = methodBody.decl(nonConfigurationInstance, variableNamer.generateName(nonConfigurationInstance)
                        , construction);

                for (InjectionNode injectionNode : nonConfigurationComponents) {

                    NonConfigurationAspect aspect = injectionNode.getAspect(NonConfigurationAspect.class);
                    for (FieldInjectionPoint fieldInjectionPoint : aspect.getFields()) {
                        construction.arg(invocationBuilder.buildFieldGet(
                                new ASTStringType(definedClass.name()),
                                fieldInjectionPoint.getField(),
                                injectionNode.getASTType(),
                                expressionMap.get(injectionNode)
                        ));
                    }
                }

                methodBody._return(instanceDecl);


            }
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already defined", e);
        }
    }

    private Map<FieldInjectionPoint, JFieldVar> configureConstructor(JMethod constructor, JDefinedClass nonConfigurationInstance, List<InjectionNode> nonConfigurationComponents) {
        Map<FieldInjectionPoint, JFieldVar> fieldMap = new HashMap<FieldInjectionPoint, JFieldVar>();
        for (InjectionNode injectionNode : nonConfigurationComponents) {
            NonConfigurationAspect aspect = injectionNode.getAspect(NonConfigurationAspect.class);
            for (FieldInjectionPoint fieldInjectionPoint : aspect.getFields()) {
                //add all fields to constructor in order
                JClass fieldNodeType = generationUtil.ref(fieldInjectionPoint.getInjectionNode().getASTType());
                JVar param = constructor.param(fieldNodeType, variableNamer.generateName(fieldInjectionPoint.getInjectionNode()));
                JFieldVar field = nonConfigurationInstance.field(JMod.PRIVATE, fieldNodeType, variableNamer.generateName(injectionNode));
                constructor.body().assign(field, param);
                fieldMap.put(fieldInjectionPoint, field);
            }
        }
        return fieldMap;
    }

    private List<InjectionNode> buildNonConfigurationComponents(Map<InjectionNode, TypedExpression> expressionMap) {
        List<InjectionNode> nonConfigurationComponents = new ArrayList<InjectionNode>();
        for (Map.Entry<InjectionNode, TypedExpression> expressionEntry : expressionMap.entrySet()) {

            if (expressionEntry.getKey().containsAspect(NonConfigurationAspect.class)) {
                nonConfigurationComponents.add(expressionEntry.getKey());
            }

        }
        return nonConfigurationComponents;
    }
}
