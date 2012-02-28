package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.MethodSignature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents the AOP proxy Method Interceptors.  This aspect is applied to the InjectionNode and used during the
 * object instantiation generation.
 *
 * @author John Ericksen
 */
public class AOPProxyAspect {

    private Map<MethodSignature, Set<InjectionNode>> methodInterceptors = new HashMap<MethodSignature, Set<InjectionNode>>();

    public Map<ASTMethod, Set<InjectionNode>> getMethodInterceptors() {
        Map<ASTMethod, Set<InjectionNode>> unboxedMethodInterceptors = new HashMap<ASTMethod, Set<InjectionNode>>();

        for (Map.Entry<MethodSignature, Set<InjectionNode>> methodSignatureSetEntry : methodInterceptors.entrySet()) {
            unboxedMethodInterceptors.put(methodSignatureSetEntry.getKey().getMethod(), methodSignatureSetEntry.getValue());
        }

        return unboxedMethodInterceptors;
    }

    public void addInterceptor(ASTMethod astMethod, InjectionNode interceptorInjectionNode) {
        MethodSignature methodSignature = new MethodSignature(astMethod);
        if (!methodInterceptors.containsKey(methodSignature)) {
            methodInterceptors.put(methodSignature, new HashSet<InjectionNode>());
        }

        methodInterceptors.get(methodSignature).add(interceptorInjectionNode);
    }
}
