package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.ProxyDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProxyVariableBuilder {

    private final UniqueVariableNamer variableNamer;

    @Inject
    public ProxyVariableBuilder(UniqueVariableNamer variableNamer) {
        this.variableNamer = variableNamer;
    }

    public JExpression buildProxyInstance(InjectionBuilderContext context, InjectionNode injectionNode, ProxyDescriptor proxyDescriptor) {
        return context.getBlock().decl(proxyDescriptor.getClassDefinition(), variableNamer.generateName(injectionNode),
                JExpr._new(proxyDescriptor.getClassDefinition()));
    }
}
