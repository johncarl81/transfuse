package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.AOPRepository;
import org.androidtransfuse.analysis.adapter.ASTType;

public class BindInterceptorProcessor extends ClassBindingMethodProcessorAdaptor {

    private AOPRepository aopRepository;

    public BindInterceptorProcessor(AOPRepository aopRepository) {
        this.aopRepository = aopRepository;
    }

    @Override
    public void innerProcess(ASTType returnType, ASTType annotationValue) {
        aopRepository.put(annotationValue, returnType);
    }
}