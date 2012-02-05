package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class AOPRepository {

    private Map<String, ASTType> interceptorAnnotationMap = new HashMap<String, ASTType>();

    public void put(ASTType interceptor, ASTType annotationType) {
        interceptorAnnotationMap.put(interceptor.getName(), annotationType);
    }

    public ASTType getInterceptor(String annotation) {
        return interceptorAnnotationMap.get(annotation);
    }

    public boolean isInterceptor(String annotation) {
        return interceptorAnnotationMap.containsKey(annotation);
    }
}
