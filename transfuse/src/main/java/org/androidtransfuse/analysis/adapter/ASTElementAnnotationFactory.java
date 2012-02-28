package org.androidtransfuse.analysis.adapter;

import javax.lang.model.element.AnnotationMirror;

/**
 * Factory creating an ASTElementAnnotation
 *
 * @author John Ericksen
 */
public interface ASTElementAnnotationFactory {

    ASTElementAnnotation buildASTElementAnnotation(AnnotationMirror annotationMirror);
}
