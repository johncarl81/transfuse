package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * Module processing class for handling Method annotations.  Pulls the return type and annotation value field
 * and supplies them to the abstract innerProcess method.
 *
 * @author John Ericksen
 */
public abstract class MethodProcessor {

    public void process(ASTMethod astMethod, ASTAnnotation astAnnotation) {
        ASTType returnType = astMethod.getReturnType();
        ASTType astType = astAnnotation.getProperty("value", ASTType.class);
        innerProcess(returnType, astType);
    }

    public abstract void innerProcess(ASTType returnType, ASTType annotationValue);

}