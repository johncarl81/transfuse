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
package org.androidtransfuse.gen.variableBuilder;

import android.view.View;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTVoidType;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.config.Nullable;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ViewVariableBuilder extends ConsistentTypeVariableBuilder {

    private static final String FIND_VIEW_BY_ID = "findViewById";
    private static final String FIND_VIEW_BY_TAG = "findViewWithTag";

    private final JType viewType;
    private final Integer viewId;
    private final String viewTag;
    private final InjectionNode activityInjectionNode;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final RResourceReferenceBuilder rResourceReferenceBuilder;
    private final JCodeModel codeModel;
    private final UniqueVariableNamer variableNamer;
    private final InvocationBuilder injectionInvocationBuilder;
    private final RResource rResource;
    private final GeneratorFactory generatorFactory;

    @Inject
    public ViewVariableBuilder(@Assisted @Nullable Integer viewId,
                               @Assisted @Nullable String viewTag,
                               @Assisted InjectionNode activityInjectionNode,
                               @Assisted JType viewType,
                               InjectionExpressionBuilder injectionExpressionBuilder,
                               RResourceReferenceBuilder rResourceReferenceBuilder,
                               JCodeModel codeModel,
                               InvocationBuilder injectionInvocationBuilder,
                               UniqueVariableNamer variableNamer,
                               RResource rResource,
                               TypedExpressionFactory typedExpressionFactory,
                               GeneratorFactory generatorFactory) {
        super(View.class, typedExpressionFactory);
        this.viewId = viewId;
        this.viewTag = viewTag;
        this.activityInjectionNode = activityInjectionNode;
        this.viewType = viewType;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.codeModel = codeModel;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.variableNamer = variableNamer;
        this.rResource = rResource;
        this.generatorFactory = generatorFactory;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        try {
            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, injectionNode);

            TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

            JExpression viewExpression;
            if (viewId != null) {
                ResourceIdentifier viewResourceIdentifier = rResource.getResourceIdentifier(viewId);
                JExpression viewIdRef = rResourceReferenceBuilder.buildReference(viewResourceIdentifier);
                viewExpression = contextVar.getExpression().invoke(FIND_VIEW_BY_ID).arg(viewIdRef);
            } else {
                //viewTag is not null
                //<Activity>.getWindow().getDecorView().findViewWithTag(...)
                viewExpression = contextVar.getExpression()
                        .invoke("getWindow").invoke("getDecorView").invoke(FIND_VIEW_BY_TAG).arg(JExpr.lit(viewTag));
            }

            ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
            if (injectionAspect == null) {
                return viewExpression;
            } else {
                return inject(injectionAspect, injectionBuilderContext, injectionNode, viewExpression);
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }
    }

    public JVar inject(ASTInjectionAspect injectionAspect, InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode, JExpression viewExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        JVar variableRef;
        JType nodeType = codeModel.parseType(injectionNode.getClassName());

        if (injectionAspect.getAssignmentType().equals(ASTInjectionAspect.InjectionAssignmentType.LOCAL)) {
            variableRef = injectionBuilderContext.getBlock().decl(nodeType, variableNamer.generateName(injectionNode));
        } else {
            variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(injectionNode));
        }
        JBlock block = injectionBuilderContext.getBlock();


        block.assign(variableRef, JExpr.cast(viewType, viewExpression));

        //field injection
        for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
            block.add(
                    injectionInvocationBuilder.buildFieldSet(
                            injectionBuilderContext.getVariableMap().get(fieldInjectionPoint.getInjectionNode()),
                            fieldInjectionPoint,
                            variableRef));
        }

        //method injection
        for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
            block.add(
                    injectionInvocationBuilder.buildMethodCall(
                            ASTVoidType.VOID,
                            methodInjectionPoint,
                            generatorFactory.buildExpressionMatchingIterable(
                                    injectionBuilderContext.getVariableMap(),
                                    methodInjectionPoint.getInjectionNodes()),
                            variableRef));
        }

        return variableRef;
    }
}