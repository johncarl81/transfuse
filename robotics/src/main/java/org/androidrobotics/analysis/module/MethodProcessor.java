package org.androidrobotics.analysis.module;

import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTMethod;

public interface MethodProcessor {

    void process(ASTMethod astMethod, ASTAnnotation astAnnotation);
}