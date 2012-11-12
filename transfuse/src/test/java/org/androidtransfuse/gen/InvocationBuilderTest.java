package org.androidtransfuse.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.invocationBuilder.PrivateInjectionBuilder;
import org.androidtransfuse.gen.invocationBuilder.ProtectedInjectionBuilder;
import org.androidtransfuse.gen.invocationBuilder.PublicInjectionBuilder;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.InstanceProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author John Ericksen
 */
public class InvocationBuilderTest {

    private PublicInjectionBuilder publicInjectionBuilder;
    private ProtectedInjectionBuilder protectedInjectionBuilder;
    private PrivateInjectionBuilder privateInjectionBuilder;
    private InvocationBuilder invocationBuilder;

    @Before
    public void setup() {

        publicInjectionBuilder = Mockito.mock(PublicInjectionBuilder.class);
        protectedInjectionBuilder = Mockito.mock(ProtectedInjectionBuilder.class);
        privateInjectionBuilder = Mockito.mock(PrivateInjectionBuilder.class);

        invocationBuilder = new InvocationBuilder(
                new InstanceProvider<PublicInjectionBuilder>(publicInjectionBuilder),
                new InstanceProvider<ProtectedInjectionBuilder>(protectedInjectionBuilder),
                new InstanceProvider<PrivateInjectionBuilder>(privateInjectionBuilder)
        );
    }

    @Test
    public void testFieldGet() {
        ASTType returnType = Mockito.mock(ASTType.class);
        ASTType variableType = Mockito.mock(ASTType.class);
        JExpression variable = Mockito.mock(JExpression.class);
        String name = "test";

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PUBLIC);
        Mockito.verify(publicInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, returnType, variable, variableType);

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PROTECTED);
        Mockito.verify(protectedInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, returnType, variable, variableType);

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PACKAGE_PRIVATE);
        Mockito.verify(protectedInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, returnType, variable, variableType);

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PRIVATE);
        Mockito.verify(privateInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
    }

    @Test
    public void testFieldSet() throws ClassNotFoundException, JClassAlreadyExistsException {
        ASTType variableType = Mockito.mock(ASTType.class);
        JExpression variable = Mockito.mock(JExpression.class);
        TypedExpression expression = Mockito.mock(TypedExpression.class);
        InjectionNode injectionNode = Mockito.mock(InjectionNode.class);
        String name = "test";

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PUBLIC, name, injectionNode);
        invocationBuilder.buildFieldSet(expression, fieldInjectionPoint, variable);
        Mockito.verify(publicInjectionBuilder).buildFieldSet(expression, fieldInjectionPoint, variable);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, variable, variableType);

        FieldInjectionPoint protectedFieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PROTECTED, name, injectionNode);
        invocationBuilder.buildFieldSet(expression, protectedFieldInjectionPoint, variable);
        Mockito.verify(protectedInjectionBuilder).buildFieldSet(expression, protectedFieldInjectionPoint, variable);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, variable, variableType);

        FieldInjectionPoint pacakgePrivateFieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PACKAGE_PRIVATE, name, injectionNode);
        invocationBuilder.buildFieldSet(expression, pacakgePrivateFieldInjectionPoint, variable);
        Mockito.verify(protectedInjectionBuilder).buildFieldSet(expression, pacakgePrivateFieldInjectionPoint, variable);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, variable, variableType);

        FieldInjectionPoint privateFieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PRIVATE, name, injectionNode);
        invocationBuilder.buildFieldSet(expression, privateFieldInjectionPoint, variable);
        Mockito.verify(privateInjectionBuilder).buildFieldSet(expression, privateFieldInjectionPoint, variable);
    }

    @Test
    public void testConstructorCall() throws ClassNotFoundException, JClassAlreadyExistsException {
        JType type = Mockito.mock(JType.class);
        ASTType containingType = Mockito.mock(ASTType.class);
        JExpression variable = Mockito.mock(JExpression.class);
        Iterable parmeters = Mockito.mock(Iterable.class);

        ConstructorInjectionPoint publicConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC, containingType);
        invocationBuilder.buildConstructorCall(publicConstructorInjectionPoint, parmeters, type);
        Mockito.verify(publicInjectionBuilder).buildConstructorCall(publicConstructorInjectionPoint, parmeters, type);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parmeters, variable);

        ConstructorInjectionPoint protectedConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PROTECTED, containingType);
        invocationBuilder.buildConstructorCall(protectedConstructorInjectionPoint, parmeters, type);
        Mockito.verify(protectedInjectionBuilder).buildConstructorCall(protectedConstructorInjectionPoint, parmeters, type);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parmeters, variable);

        ConstructorInjectionPoint pacakgePrivateConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PACKAGE_PRIVATE, containingType);
        invocationBuilder.buildConstructorCall(pacakgePrivateConstructorInjectionPoint, parmeters, type);
        Mockito.verify(protectedInjectionBuilder).buildConstructorCall(pacakgePrivateConstructorInjectionPoint, parmeters, type);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parmeters, variable);

        ConstructorInjectionPoint privateConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PRIVATE, containingType);
        invocationBuilder.buildConstructorCall(privateConstructorInjectionPoint, parmeters, type);
        Mockito.verify(privateInjectionBuilder).buildConstructorCall(privateConstructorInjectionPoint, parmeters, type);
        Mockito.reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parmeters, variable);
    }
}