package org.androidtransfuse.gen;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.invocationBuilder.ModifierInjectionBuilder;
import org.androidtransfuse.gen.invocationBuilder.PrivateInjectionBuilder;
import org.androidtransfuse.gen.invocationBuilder.ProtectedInjectionBuilder;
import org.androidtransfuse.gen.invocationBuilder.PublicInjectionBuilder;
import org.androidtransfuse.model.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Builds the invocations of constructors, methods and field get/set.
 *
 * @author John Ericksen
 */
public class InvocationBuilder {

    private final Provider<PublicInjectionBuilder> publicProvider;
    private final Provider<ProtectedInjectionBuilder> protectedProvider;
    private final Provider<PrivateInjectionBuilder> privateProvider;

    @Inject
    public InvocationBuilder(Provider<PublicInjectionBuilder> publicProvider,
                             Provider<ProtectedInjectionBuilder> protectedProvider,
                             Provider<PrivateInjectionBuilder> privateProvider) {
        this.publicProvider = publicProvider;
        this.protectedProvider = protectedProvider;
        this.privateProvider = privateProvider;
    }

    private ModifierInjectionBuilder getInjectionBuilder(ASTAccessModifier modifier) {
        switch (modifier) {
            case PUBLIC:
                return publicProvider.get();
            case PACKAGE_PRIVATE:
            case PROTECTED:
                return protectedProvider.get();
            default:
                return privateProvider.get();
        }
    }

    public JInvocation buildMethodCall(ASTType returnType, List<ASTParameter> callingParameters, Map<ASTParameter, TypedExpression> parameters, ASTType targetExpressionType, JExpression targetExpression, ASTMethod methodToCall) {
        List<ASTParameter> matchedParameters = matchMethodArguments(callingParameters, methodToCall);
        List<ASTType> parameterTypes = new ArrayList<ASTType>();
        List<JExpression> expressionParameters = new ArrayList<JExpression>();

        for (ASTParameter matchedParameter : matchedParameters) {
            parameterTypes.add(matchedParameter.getASTType());
            expressionParameters.add(parameters.get(matchedParameter).getExpression());
        }

        return buildMethodCall(methodToCall.getAccessModifier(), returnType, methodToCall.getName(), expressionParameters, parameterTypes, targetExpressionType, targetExpression);
    }

    public JInvocation buildMethodCall(ASTType returnType, MethodInjectionPoint methodInjectionPoint, Iterable<JExpression> parameters, JExpression variable) {
        return buildMethodCall(methodInjectionPoint.getAccessModifier(), returnType, methodInjectionPoint.getName(), parameters, pullASTTypes(methodInjectionPoint.getInjectionNodes()), methodInjectionPoint.getContainingType(), variable);
    }

    public JInvocation buildMethodCall(ASTAccessModifier accessModifier, ASTType returnType, String name, Iterable<JExpression> parameters, List<ASTType> parameterTypes, ASTType targetExpressionType, JExpression targetExpression) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(accessModifier);

        return injectionBuilder.buildMethodCall(returnType, name, parameters, parameterTypes, targetExpressionType, targetExpression);
    }

    private List<ASTParameter> matchMethodArguments(List<ASTParameter> parametersToMatch, ASTMethod methodToCall) {
        List<ASTParameter> arguments = new ArrayList<ASTParameter>();

        List<ASTParameter> overrideParameters = new ArrayList<ASTParameter>(parametersToMatch);

        for (ASTParameter callParameter : methodToCall.getParameters()) {
            Iterator<ASTParameter> overrideParameterIterator = overrideParameters.iterator();

            while (overrideParameterIterator.hasNext()) {
                ASTParameter overrideParameter = overrideParameterIterator.next();
                if (overrideParameter.getASTType().equals(callParameter.getASTType())) {
                    arguments.add(overrideParameter);
                    overrideParameterIterator.remove();
                    break;
                }
            }
        }

        return arguments;
    }

    private List<ASTType> pullASTTypes(List<InjectionNode> injectionNodes) {
        List<ASTType> astTypes = new ArrayList<ASTType>();

        for (InjectionNode injectionNode : injectionNodes) {
            astTypes.add(injectionNode.getASTType());
        }

        return astTypes;
    }

    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(fieldInjectionPoint.getAccessModifier());
        return injectionBuilder.buildFieldSet(expression, fieldInjectionPoint, variable);
    }

    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name, ASTAccessModifier accessModifier) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(accessModifier);
        return injectionBuilder.buildFieldGet(returnType, variableType, variable, name);
    }

    public JExpression buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, Iterable<JExpression> parameters, JType type) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(constructorInjectionPoint.getAccessModifier());
        return injectionBuilder.buildConstructorCall(constructorInjectionPoint, parameters, type);
    }
}
