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

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.ASTVoidType;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.AbstractCompletionTransactionWorker;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class PackageHelperGenerator extends AbstractCompletionTransactionWorker<Void, Void> {

    private final PackageHelperRepository repository;
    private final JCodeModel codeModel;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public PackageHelperGenerator(PackageHelperRepository repository,
                                  JCodeModel codeModel,
                                  UniqueVariableNamer namer,
                                  ClassGenerationUtil generationUtil) {
        this.repository = repository;
        this.codeModel = codeModel;
        this.namer = namer;
        this.generationUtil = generationUtil;
    }

    @Override
    public Void innerRun(Void value) {
        for (PackageHelperDescriptor packageHelper : repository.getPackageHelpers()) {
            JDefinedClass packageHelperClass = buildPackageHelper(packageHelper.getName());

            //constructor
            for (Map.Entry<ConstructorInjectionPoint, String> constructorEntry : packageHelper.getConstructorMapping().entrySet()) {
                buildConstructorCall(constructorEntry.getKey(), constructorEntry.getValue(), packageHelperClass);
            }

            //method
            for (Map.Entry<MethodCall, String> methodCallEntry : packageHelper.getMethodCallMapping().entrySet()) {
                ASTType type = methodCallEntry.getKey().getType();
                ASTType returnType = methodCallEntry.getKey().getReturnType();
                String methodName = methodCallEntry.getKey().getMethodName();
                List<ASTType> paramTypes = methodCallEntry.getKey().getParamTypes();

                buildMethodCall(returnType, type, methodName, paramTypes, methodCallEntry.getValue(), packageHelperClass);
            }

            //field get
            for (Map.Entry<FieldGetter, String> fieldGetEntry : packageHelper.getFieldGetMapping().entrySet()) {
                ASTType returnType = fieldGetEntry.getKey().getReturnType();
                ASTType variableType = fieldGetEntry.getKey().getVariableType();
                String name = fieldGetEntry.getKey().getName();

                buildFieldGet(returnType, variableType, name, fieldGetEntry.getValue(), packageHelperClass);
            }

            //field set
            for (Map.Entry<FieldInjectionPoint, String> fieldSetEntry : packageHelper.getFieldSetMapping().entrySet()) {
                buildFieldSet(fieldSetEntry.getKey(), fieldSetEntry.getValue(), packageHelperClass);
            }
        }

        return null;
    }

    private void buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, String accessorMethodName, JDefinedClass helperClass) {
        JClass returnTypeRef = codeModel.ref(constructorInjectionPoint.getContainingType().getName());
        //get, ClassName, FG, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, returnTypeRef, accessorMethodName);

        JInvocation constructorInvocation = JExpr._new(returnTypeRef);
        for (InjectionNode injectionNode : constructorInjectionPoint.getInjectionNodes()) {
            JClass paramRef = codeModel.ref(injectionNode.getASTType().getName());
            JVar param = accessorMethod.param(paramRef, namer.generateName(paramRef));
            constructorInvocation.arg(param);
        }

        accessorMethod.body()._return(constructorInvocation);
    }


    private void buildMethodCall(ASTType returnType, ASTType targetExpressionsType, String methodName, List<ASTType> argTypes, String accessorMethodName, JDefinedClass helperClass) {
        JClass returnTypeRef = codeModel.ref(returnType.getName());
        //get, ClassName, FG, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, returnTypeRef, accessorMethodName);

        JClass targetRef = codeModel.ref(targetExpressionsType.getName());
        JVar targetParam = accessorMethod.param(targetRef, namer.generateName(targetRef));
        JInvocation invocation = targetParam.invoke(methodName);

        for (ASTType argType : argTypes) {
            JClass ref = codeModel.ref(argType.getName());
            JVar param = accessorMethod.param(ref, namer.generateName(ref));
            invocation.arg(param);
        }

        if (returnType.equals(ASTVoidType.VOID)) {
            accessorMethod.body().add(invocation);
        } else {
            accessorMethod.body()._return(invocation);
        }
    }

    private void buildFieldGet(ASTType returnType, ASTType variableType, String name, String accessorMethodName, JDefinedClass helperClass) {
        JClass returnTypeRef = codeModel.ref(returnType.getName());
        //get, ClassName, FG, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, returnTypeRef, accessorMethodName);

        JClass variableTypeRef = codeModel.ref(variableType.getName());
        JVar variableParam = accessorMethod.param(variableTypeRef, namer.generateName(variableTypeRef));
        JBlock body = accessorMethod.body();

        body._return(variableParam.ref(name));
    }

    private void buildFieldSet(FieldInjectionPoint fieldInjectionPoint, String accessorMethodName, JDefinedClass helperClass) {
        //get, ClassName, FS, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, codeModel.VOID, accessorMethodName);

        JClass containerType = codeModel.ref(fieldInjectionPoint.getContainingType().getName());
        JVar containerParam = accessorMethod.param(containerType, namer.generateName(containerType));

        JClass inputType = codeModel.ref(fieldInjectionPoint.getInjectionNode().getASTType().getName());
        JVar inputParam = accessorMethod.param(inputType, namer.generateName(inputType));

        JBlock body = accessorMethod.body();

        body.assign(containerParam.ref(fieldInjectionPoint.getName()), inputParam);
    }

    private JDefinedClass buildPackageHelper(PackageClass helperClassName) {
        try {
            return generationUtil.defineClass(helperClassName);

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Unable to create helper", e);
        }
    }
}
