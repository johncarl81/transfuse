package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BindInterceptorsProcessor implements TypeProcessor {

    private final BindInterceptorProcessor bindInterceptorProcessor;

    @Inject
    public BindInterceptorsProcessor(BindInterceptorProcessor bindInterceptorProcessor) {
        this.bindInterceptorProcessor = bindInterceptorProcessor;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation typeAnnotation) {

        ASTAnnotation[] values = typeAnnotation.getProperty("value", ASTAnnotation[].class);

        ModuleConfigurationComposite configurations = new ModuleConfigurationComposite();

        for (ASTAnnotation interceptorBinding : values) {
            configurations.add(bindInterceptorProcessor.process(interceptorBinding));
        }

        return configurations;
    }
}
