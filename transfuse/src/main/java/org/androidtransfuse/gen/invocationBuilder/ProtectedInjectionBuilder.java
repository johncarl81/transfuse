package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.androidtransfuse.gen.GeneratedClassAnnotator.annotateGeneratedClass;

/**
 * @author John Ericksen
 */
@Singleton
public class ProtectedInjectionBuilder implements ModifierInjectionBuilder {

    private static final String PACKAGE_HELPER_NAME = "TransfusePackageHelper";

    private final JCodeModel codeModel;
    private final PrivateInjectionBuilder delegate;
    private final UniqueVariableNamer namer;
    private final Map<FieldInjectionPoint, ProtectedAccessorMethod> fieldMapping = new HashMap<FieldInjectionPoint, ProtectedAccessorMethod>();
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
    public <T> JInvocation buildMethodCall(String returnType, Map<T, TypedExpression> expressionMap, String methodName, List<T> injectionNodes, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {
        return delegate.buildMethodCall(returnType, expressionMap, methodName, injectionNodes, injectionNodeType, targetExpressionType, targetExpression);
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, JClass variableType, JExpression variable, String name) {
        return delegate.buildFieldGet(returnType, variableType, variable, name);
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
        if(!fieldMapping.containsKey(fieldInjectionPoint)){
            fieldMapping.put(fieldInjectionPoint, buildFieldSet(fieldInjectionPoint));
        }
        return fieldMapping.get(fieldInjectionPoint);
    }

    private ProtectedAccessorMethod buildFieldSet(FieldInjectionPoint fieldInjectionPoint) {

        PackageClass containedPackageClass = new PackageClass(fieldInjectionPoint.getContainingType().getName());
        JDefinedClass helperClass = getPackageHelper(containedPackageClass);

        //get, ClassName, F, fieldName
        JMethod accessorMethod = helperClass.method(JMod.PUBLIC | JMod.STATIC, codeModel.VOID ,
                "get" + containedPackageClass.getClassName() + "F" + fieldInjectionPoint.getName());

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
