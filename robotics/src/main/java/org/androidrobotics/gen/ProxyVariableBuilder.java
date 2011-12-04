package org.androidrobotics.gen;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JVar;
import org.androidrobotics.model.ProxyDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProxyVariableBuilder {

    private UniqueVariableNamer variableNamer;
    private JCodeModel codeModel;

    @Inject
    public ProxyVariableBuilder(UniqueVariableNamer variableNamer, JCodeModel codeModel) {
        this.variableNamer = variableNamer;
        this.codeModel = codeModel;
    }

    public JExpression buildProxyInstance(InjectionBuilderContext context, ProxyDescriptor proxyDescriptor) {

        JVar proxyVariable = context.getBlock().decl(proxyDescriptor.getClassDefinition(), variableNamer.generateName(context.getInjectionNode().getClassName()));

        context.getBlock().assign(proxyVariable, JExpr._new(proxyDescriptor.getClassDefinition()));

        return proxyVariable;
    }
}
