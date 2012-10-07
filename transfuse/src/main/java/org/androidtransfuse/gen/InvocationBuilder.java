package org.androidtransfuse.gen;

import com.sun.codemodel.*;
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

    private ModifierInjectionBuilder getInjectionBuilder(ASTAccessModifier modifier){
        switch (modifier){
            case PUBLIC:
                return publicProvider.get();
            case PACKAGE_PRIVATE:
            case PROTECTED:
                return protectedProvider.get();
            default:
                return privateProvider.get();
        }
    }

    public JInvocation buildMethodCall(ASTType returnType, List<ASTParameter> callingParameters, Map<ASTParameter, TypedExpression> parameters, ASTType targetExpressionType, JExpression targetExpression, ASTMethod methodToCall) throws ClassNotFoundException, JClassAlreadyExistsException {
        List<ASTParameter> matchedParameters = matchMethodArguments(callingParameters, methodToCall);
        List<ASTType> parameterTypes = new ArrayList<ASTType>();

        for (ASTParameter matchedParameter : matchedParameters) {
            parameterTypes.add(matchedParameter.getASTType());
        }

        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(methodToCall.getAccessModifier());

        return injectionBuilder.buildMethodCall(returnType, parameters, methodToCall.getName(), matchedParameters, parameterTypes, targetExpressionType, targetExpression);
    }

    public JStatement buildMethodCall(ASTType returnType, Map<InjectionNode, TypedExpression> expressionMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(methodInjectionPoint.getAccessModifier());

        return injectionBuilder.buildMethodCall(returnType, expressionMap, methodInjectionPoint.getName(), methodInjectionPoint.getInjectionNodes(), pullASTTypes(methodInjectionPoint.getInjectionNodes()), methodInjectionPoint.getContainingType(), variable);
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

    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(fieldInjectionPoint.getAccessModifier());

        return injectionBuilder.buildFieldSet(expression, fieldInjectionPoint, variable);
    }

    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name, ASTAccessModifier accessModifier) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(accessModifier);

        return injectionBuilder.buildFieldGet(returnType, variableType, variable, name);
    }

    public JExpression buildConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) throws ClassNotFoundException {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(constructorInjectionPoint.getAccessModifier());

        return injectionBuilder.buildConstructorCall(expressionMap, constructorInjectionPoint, type);
    }
}
