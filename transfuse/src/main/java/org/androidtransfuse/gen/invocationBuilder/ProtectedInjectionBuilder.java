package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTVoidType;
import org.androidtransfuse.analysis.adapter.MethodSignature;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author John Ericksen
 */
//todo: move away from singleton
@Singleton
public class ProtectedInjectionBuilder implements ModifierInjectionBuilder {

    private static final String PRE_METHOD = "access";
    private static final String PACKAGE_HELPER_NAME = "Transfuse$PackageHelper";

    private final JCodeModel codeModel;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;
    private final Map<FieldInjectionPoint, ProtectedAccessorMethod> fieldSetterMapping = new HashMap<FieldInjectionPoint, ProtectedAccessorMethod>();
    private final Map<ConstructorInjectionPoint, ProtectedAccessorMethod> constructorMapping = new HashMap<ConstructorInjectionPoint, ProtectedAccessorMethod>();
    private final Map<TypeMethodSignature, ProtectedAccessorMethod> methodMapping = new HashMap<TypeMethodSignature, ProtectedAccessorMethod>();
    private final Map<FieldGetter, ProtectedAccessorMethod> fieldGetterMapping = new HashMap<FieldGetter, ProtectedAccessorMethod>();
    private final Map<PackageClass, JDefinedClass> packageHelpers = new HashMap<PackageClass, JDefinedClass>();
    private final TypeInvocationHelper invocationHelper;

