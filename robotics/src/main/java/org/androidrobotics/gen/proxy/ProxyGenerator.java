package org.androidrobotics.gen.proxy;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTParameter;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ProxyGenerator {

    private static final String VOID_TYPE_NAME = "void";

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;

    @Inject
    public ProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
    }

    public ProxyDescriptor generateProxy(InjectionNode injectionNode, DelegateInstantiationGeneratorStrategy delegateInstantiationGenerator) {

        JDefinedClass definedClass;
        try {

            definedClass = codeModel._class(JMod.PUBLIC, injectionNode.getClassName() + "_Proxy", ClassType.CLASS);

            //define delegate
            JFieldVar delegateField = delegateInstantiationGenerator.addDelegateInstantiation(definedClass, injectionNode);

            //implements interfaces
            for (ASTType interfaceType : injectionNode.getProxyInterfaces()) {
                definedClass._implements(codeModel.ref(interfaceType.getName()));

                //implement methods
                for (ASTMethod method : interfaceType.getMethods()) {
                    // public <type> <method_name> ( <parameters...>)
                    JClass returnType = codeModel.ref(method.getReturnType().getName());
                    JMethod methodDeclaration = definedClass.method(JMod.PUBLIC, returnType, method.getName());

                    //define method parameter
                    Map<ASTParameter, JVar> parmaeterMap = new HashMap<ASTParameter, JVar>();
                    for (ASTParameter parameter : method.getParameters()) {
                        parmaeterMap.put(parameter,
                                methodDeclaration.param(codeModel.ref(parameter.getASTType().getName()),
                                        variableNamer.generateName(parameter.getASTType().getName())));
                    }

                    //define method body
                    JBlock body = methodDeclaration.body();

                    //delegate invocation
                    JInvocation invocation = delegateField.invoke(method.getName());

                    for (ASTParameter parameter : method.getParameters()) {
                        invocation.arg(parmaeterMap.get(parameter));
                    }

                    //todo: add AOP here?

                    if (!VOID_TYPE_NAME.equals(method.getReturnType().getName())) {
                        body._return(invocation);
                    } else {
                        body.add(invocation);
                    }
                }
            }
        } catch (JClassAlreadyExistsException e) {
            throw new RoboticsAnalysisException("Error while trying to build new class", e);
        }

        return new ProxyDescriptor(definedClass);
    }
}
