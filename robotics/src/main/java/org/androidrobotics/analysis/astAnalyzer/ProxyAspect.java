package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.adapter.ASTType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ProxyAspect {

    private boolean proxyRequired;
    private Set<ASTType> proxyInterfaces = new HashSet<ASTType>();

    public ProxyAspect() {
        this.proxyRequired = true;
    }

    public Set<ASTType> getProxyInterfaces() {
        return proxyInterfaces;
    }

    public void setProxyRequired(boolean proxyRequired) {
        this.proxyRequired = proxyRequired;
    }

    public boolean isProxyRequired() {
        return proxyRequired;
    }
}
