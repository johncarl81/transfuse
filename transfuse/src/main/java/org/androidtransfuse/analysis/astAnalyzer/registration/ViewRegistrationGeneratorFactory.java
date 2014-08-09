/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.analysis.astAnalyzer.registration;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.ListenableMethod;
import org.androidtransfuse.analysis.repository.RegistrationGeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.RegistrationGenerator;
import org.androidtransfuse.gen.componentBuilder.ViewRegistrationInvocationBuilder;
import org.androidtransfuse.gen.componentBuilder.ViewTypeRegistrationInvocationBuilderImpl;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collections;

public final class ViewRegistrationGeneratorFactory implements RegistrationGeneratorFactory {

    private final InjectionPointFactory injectionPointFactory;
    private final ASTClassFactory astClassFactory;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ListenableMethod listenerMethod;

    @Inject
    private ViewRegistrationGeneratorFactory(/*@Assisted*/ ListenableMethod listenerMethod, InjectionPointFactory injectionPointFactory, ASTClassFactory astClassFactory, ComponentBuilderFactory componentBuilderFactory) {
        this.injectionPointFactory = injectionPointFactory;
        this.astClassFactory = astClassFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.listenerMethod = listenerMethod;
    }

    @Override
    public RegistrationGenerator buildRegistrationGenerator(InjectionNode injectionNode, ASTBase astBase, ASTAnnotation registerAnnotation, AnalysisContext context) {

        InjectionNode viewInjectionNode = buildViewInjectionNode(registerAnnotation, context);

        ViewRegistrationInvocationBuilder invocationBuilder;
        if (astBase instanceof ASTType) {
            invocationBuilder = new ViewTypeRegistrationInvocationBuilderImpl();
        } else if (astBase instanceof ASTMethod) {
            invocationBuilder = componentBuilderFactory.buildViewMethodRegistrationInvocationBuilder((ASTMethod) astBase);
        } else if (astBase instanceof ASTField) {
            invocationBuilder = componentBuilderFactory.buildViewFieldRegistrationInvocationBuilder((ASTField) astBase);
        } else {
            throw new TransfuseAnalysisException("ASTBase type not mapped");
        }

        return componentBuilderFactory.buildViewRegistrationGenerator(viewInjectionNode, listenerMethod.getMethod(), injectionNode, invocationBuilder);
    }

    private InjectionNode buildViewInjectionNode(final ASTAnnotation registerAnnotation, AnalysisContext context) {

        ASTType atViewType = astClassFactory.getType(org.androidtransfuse.annotations.View.class);
        ASTAnnotation viewRegistrationAnnotation = new ASTAnnotationPropertyReplacement(registerAnnotation, atViewType);

        return injectionPointFactory.buildInjectionNode(Collections.singleton(viewRegistrationAnnotation), listenerMethod.getListenable(), context);
    }

    private static final class ASTAnnotationPropertyReplacement implements ASTAnnotation {

        private final ASTAnnotation annotation;
        private final ASTType astType;

        private ASTAnnotationPropertyReplacement(ASTAnnotation annotation, ASTType astType) {
            this.annotation = annotation;
            this.astType = astType;
        }

        @Override
        public <T> T getProperty(String name, Class<T> type) {
            return annotation.getProperty(name, type);
        }

        @Override
        public ASTType getASTType() {
            return astType;
        }

        @Override
        public ImmutableSet<String> getPropertyNames() {
            return annotation.getPropertyNames();
        }
    }
}