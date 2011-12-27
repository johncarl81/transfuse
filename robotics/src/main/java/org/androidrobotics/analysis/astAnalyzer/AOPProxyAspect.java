package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.model.InjectionNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class AOPProxyAspect {

    private Map<ASTMethod, Set<InjectionNode>> methodInterceptors = new HashMap<ASTMethod, Set<InjectionNode>>();

    public Map<ASTMethod, Set<InjectionNode>> getMethodInterceptors() {
        return methodInterceptors;
    }

    public void addInterceptor(ASTMethod astMethod, InjectionNode interceptorInjectionNode) {
        if (!methodInterceptors.containsKey(astMethod)) {
            methodInterceptors.put(astMethod, new HashSet<InjectionNode>());
        }

        methodInterceptors.get(astMethod).add(interceptorInjectionNode);
    }
}