    @Inject
    public ProtectedInjectionBuilder(JCodeModel codeModel, UniqueVariableNamer namer, ClassGenerationUtil generationUtil, TypeInvocationHelper invocationHelper) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.generationUtil = generationUtil;
        this.invocationHelper = invocationHelper;
    }

    @Override
    public JExpression buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, Iterable<JExpression> parameters, JType type) {
        ProtectedAccessorMethod accessorMethod = getConstructorCall(constructorInjectionPoint);

        JInvocation invocation = accessorMethod.invoke();

        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }

        return invocation;
    }

    private ProtectedAccessorMethod getConstructorCall(ConstructorInjectionPoint constructorInjectionPoint) {
        if (!constructorMapping.containsKey(constructorInjectionPoint)) {
            constructorMapping.put(constructorInjectionPoint, buildConstructorCall(constructorInjectionPoint));
        }
        return constructorMapping.get(constructorInjectionPoint);
    }

    private ProtectedAccessorMethod buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint) {
        PackageClass containedPackageClass = constructorInjectionPoint.getContainingType().getPackageClass();
        JDefinedClass helperClass = getPackageHelper(containedPackageClass);

        JClass returnTypeRef = codeModel.ref(constructorInjectionPoint.getContainingType().getName());
        //get, ClassName, FG, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, returnTypeRef,
                PRE_METHOD + containedPackageClass.getClassName() + "$INIT");

        JInvocation constructorInvocation = JExpr._new(returnTypeRef);
        for (InjectionNode injectionNode : constructorInjectionPoint.getInjectionNodes()) {
            JClass paramRef = codeModel.ref(injectionNode.getASTType().getName());
            JVar param = accessorMethod.param(paramRef, namer.generateName(paramRef));
            constructorInvocation.arg(param);
        }

        accessorMethod.body()._return(constructorInvocation);

        return new ProtectedAccessorMethod(helperClass, accessorMethod);
    }

    @Override
    public JInvocation buildMethodCall(ASTType returnType, String methodName, Iterable<JExpression> parameters, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {

        ProtectedAccessorMethod accessorMethod = getMethodCall(returnType, targetExpressionType, methodName, injectionNodeType);

        JInvocation invocation = accessorMethod.invoke().arg(targetExpression);

        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }

        return invocation;
    }

    private <T> ProtectedAccessorMethod getMethodCall(ASTType returnType, ASTType targetExpressionsType, String methodName, List<ASTType> argTypes) {

        List<ASTType> paramTypes = new ArrayList<ASTType>();
        for (ASTType argType : argTypes) {
            paramTypes.add(argType);
        }

        TypeMethodSignature methodSignature = new TypeMethodSignature(targetExpressionsType, new MethodSignature(returnType, methodName, paramTypes));

        if (!methodMapping.containsKey(methodSignature)) {
            methodMapping.put(methodSignature, buildMethodCall(returnType, targetExpressionsType, methodName, argTypes));
        }

        return methodMapping.get(methodSignature);
    }

    private static final class TypeMethodSignature {
        private final ASTType type;
        private final MethodSignature methodSignature;

        private TypeMethodSignature(ASTType type, MethodSignature methodSignature) {
            this.type = type;
            this.methodSignature = methodSignature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TypeMethodSignature)) {
                return false;
            }
            TypeMethodSignature that = (TypeMethodSignature) o;
            return new EqualsBuilder().append(type, that.type).append(methodSignature, that.methodSignature).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(type).append(methodSignature).hashCode();
        }
    }

    private <T> ProtectedAccessorMethod buildMethodCall(ASTType returnType, ASTType targetExpressionsType, String methodName, List<ASTType> argTypes) {
        PackageClass containedPackageClass = targetExpressionsType.getPackageClass();
        JDefinedClass helperClass = getPackageHelper(containedPackageClass);

        JClass returnTypeRef = codeModel.ref(returnType.getName());
        //get, ClassName, FG, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, returnTypeRef,
                "get" + containedPackageClass.getClassName() + "$M$" + methodName);

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

        return new ProtectedAccessorMethod(helperClass, accessorMethod);
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {
        ProtectedAccessorMethod accessorMethod = getFieldGetter(returnType, variableType, name);

        return accessorMethod.invoke().arg(variable);
    }

    private ProtectedAccessorMethod getFieldGetter(ASTType returnType, ASTType variableType, String name) {
        FieldGetter fieldGetter = new FieldGetter(variableType, name);
        if (!fieldGetterMapping.containsKey(fieldGetter)) {
            fieldGetterMapping.put(fieldGetter, buildFieldGetter(returnType, variableType, name));
        }
        return fieldGetterMapping.get(fieldGetter);
    }

    private ProtectedAccessorMethod buildFieldGetter(ASTType returnType, ASTType variableType, String name) {
        PackageClass containedPackageClass = variableType.getPackageClass();
        JDefinedClass helperClass = getPackageHelper(containedPackageClass);

        JClass returnTypeRef = codeModel.ref(returnType.getName());
        //get, ClassName, FG, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, returnTypeRef,
                "get" + containedPackageClass.getClassName() + "$FG$" + name);

        JClass variableTypeRef = codeModel.ref(variableType.getName());
        JVar variableParam = accessorMethod.param(variableTypeRef, namer.generateName(variableTypeRef));
        JBlock body = accessorMethod.body();

        body._return(variableParam.ref(name));

        return new ProtectedAccessorMethod(helperClass, accessorMethod);
    }

    private static final class FieldGetter {
        private final ASTType variableType;
        private final String name;

        private FieldGetter(ASTType variableType, String name) {
            this.variableType = variableType;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FieldGetter)) {
                return false;
            }
            FieldGetter that = (FieldGetter) o;
            return new EqualsBuilder().append(name, that.name).append(variableType, that.variableType).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(name).append(variableType).hashCode();
        }
    }

    @Override
    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {

        //build field setter
        ProtectedAccessorMethod accessorMethod = getFieldSetter(fieldInjectionPoint);

        return accessorMethod.invoke()
                .arg(variable)
                .arg(invocationHelper.coerceType(fieldInjectionPoint.getInjectionNode().getASTType(), expression));
    }

    private ProtectedAccessorMethod getFieldSetter(FieldInjectionPoint fieldInjectionPoint) {
        if (!fieldSetterMapping.containsKey(fieldInjectionPoint)) {
            fieldSetterMapping.put(fieldInjectionPoint, buildFieldSet(fieldInjectionPoint));
        }
        return fieldSetterMapping.get(fieldInjectionPoint);
    }

    private ProtectedAccessorMethod buildFieldSet(FieldInjectionPoint fieldInjectionPoint) {

        PackageClass containedPackageClass = fieldInjectionPoint.getContainingType().getPackageClass();
        JDefinedClass helperClass = getPackageHelper(containedPackageClass);

        //get, ClassName, FS, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, codeModel.VOID,
                "get" + containedPackageClass.getClassName().replace('.', '$') + "$FS$" + fieldInjectionPoint.getName());

        JClass containerType = codeModel.ref(fieldInjectionPoint.getContainingType().getName());
        JVar containerParam = accessorMethod.param(containerType, namer.generateName(containerType));

        JClass inputType = codeModel.ref(fieldInjectionPoint.getInjectionNode().getASTType().getName());
        JVar inputParam = accessorMethod.param(inputType, namer.generateName(inputType));

        JBlock body = accessorMethod.body();

        body.assign(containerParam.ref(fieldInjectionPoint.getName()), inputParam);

        return new ProtectedAccessorMethod(helperClass, accessorMethod);
    }

    private JDefinedClass getPackageHelper(PackageClass pkg) {
        PackageClass helperPackageClass = pkg.replaceName(PACKAGE_HELPER_NAME);
        if (!packageHelpers.containsKey(helperPackageClass)) {
            packageHelpers.put(helperPackageClass, buildPackageHelper(helperPackageClass));
        }
        return packageHelpers.get(helperPackageClass);
    }

    private JDefinedClass buildPackageHelper(PackageClass helperClassName) {
        try {
            return generationUtil.defineClass(helperClassName);

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Unable to create helper", e);
        }
    }
}
