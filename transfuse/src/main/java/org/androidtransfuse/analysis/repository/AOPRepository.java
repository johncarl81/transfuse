package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class AOPRepository {

    private final Map<ASTType, ASTType> interceptorAnnotationMap = new HashMap<ASTType, ASTType>();

    public void put(ASTType annotationType, ASTType interceptor) {
        interceptorAnnotationMap.put(annotationType, interceptor);
    }

    public ASTType getInterceptor(ASTType annotationType) {
        return interceptorAnnotationMap.get(annotationType);
    }

    public boolean isInterceptor(ASTAnnotation annotation) {
        return interceptorAnnotationMap.containsKey(annotation.getASTType());
    }
}
