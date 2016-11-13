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
package org.androidtransfuse.gen;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.gen.invocationBuilder.InvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.ModifiedInvocationBuilder;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;

/**
 * Builds the invocations of constructors, methods and field get/set.
 *
 * @author John Ericksen
 */
public class InvocationBuilder {

    private final InvocationBuilderStrategy invocationBuilderStrategy;

    @Inject
    public InvocationBuilder(InvocationBuilderStrategy invocationBuilderStrategy) {
        this.invocationBuilderStrategy = invocationBuilderStrategy;
    }

    public JInvocation buildMethodCall(ASTType userType, ASTType containingRootType, ASTMethod method, List<? extends JExpression> parameters, TypedExpression expression) {

        boolean cast = isHiddenMethod(containingRootType, expression.getType(), method);
        ModifiedInvocationBuilder injectionBuilder = getInjectionBuilder(userType, expression.getType(), method.getAccessModifier());

        return injectionBuilder.buildMethodCall(cast, method, parameters, expression);
    }

    private boolean isHiddenMethod(ASTType rootType, ASTType holdingType, ASTMethod inputMethod){
        return inputMethod.getAccessModifier().equals(ASTAccessModifier.PACKAGE_PRIVATE) &&
                !rootType.getPackageClass().getPackage().equals(holdingType.getPackageClass().getPackage());
    }

    public JStatement buildFieldSet(ASTType userType, TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        return buildFieldSet(userType, fieldInjectionPoint.getField(), fieldInjectionPoint.getRootContainingType(), new TypedExpression(fieldInjectionPoint.getContainingType(), variable), expression);
    }

    public JStatement buildFieldSet(ASTType userType, ASTField field, ASTType containingRootType, TypedExpression containingExpression, TypedExpression expression) {
        ModifiedInvocationBuilder injectionBuilder;
        boolean cast = false;
        if(field.isFinal()){
            injectionBuilder = invocationBuilderStrategy.getInjectionBuilder(ASTAccessModifier.PRIVATE);
        }
        else{
            cast = isHiddenField(containingRootType, containingExpression.getType(), field.getName());
            injectionBuilder = getInjectionBuilder(userType, containingExpression.getType(), field.getAccessModifier());
        }
        return injectionBuilder.buildFieldSet(cast, field, expression, containingExpression);
    }

    private boolean isHiddenField(ASTType rootType, ASTType holdingType, String name){
        for(ASTType iterType = rootType; !iterType.equals(holdingType); iterType = iterType.getSuperClass()){
            for(ASTField field : iterType.getFields()){
                if(field.getName().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public JExpression buildFieldGet(ASTType userType, ASTField field, ASTType rootType, TypedExpression targetExpression) {
        ModifiedInvocationBuilder injectionBuilder = getInjectionBuilder(userType, targetExpression.getType(), field.getAccessModifier());

        boolean cast = isHiddenField(rootType, targetExpression.getType(), field.getName());

        return injectionBuilder.buildFieldGet(cast, field, targetExpression);
    }

    public JExpression buildConstructorCall(ASTType userType, ASTConstructor constructor, ASTType type, List<JExpression> parameters) {
        ModifiedInvocationBuilder injectionBuilder = getInjectionBuilder(userType, type, constructor.getAccessModifier());

        return injectionBuilder.buildConstructorCall(constructor, type, parameters);
    }

    private ModifiedInvocationBuilder getInjectionBuilder(ASTType user, ASTType target, ASTAccessModifier modifier) {

        if(modifier.equals(ASTAccessModifier.PROTECTED) && (target.inherits(user) || user.getPackageClass().getPackage().equals(target.getPackageClass().getPackage()))){
            return invocationBuilderStrategy.getInjectionBuilder(ASTAccessModifier.PUBLIC);
        }
        if(modifier.equals(ASTAccessModifier.PACKAGE_PRIVATE) && user.getPackageClass().getPackage().equals(target.getPackageClass().getPackage())){
            return invocationBuilderStrategy.getInjectionBuilder(ASTAccessModifier.PUBLIC);
        }
        if(user.getPackageClass().equals(target.getPackageClass())){
            return invocationBuilderStrategy.getInjectionBuilder(ASTAccessModifier.PUBLIC);
        }

        return invocationBuilderStrategy.getInjectionBuilder(modifier);
    }
}
