package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InterceptorRepository {

    private Map<String, ASTType> interceptors = new HashMap<String, ASTType>();

    public void addInterceptor(String annotationType, ASTType interceptorType) {
        interceptors.put(annotationType, interceptorType);
    }

    public Set<String> getInterceptorAnnotationTypes() {
        return interceptors.keySet();
    }

    public ASTType getInterceptorType(String annotationType) {
        return interceptors.get(annotationType);
    }
}
