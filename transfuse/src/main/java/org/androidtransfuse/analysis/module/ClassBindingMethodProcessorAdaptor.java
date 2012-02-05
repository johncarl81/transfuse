package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;

public abstract class ClassBindingMethodProcessorAdaptor implements MethodProcessor {

    @Override
    public void process(ASTMethod astMethod, ASTAnnotation astAnnotation) {
        ASTType returnType = astMethod.getReturnType();

        ASTType astType = astAnnotation.getProperty("value", ASTType.class);

        innerProcess(returnType, astType);

    }

    public abstract void innerProcess(ASTType returnType, ASTType annotationValue);

}