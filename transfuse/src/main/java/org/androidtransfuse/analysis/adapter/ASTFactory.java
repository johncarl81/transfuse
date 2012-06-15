package org.androidtransfuse.analysis.adapter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.DeclaredType;
import java.lang.reflect.ParameterizedType;

/**
 * Factory creating an ASTElementAnnotation
 *
 * @author John Ericksen
 */
public interface ASTFactory {

    ASTElementAnnotation buildASTElementAnnotation(AnnotationMirror annotationMirror, ASTType type);

    LazyClassParameterBuilder builderParameterBuilder(ParameterizedType genericType);

    LazyElementParameterBuilder buildParameterBuilder(DeclaredType declaredType);

    ASTGenericTypeWrapper buildGenericTypeWrapper(ASTType astType, LazyTypeParameterBuilder lazyTypeParameterBuilder);
}
