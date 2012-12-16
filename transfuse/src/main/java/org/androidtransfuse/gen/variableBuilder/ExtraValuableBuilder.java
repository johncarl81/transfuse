/**
 * Copyright 2012 John Ericksen
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

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.ExtraUtil;
import org.androidtransfuse.util.ParcelWrapper;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraValuableBuilder extends ConsistentTypeVariableBuilder {

    private static final String GET_INTENT = "getIntent";
    private static final String GET_EXTRAS = "getExtras";

    private final boolean wrapped;
    private final String extraId;
    private final InjectionNode activityInjectionNode;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final boolean nullable;
    private final JCodeModel codeModel;

    @Inject
    public ExtraValuableBuilder(@Assisted String extraId,
                                @Assisted InjectionNode activityInjectionNode,
                                @Assisted("nullable") boolean nullable,
                                @Assisted("wrapped") boolean wrapped,
                                InjectionExpressionBuilder injectionExpressionBuilder,
                                JCodeModel codeModel,
                                TypedExpressionFactory typedExpressionFactory) {
        super(Object.class, typedExpressionFactory);
        this.extraId = extraId;
        this.activityInjectionNode = activityInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.nullable = nullable;
        this.codeModel = codeModel;
        this.wrapped = wrapped;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

        JInvocation getExtraInvocation = codeModel.ref(ExtraUtil.class)
                .staticInvoke(ExtraUtil.GET_INSTANCE).invoke(ExtraUtil.GET_EXTRA)
                .arg(contextVar.getExpression().invoke(GET_INTENT).invoke(GET_EXTRAS))
                .arg(JExpr.lit(extraId))
                .arg(JExpr.lit(nullable));

        if (wrapped) {
            getExtraInvocation = ((JExpression) JExpr.cast(codeModel.ref(ParcelWrapper.class),
                    getExtraInvocation)).invoke(ParcelWrapper.GET_PARCEL);
        }

        return getExtraInvocation;
    }
}
