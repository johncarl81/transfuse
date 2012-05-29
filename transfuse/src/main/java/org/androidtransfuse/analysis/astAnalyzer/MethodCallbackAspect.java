package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.util.MethodSignature;
import org.apache.commons.lang.builder.EqualsBuilder;

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

    public boolean contains(String name) {
        return methodCallbacks.containsKey(name);
    }

    public static final class MethodCallback {
        private ASTMethod method;
        private String name;
        private int superClassLevel;
        private MethodSignature methodSignature;

        public MethodCallback(ASTMethod method, String name, int superClassLevel) {
            this.method = method;
            this.name = name;
            this.superClassLevel = superClassLevel;
            this.methodSignature = new MethodSignature(method);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MethodCallback)) {
                return false;
            }

            MethodCallback that = (MethodCallback) o;

            return new EqualsBuilder().append(methodSignature, that.methodSignature).isEquals();
        }

        @Override
        public int hashCode() {
            return methodSignature.hashCode();
        }
    }
}
