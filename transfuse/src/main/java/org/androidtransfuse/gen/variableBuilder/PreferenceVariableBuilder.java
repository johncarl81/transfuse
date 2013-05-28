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

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class PreferenceVariableBuilder implements VariableBuilder {

    private final ASTType preferenceType;
    private final String preferenceName;
    private final InjectionNode preferenceManagerInjectionNode;
    private final TypedExpressionFactory typedExpressionFactory;
    private final InjectionExpressionBuilder injectionExpressionBuilder;

    private Map<ASTType, PrefGetBuilder> accessorMethods = new HashMap<ASTType, PrefGetBuilder>();

    @Inject
    public PreferenceVariableBuilder(/*@Assisted*/ ASTType preferenceType,
                                     /*@Assisted*/ String preferenceName,
                                     /*@Assisted*/ InjectionNode preferenceManagerInjectionNode,
                                     TypedExpressionFactory typedExpressionFactory, InjectionExpressionBuilder injectionExpressionBuilder, ASTClassFactory astClassFactory) {
        this.preferenceName = preferenceName;
        this.preferenceManagerInjectionNode = preferenceManagerInjectionNode;
        this.typedExpressionFactory = typedExpressionFactory;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.preferenceType = preferenceType;

        accessorMethods.put(astClassFactory.getType(String.class), new PrefGetBuilder("getString", JExpr.lit("")));
        accessorMethods.put(astClassFactory.getType(long.class), new PrefGetBuilder("getLong", JExpr.lit(0L)));
        accessorMethods.put(astClassFactory.getType(int.class), new PrefGetBuilder("getInt", JExpr.lit(0)));
        accessorMethods.put(astClassFactory.getType(float.class), new PrefGetBuilder("getFloat", JExpr.lit(0F)));
        accessorMethods.put(astClassFactory.getType(boolean.class), new PrefGetBuilder("getBoolean", JExpr.lit(false)));
        accessorMethods.put(astClassFactory.getType(Long.class), new PrefGetBuilder("getLong", JExpr.lit(0L)));
        accessorMethods.put(astClassFactory.getType(Integer.class), new PrefGetBuilder("getInt", JExpr.lit(0)));
        accessorMethods.put(astClassFactory.getType(Float.class), new PrefGetBuilder("getFloat", JExpr.lit(0F)));
        accessorMethods.put(astClassFactory.getType(Boolean.class), new PrefGetBuilder("getBoolean", JExpr.lit(false)));
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression preferenceManagerExpression = injectionExpressionBuilder.buildVariable(injectionBuilderContext, preferenceManagerInjectionNode);

        JExpression expression = invokePreferenceMethod(preferenceManagerExpression.getExpression());
        return typedExpressionFactory.build(preferenceType, expression);
    }

    private JExpression invokePreferenceMethod(JExpression preferences) {
        if (accessorMethods.containsKey(preferenceType)) {
            PrefGetBuilder getBuilder = accessorMethods.get(preferenceType);
            return preferences.invoke(getBuilder.getName()).arg(preferenceName).arg(getBuilder.getLit());
        }

        throw new TransfuseAnalysisException("Unable to find preference accessor method for " + preferenceType);
    }

    private static final class PrefGetBuilder {
        private String name;
        private JExpression lit;

        private PrefGetBuilder(String name, JExpression lit) {
            this.name = name;
            this.lit = lit;
        }

        public JExpression getLit() {
            return lit;
        }

        public String getName() {
            return name;
        }
    }
}
