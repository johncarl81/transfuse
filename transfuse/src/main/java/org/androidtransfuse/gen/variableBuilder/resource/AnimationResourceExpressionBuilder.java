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
package org.androidtransfuse.gen.variableBuilder.resource;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnimationResourceExpressionBuilder implements ResourceExpressionBuilder {

    private static final String LOAD_ANIMATION = "loadAnimation";

    private final JCodeModel codeModel;
    private final InjectionNode applicationInjectionNode;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;

    @Inject
    public AnimationResourceExpressionBuilder(@Assisted InjectionNode applicationInjectionNode,
                                              JCodeModel codeModel,
                                              InjectionExpressionBuilder injectionExpressionBuilder, TypedExpressionFactory typedExpressionFactory) {
        this.codeModel = codeModel;
        this.applicationInjectionNode = applicationInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        TypedExpression applicationVar = injectionExpressionBuilder.buildVariable(context, applicationInjectionNode);

        //AnimationUtils.loadAnimation(application, id);
        JExpression expression = codeModel.ref(AnimationUtils.class).staticInvoke(LOAD_ANIMATION).arg(applicationVar.getExpression()).arg(resourceIdExpr);
        return typedExpressionFactory.build(Animation.class, expression);
    }
}
