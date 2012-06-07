package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
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

    public boolean isInterceptor(ASTAnnotation annotation) {
        return interceptorAnnotationMap.containsKey(annotation.getName());
    }
}
