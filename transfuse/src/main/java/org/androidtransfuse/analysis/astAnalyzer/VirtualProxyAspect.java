package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.TypedExpression;

import java.util.HashSet;
import java.util.Set;

/**
 * Aspect to flag the the given InjectionNode requires a virtual proxy.  This is due to some circular dependency
 * which is broken by a virtual proxy.
 *
 * @author John Ericksen
 */
public class VirtualProxyAspect {

    private Set<ASTType> proxyInterfaces = new HashSet<ASTType>();
    private boolean proxyDefined = false;
    private TypedExpression proxyExpression;

    public Set<ASTType> getProxyInterfaces() {
        return proxyInterfaces;
    }

    public boolean isProxyRequired() {
        return !proxyInterfaces.isEmpty();
    }

    public boolean isProxyDefined() {
        return proxyDefined;
    }

    public void setProxyDefined(boolean proxyDefined) {
        this.proxyDefined = proxyDefined;
    }

    public void setProxyExpression(TypedExpression proxyExpression) {
        this.proxyExpression = proxyExpression;
    }

    public TypedExpression getProxyExpression() {
        return proxyExpression;
    }
}
