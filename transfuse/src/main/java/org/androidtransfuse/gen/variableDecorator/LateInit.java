package org.androidtransfuse.gen.variableDecorator;

import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public class LateInit {

    private TypedExpression proxyVariable;

    public LateInit(TypedExpression proxyVariable) {
        this.proxyVariable = proxyVariable;
    }

    public TypedExpression getProxyVariable() {
        return proxyVariable;
    }
}

