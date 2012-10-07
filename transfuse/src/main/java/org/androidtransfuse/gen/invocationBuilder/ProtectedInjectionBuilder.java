package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTVoidType;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.androidtransfuse.gen.GeneratedClassAnnotator.annotateGeneratedClass;

/**
 * @author John Ericksen
 */
@Singleton
public class ProtectedInjectionBuilder implements ModifierInjectionBuilder {

    private static final String PACKAGE_HELPER_NAME = "Transfuse$PackageHelper";

    private final JCodeModel codeModel;
    private final PrivateInjectionBuilder delegate;
    private final UniqueVariableNamer namer;
    private final Map<FieldInjectionPoint, ProtectedAccessorMethod> fieldSetterMapping = new HashMap<FieldInjectionPoint, ProtectedAccessorMethod>();
    private final Map<FieldGetter, ProtectedAccessorMethod> fieldGetterMapping = new HashMap<FieldGetter, ProtectedAccessorMethod>();
    private final Map<PackageClass, JDefinedClass> packageHelpers = new HashMap<PackageClass, JDefinedClass>();

    @Inject
    public ProtectedInjectionBuilder(JCodeModel codeModel, PrivateInjectionBuilder delegate, UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.delegate = delegate;
        this.namer = namer;
    }

    @Override
    public JExpression buildConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) {
        return delegate.buildConstructorCall(expressionMap, constructorInjectionPoint, type);
    }

    @Override
    public <T> JInvocation buildMethodCall(ASTType returnType, Map<T, TypedExpression> expressionMap, String methodName, List<T> injectionNodes, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {
        List<TypedExpression> paramExpressions = new ArrayList<TypedExpression>();

        for (T injectionNode : injectionNodes) {
            paramExpressions.add(expressionMap.get(injectionNode));
        }

        ProtectedAccessorMethod accessorMethod = getMethodCall(returnType, targetExpressionType, methodName, paramExpressions);

        JInvocation invocation = accessorMethod.invoke().arg(targetExpression);

        for(TypedExpression expression : paramExpressions){
            invocation.arg(expression.getExpression());
        }

        return invocation;
    }

    public <T> ProtectedAccessorMethod getMethodCall(ASTType returnType, ASTType targetExpressionsType, String methodName, List<TypedExpression> argExpressions) {
        PackageClass containedPackageClass = new PackageClass(targetExpressionsType.getName());
        JDefinedClass helperClass = getPackageHelper(containedPackageClass);

        JClass returnTypeRef = codeModel.ref(returnType.getName());
        //get, ClassName, FG, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, returnTypeRef,
                "get" + containedPackageClass.getClassName() + "$M$" + methodName);

        JClass targetRef = codeModel.ref(targetExpressionsType.getName());
        JVar targetParam = accessorMethod.param(targetRef, namer.generateName(targetRef));
        JInvocation invocation = targetParam.invoke(methodName);

        for (TypedExpression expression : argExpressions) {
            JClass ref = codeModel.ref(expression.getType().getName());
            JVar param = accessorMethod.param(ref, namer.generateName(ref));
            invocation.arg(param);
        }

        if(returnType.equals(ASTVoidType.VOID)){
            accessorMethod.body().add(invocation);
        }
        else{
            accessorMethod.body()._return(invocation);
        }

        return new ProtectedAccessorMethod(helperClass, accessorMethod);
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {
        ProtectedAccessorMethod accessorMethod = getFieldGetter(returnType, variableType, name);

        return accessorMethod.invoke().arg(variable);
    }

    private ProtectedAccessorMethod getFieldGetter(ASTType returnType, ASTType variableType, String name){
        FieldGetter fieldGetter = new FieldGetter(variableType, name);
        if(!fieldGetterMapping.containsKey(fieldGetter)){
            fieldGetterMapping.put(fieldGetter, buildFieldGetter(returnType, variableType, name));
        }
        return fieldGetterMapping.get(fieldGetter);
    }

    private ProtectedAccessorMethod buildFieldGetter(ASTType returnType, ASTType variableType, String name) {
        PackageClass containedPackageClass = new PackageClass(variableType.getName());
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

    private final class FieldGetter{
        private ASTType variableType;
        private String name;

        private FieldGetter(ASTType variableType, String name) {
            this.variableType = variableType;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FieldGetter)) return false;

            FieldGetter that = (FieldGetter) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (variableType != null ? !variableType.equals(that.variableType) : that.variableType != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = variableType != null ? variableType.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

    @Override
    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {

        //build field setter
        ProtectedAccessorMethod accessorMethod = getFieldSetter(fieldInjectionPoint);

        return accessorMethod.invoke()
                .arg(variable)
                .arg(expression.getExpression());
    }

    private ProtectedAccessorMethod getFieldSetter(FieldInjectionPoint fieldInjectionPoint) {
        if(!fieldSetterMapping.containsKey(fieldInjectionPoint)){
            fieldSetterMapping.put(fieldInjectionPoint, buildFieldSet(fieldInjectionPoint));
        }
        return fieldSetterMapping.get(fieldInjectionPoint);
    }

    private ProtectedAccessorMethod buildFieldSet(FieldInjectionPoint fieldInjectionPoint) {

        PackageClass containedPackageClass = new PackageClass(fieldInjectionPoint.getContainingType().getName());
        JDefinedClass helperClass = getPackageHelper(containedPackageClass);

        //get, ClassName, FS, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, codeModel.VOID,
                "get" + containedPackageClass.getClassName() + "$FS$" + fieldInjectionPoint.getName());

        JClass containerType = codeModel.ref(fieldInjectionPoint.getContainingType().getName());
        JVar containerParam = accessorMethod.param(containerType, namer.generateName(containerType));

        JClass inputType = codeModel.ref(fieldInjectionPoint.getInjectionNode().getASTType().getName());
        JVar inputParam = accessorMethod.param(inputType, namer.generateName(inputType));

        JBlock body = accessorMethod.body();

        body.assign(containerParam.ref(fieldInjectionPoint.getName()), inputParam);

        //perform set


        return new ProtectedAccessorMethod(helperClass, accessorMethod);
    }

    private JDefinedClass getPackageHelper(PackageClass pkg){
        PackageClass helperPackageClass = pkg.replaceName(PACKAGE_HELPER_NAME);
        if(!packageHelpers.containsKey(helperPackageClass)){
            System.out.println("Building: " + helperPackageClass);
            packageHelpers.put(helperPackageClass, buildPackageHelper(helperPackageClass));
        }
        return packageHelpers.get(helperPackageClass);
    }

    private JDefinedClass buildPackageHelper(PackageClass helperClassName) {
        try{
            JDefinedClass helperClass = codeModel._class(JMod.PUBLIC, helperClassName.getFullyQualifiedName(), ClassType.CLASS);

            annotateGeneratedClass(helperClass);

            return helperClass;

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Unable to create helper", e);
        }
    }
}
