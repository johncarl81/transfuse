package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.AOPRepository;

import javax.inject.Inject;

/**
 * Adds the given annotated method return type as a MethodInterceptor associated with input the annotation value.
 *
 * @author John Ericksen
 */
public class BindInterceptorProcessor implements TypeProcessor {

    private final AOPRepository aopRepository;

    @Inject
    public BindInterceptorProcessor(AOPRepository aopRepository) {
        this.aopRepository = aopRepository;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation bindInterceptor) {
        ASTType annotation = bindInterceptor.getProperty("annotation", ASTType.class);
        ASTType interceptor = bindInterceptor.getProperty("interceptor", ASTType.class);

        return new InterceptorsConfiguration(annotation, interceptor);
    }


    private final class InterceptorsConfiguration implements ModuleConfiguration{

        private final ASTType annotation;
        private final ASTType interceptor;

        private InterceptorsConfiguration(ASTType annotation, ASTType interceptor) {
            this.annotation = annotation;
            this.interceptor = interceptor;
        }

        @Override
        public void setConfiguration() {
            aopRepository.put(annotation, interceptor);
        }
    }
}
