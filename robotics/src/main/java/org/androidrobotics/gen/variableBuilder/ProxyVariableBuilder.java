package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JVar;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProxyVariableBuilder {

    private UniqueVariableNamer variableNamer;

    @Inject
    public ProxyVariableBuilder(UniqueVariableNamer variableNamer) {
        this.variableNamer = variableNamer;
    }

    public JExpression buildProxyInstance(InjectionBuilderContext context, InjectionNode injectionNode, ProxyDescriptor proxyDescriptor) {

        JVar proxyVariable = context.getBlock().decl(proxyDescriptor.getClassDefinition(), variableNamer.generateName(injectionNode.getClassName()));

        context.getBlock().assign(proxyVariable, JExpr._new(proxyDescriptor.getClassDefinition()));

        return proxyVariable;
    }
}
