package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.adapter.ASTType;

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
