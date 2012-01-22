package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.AnnotationMirror;

/**
 * @author John Ericksen
 */
public interface ASTElementAnnotationFactory {

    ASTElementAnnotation buildASTElementAnnotation(AnnotationMirror annotationMirror);
}
