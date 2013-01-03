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

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectorsGenerator;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectorVariableBuilder extends ConsistentTypeVariableBuilder {

    private final ASTType injectorType;
    private final JCodeModel codeModel;

    @Inject
    public InjectorVariableBuilder(@Assisted ASTType injectorType, TypedExpressionFactory typedExpressionFactory, JCodeModel codeModel) {
        super(injectorType, typedExpressionFactory);
        this.injectorType = injectorType;
        this.codeModel = codeModel;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {

        JExpression injectorClass = codeModel.ref(injectorType.getName()).dotclass();

        return codeModel.ref(InjectorsGenerator.INJECTORS_NAME.getFullyQualifiedName())
                .staticInvoke(InjectorsGenerator.GET_METHOD)
                .arg(injectorClass);
    }
}
