package org.androidrobotics.analysis.module;

import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;

public abstract class ClassBindingMethodProcessorAdaptor implements MethodProcessor {

    @Override
    public void process(ASTMethod astMethod, ASTAnnotation astAnnotation) {
        ASTType returnType = astMethod.getReturnType();

        ASTType astType = astAnnotation.getProperty("value", ASTType.class);

        innerProcess(returnType, astType);

    }

    public abstract void innerProcess(ASTType returnType, ASTType annotationValue);

}