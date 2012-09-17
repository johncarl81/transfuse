package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;

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

    public JExpression buildProxyInstance(InjectionBuilderContext context, InjectionNode injectionNode, JDefinedClass proxyDescriptor) {
        return context.getBlock().decl(proxyDescriptor, variableNamer.generateName(injectionNode),
                JExpr._new(proxyDescriptor));
    }
}
