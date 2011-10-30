package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTParameter;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.FactoryDescriptor;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ProxyGenerator {

    private static final String DELEGATE_NAME = "delegate";

    private JCodeModel codeModel;
    private FactoryGenerator factoryGenerator;

    @Inject
    public ProxyGenerator(JCodeModel codeModel, FactoryGenerator factoryGenerator) {
        this.codeModel = codeModel;
        this.factoryGenerator = factoryGenerator;
    }

    public ProxyDescriptor generateProxy(ASTType interfaceType, InjectionNode delegateNode) throws JClassAlreadyExistsException, ClassNotFoundException {

        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, delegateNode.getClassName() + "Proxy", ClassType.CLASS);
        //implements interface
        definedClass._implements(codeModel.ref(interfaceType.getName()));

        //define delegate from constructor input
        JFieldVar delegateField = definedClass.field(JMod.PRIVATE, codeModel.ref(delegateNode.getClassName()), DELEGATE_NAME);
        FactoryDescriptor factoryDescriptor = factoryGenerator.buildFactory(delegateNode);
        JMethod constructor = definedClass.constructor(JMod.PUBLIC);

        JVar delegateParam = constructor.param(codeModel.ref(delegateNode.getClassName()), "todo");
        constructor.body().assign(delegateField, delegateParam);

        //implement methods
        for (ASTMethod method : interfaceType.getMethods()) {
            // public <type> <method_name> ( <parameters...>)
            JClass returnType = codeModel.ref(method.getReturnType().getName());
            JMethod methodDeclaration = definedClass.method(JMod.PUBLIC, returnType, method.getName());

            //define method parameter
            Map<ASTParameter, JVar> parmaeterMap = new HashMap<ASTParameter, JVar>();
            for (ASTParameter parameter : method.getParameters()) {
                parmaeterMap.put(parameter,
                        methodDeclaration.param(codeModel.ref(parameter.getASTType().getName()), "todo"));
            }

            //define method body
            JBlock body = methodDeclaration.body();

            //delegate invocation
            JInvocation invocation = delegateField.invoke(method.getName());

            for (ASTParameter parameter : method.getParameters()) {
                invocation.arg(parmaeterMap.get(parameter));
            }

            //todo: add AOP here

            body.add(invocation);

            if (!"void".equals(method.getReturnType().getName())) {
                body._return(invocation);
            }

        }

        return new ProxyDescriptor(definedClass);

    }
}
