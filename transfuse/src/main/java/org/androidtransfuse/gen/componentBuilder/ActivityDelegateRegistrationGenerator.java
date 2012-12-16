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
package org.androidtransfuse.gen.componentBuilder;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTVoidType;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ActivityDelegateRegistrationGenerator implements RegistrationGenerator {

    private final ImmutableList<ASTMethod> methods;
    private final JCodeModel codeModel;
    private final UniqueVariableNamer namer;
    private final ActivityDelegateASTReference activityDelegateASTReference;


    @Inject
    public ActivityDelegateRegistrationGenerator(@Assisted ActivityDelegateASTReference activityDelegateASTReference,
                                                 @Assisted ImmutableList<ASTMethod> methods,
                                                 JCodeModel codeModel,
                                                 UniqueVariableNamer namer) {
        this.methods = methods;
        this.codeModel = codeModel;
        this.namer = namer;
        this.activityDelegateASTReference = activityDelegateASTReference;
    }

    @Override
    public void build(JDefinedClass definedClass, JBlock block, TypedExpression value) {

        for (ASTMethod method : methods) {
            //mirror method
            JMethod implementedMethod = definedClass.method(JMod.PUBLIC, codeModel.ref(method.getReturnType().getName()), method.getName());

            Map<ASTParameter, JVar> parameterMap = new HashMap<ASTParameter, JVar>();
            for (ASTParameter astParameter : method.getParameters()) {
                JVar param = implementedMethod.param(codeModel.ref(astParameter.getASTType().getName()), namer.generateName(astParameter.getASTType()));
                parameterMap.put(astParameter, param);
            }

            JExpression targetExpression = activityDelegateASTReference.buildReference(value);

            JInvocation delegateInvocation = targetExpression.invoke(method.getName());

            for (ASTParameter astParameter : method.getParameters()) {
                delegateInvocation.arg(parameterMap.get(astParameter));
            }

            if(ASTVoidType.VOID.equals(method.getReturnType())){
                implementedMethod.body().add(delegateInvocation);
            }
            else{
                implementedMethod.body()._return(delegateInvocation);
            }
        }
    }
}
