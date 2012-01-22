package org.androidrobotics.analysis.module;

import org.androidrobotics.analysis.AOPRepository;
import org.androidrobotics.analysis.adapter.ASTType;

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