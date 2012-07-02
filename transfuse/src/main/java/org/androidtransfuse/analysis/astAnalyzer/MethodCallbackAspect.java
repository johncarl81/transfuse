package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MethodCallbackAspect {

    private Map<String, Set<MethodSignatureWrapper>> methodCallbacks = new HashMap<String, Set<MethodSignatureWrapper>>();

    public void addMethodCallback(String name, ASTMethod method) {
        if (!methodCallbacks.containsKey(name)) {
            methodCallbacks.put(name, new HashSet<MethodSignatureWrapper>());
        }
        Set<MethodSignatureWrapper> methods = methodCallbacks.get(name);

        methods.add(new MethodSignatureWrapper(method));
    }

    public Set<ASTMethod> getMethodCallbacks(String name) {
        Set<ASTMethod> methods = new HashSet<ASTMethod>();

        if(methodCallbacks.containsKey(name)){
            for (MethodSignatureWrapper methodSignatureWrapper : methodCallbacks.get(name)) {
                methods.add(methodSignatureWrapper.getMethod());
            }
        }

        return methods;
    }

    public boolean contains(String name) {
        return methodCallbacks.containsKey(name);
    }
}
