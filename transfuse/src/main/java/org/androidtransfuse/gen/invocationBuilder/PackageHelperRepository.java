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
package org.androidtransfuse.gen.invocationBuilder;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.PackageClass;

import javax.inject.Singleton;
import java.util.*;

/**
 * @author John Ericksen
 */
@Singleton
public class PackageHelperRepository {

    private static final String PRE_METHOD = "access";
    private static final String PACKAGE_HELPER_NAME = "Transfuse$PackageHelper";

    private final Map<PackageClass, PackageHelperDescriptor> packageHelpers = new HashMap<PackageClass, PackageHelperDescriptor>();

    public synchronized ProtectedAccessorMethod getConstructorCall(ConstructorInjectionPoint constructorInjectionPoint) {

        PackageClass containedPackageClass = constructorInjectionPoint.getContainingType().getPackageClass();
        PackageHelperDescriptor helperClass = getPackageHelper(containedPackageClass);

        if (!helperClass.getConstructorMapping().containsKey(constructorInjectionPoint)) {
            String helperMethod = PRE_METHOD + containedPackageClass.getClassName() + "$INIT";
            helperClass.getConstructorMapping().put(constructorInjectionPoint, helperMethod);
        }

        return new ProtectedAccessorMethod(helperClass.getName(), helperClass.getConstructorMapping().get(constructorInjectionPoint));
    }

    public synchronized ProtectedAccessorMethod getMethodCall(ASTType returnType, ASTType targetExpressionsType, String methodName, List<ASTType> argTypes) {

        List<ASTType> paramTypes = new ArrayList<ASTType>();
        for (ASTType argType : argTypes) {
            paramTypes.add(argType);
        }

        MethodCall methodSignature = new MethodCall(targetExpressionsType, returnType, methodName, paramTypes);

        PackageClass containedPackageClass = targetExpressionsType.getPackageClass();
        PackageHelperDescriptor helperClass = getPackageHelper(containedPackageClass);

        if (!helperClass.getMethodCallMapping().containsKey(methodSignature)) {
            String accessorMethod = PRE_METHOD + containedPackageClass.getClassName() + "$M$" + methodName;
            helperClass.getMethodCallMapping().put(methodSignature, accessorMethod);
        }

        return new ProtectedAccessorMethod(helperClass.getName(), helperClass.getMethodCallMapping().get(methodSignature));
    }

    public synchronized ProtectedAccessorMethod getFieldGetter(ASTType returnType, ASTType variableType, String name) {
        FieldGetter fieldGetter = new FieldGetter(returnType, variableType, name);

        PackageClass containedPackageClass = variableType.getPackageClass();
        PackageHelperDescriptor helperClass = getPackageHelper(containedPackageClass);

        if (!helperClass.getFieldGetMapping().containsKey(fieldGetter)) {
            String accessorMethod = PRE_METHOD + containedPackageClass.getClassName() + "$FG$" + name;
            helperClass.getFieldGetMapping().put(fieldGetter, accessorMethod);
        }

        return new ProtectedAccessorMethod(helperClass.getName(), helperClass.getFieldGetMapping().get(fieldGetter));
    }


    public synchronized ProtectedAccessorMethod getFieldSetter(FieldInjectionPoint fieldInjectionPoint) {

        PackageClass containedPackageClass = fieldInjectionPoint.getContainingType().getPackageClass();
        PackageHelperDescriptor helperClass = getPackageHelper(containedPackageClass);

        if (!helperClass.getFieldSetMapping().containsKey(fieldInjectionPoint)) {

            //todo: do we need to replace . with$?
            String accessorMethod = PRE_METHOD + containedPackageClass.getClassName().replace('.', '$') + "$FS$" + fieldInjectionPoint.getName();
            helperClass.getFieldSetMapping().put(fieldInjectionPoint, accessorMethod);
        }

        return new ProtectedAccessorMethod(helperClass.getName(), helperClass.getFieldSetMapping().get(fieldInjectionPoint));
    }

    protected Collection<PackageHelperDescriptor> getPackageHelpers() {
        return packageHelpers.values();
    }

    private PackageHelperDescriptor getPackageHelper(PackageClass pkg) {
        PackageClass helperPackageClass = pkg.replaceName(PACKAGE_HELPER_NAME);
        if (!packageHelpers.containsKey(helperPackageClass)) {
            packageHelpers.put(helperPackageClass, new PackageHelperDescriptor(helperPackageClass));
        }
        return packageHelpers.get(helperPackageClass);
    }
}
