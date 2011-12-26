package org.androidrobotics.gen.proxy;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTParameter;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.analysis.astAnalyzer.AOPProxyAspect;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class AOPProxyGenerator {

    private static final String VOID_TYPE_NAME = "void";

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;

    @Inject
    public AOPProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
    }

    public ProxyDescriptor generateProxy(InjectionNode injectionNode) {

        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);
        JDefinedClass definedClass = null;
        try {
            definedClass = codeModel._class(JMod.PUBLIC, injectionNode.getClassName() + "_AOPProxy", ClassType.CLASS);
            //extending injectionNode
            definedClass._extends(codeModel.ref(injectionNode.getClassName()));

            for (Map.Entry<ASTMethod, ASTType> methodInterceptorEntry : aopProxyAspect.getMethodInterceptors().entrySet()) {
                ASTMethod method = methodInterceptorEntry.getKey();
                JClass returnType = null;
                if (method.getReturnType() != null) {
                    returnType = codeModel.ref(method.getReturnType().getName());
                }
                JMethod methodDeclaration = definedClass.method(JMod.PUBLIC, returnType, method.getName());
                JBlock body = methodDeclaration.body();

                //define method parameter
                Map<ASTParameter, JVar> parameterMap = new HashMap<ASTParameter, JVar>();
                for (ASTParameter parameter : method.getParameters()) {
                    parameterMap.put(parameter,
                            methodDeclaration.param(codeModel.ref(parameter.getASTType().getName()),
                                    variableNamer.generateName(parameter.getASTType().getName())));
                }

                JInvocation invocation = JExpr._super().invoke(method.getName());

                for (ASTParameter parameter : method.getParameters()) {
                    invocation.arg(parameterMap.get(parameter));
                }

                //todo:fix void and primitive return
                if (method.getReturnType() == null || VOID_TYPE_NAME.equals(method.getReturnType().getName())) {
                    body.add(invocation);
                } else {
                    body._return(invocation);
                }
            }

        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        ProxyDescriptor proxyDescriptor = new ProxyDescriptor(definedClass);

        return proxyDescriptor;
    }
}
