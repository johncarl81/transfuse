package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.HashSet;
import java.util.Set;

/**
 * Aspect to flag the the given InjectionNode requires a virtual proxy.  This is due to some circular dependency
 * which is broken by this InjectionNode proxy.
 *
 * @author John Ericksen
 */
public class VirtualProxyAspect {

    private Set<ASTType> proxyInterfaces = new HashSet<ASTType>();

    public Set<ASTType> getProxyInterfaces() {
        return proxyInterfaces;
    }

    public boolean isProxyRequired() {
        return !proxyInterfaces.isEmpty();
    }
}
