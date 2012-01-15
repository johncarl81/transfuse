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

    private Map<String, Set<MethodCallback>> methodCallbacks = new HashMap<String, Set<MethodCallback>>();

    public void addMethodCallback(String name, ASTMethod method, int superClassLevel) {
        if (!methodCallbacks.containsKey(name)) {
            methodCallbacks.put(name, new HashSet<MethodCallback>());
        }
        Set<MethodCallback> methods = methodCallbacks.get(name);

        methods.add(new MethodCallback(method, name, superClassLevel));
    }

    public Set<MethodCallback> getMethodCallbacks(String name) {
        return methodCallbacks.get(name);
    }

    public class MethodCallback {
        private ASTMethod method;
        private String name;
        private int superClassLevel;

        public MethodCallback(ASTMethod method, String name, int superClassLevel) {
            this.method = method;
            this.name = name;
            this.superClassLevel = superClassLevel;
        }

        public ASTMethod getMethod() {
            return method;
        }

        public String getName() {
            return name;
        }

        public int getSuperClassLevel() {
            return superClassLevel;
        }
    }
}
