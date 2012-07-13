package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.AOPRepository;

import javax.inject.Inject;

/**
 * Adds the given annotated method return type as a MethodInterceptor associated with input the annotation value.
 *
 * @author John Ericksen
 */
public class BindInterceptorProcessor extends MethodProcessor {

    private AOPRepository aopRepository;

    @Inject
    public BindInterceptorProcessor(AOPRepository aopRepository) {
        this.aopRepository = aopRepository;
    }

    @Override
    public void innerProcess(ASTType returnType, ASTType annotationValue) {
        aopRepository.put(annotationValue, returnType);
    }
}