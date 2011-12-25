package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ProxyAspect {

    private Set<ASTType> proxyInterfaces = new HashSet<ASTType>();
    private Map<ASTMethod, Set<ASTType>> methodInterceptors = new HashMap<ASTMethod, Set<ASTType>>();

    public Set<ASTType> getProxyInterfaces() {
        return proxyInterfaces;
    }

    public boolean isProxyRequired() {
        return !(proxyInterfaces.isEmpty() && methodInterceptors.isEmpty());
    }

    public synchronized void addInterceptor(ASTMethod astMethod, ASTType interceptorType) {
        if (!methodInterceptors.containsKey(astMethod)) {
            methodInterceptors.put(astMethod, new HashSet<ASTType>());
        }

        methodInterceptors.get(astMethod).add(interceptorType);
    }
}
