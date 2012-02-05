package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.HashSet;
import java.util.Set;

/**
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
