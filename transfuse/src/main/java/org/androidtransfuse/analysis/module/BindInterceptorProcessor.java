/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.analysis.module;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;

/**
 * Adds the given @BindInterceptor properties as a MethodInterceptor and associated annotation.
 *
 * @author John Ericksen
 */
public class BindInterceptorProcessor implements TypeProcessor {

    private final Logger log;

    @Inject
    public BindInterceptorProcessor(Logger log) {
        this.log = log;
    }

    @Override
    public ModuleConfiguration process(ASTType rootModuleType, ASTType moduleAncestor, ASTAnnotation bindInterceptor) {
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
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            log.debug("Adding interceptor @" + annotation.getPackageClass().getClassName() + ": " + interceptor);
            configurationRepository.putInterceptor(annotation, interceptor);
        }
    }
}
