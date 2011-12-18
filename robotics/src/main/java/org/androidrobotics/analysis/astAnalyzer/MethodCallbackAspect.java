package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.adapter.ASTMethod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class MethodCallbackAspect {

    private Map<String, Set<ASTMethod>> methodCallbacks = new HashMap<String, Set<ASTMethod>>();

    public void addMethod(String name, ASTMethod method) {
        if (!methodCallbacks.containsKey(name)) {
            methodCallbacks.put(name, new HashSet<ASTMethod>());
        }
        Set<ASTMethod> methods = methodCallbacks.get(name);

        methods.add(method);
    }

    public Set<ASTMethod> getMethods(String name) {
        return methodCallbacks.get(name);
    }
}
