package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;

public interface MethodProcessor {

    void process(ASTMethod astMethod, ASTAnnotation astAnnotation);
}