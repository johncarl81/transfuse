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
package org.androidtransfuse.analysis.astAnalyzer;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAccessModifier;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

/**
 * Analyzer to add Aspect Oriented Programming method interceptors from the AOP Repository to the appropriately
 * annotated methods.
 *
 * @author John Ericksen
 */
public class AOPProxyAnalyzer extends ASTAnalysisAdaptor {

    private final InjectionPointFactory injectionPointFactory;

    @Inject
    public AOPProxyAnalyzer(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType concreteType, AnalysisContext context) {
        //AOP is only available on top level
        if (injectionNode.getASTType().equals(concreteType)) {
            for (ASTAnnotation annotation : concreteType.getAnnotations()) {
                if (context.getInjectionNodeBuilders().isInterceptor(annotation)) {

                    ImmutableSet<ASTMethod> nonPrivateMethods = FluentIterable.from(concreteType.getMethods()).filter(new Predicate<ASTMethod>() {
                        @Override
                        public boolean apply(ASTMethod method) {
                            return !ASTAccessModifier.PRIVATE.equals(method.getAccessModifier());
                        }
                    }).toImmutableSet();

                    addInterceptor(injectionNode, nonPrivateMethods, getInterceptorInjectionNode(annotation, context));
                }
            }
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        //AOP is only available on top level
        if (injectionNode.getASTType().equals(concreteType)) {
            for (ASTAnnotation annotation : astMethod.getAnnotations()) {
                if (context.getInjectionNodeBuilders().isInterceptor(annotation)) {
                    addInterceptor(injectionNode, Collections.singleton(astMethod), getInterceptorInjectionNode(annotation, context));
                }
            }
        }
    }

    private InjectionNode getInterceptorInjectionNode(ASTAnnotation methodAnnotation, AnalysisContext context) {
        ASTType interceptorType = context.getInjectionNodeBuilders().getInterceptor(methodAnnotation.getASTType());

        return injectionPointFactory.buildInjectionNode(interceptorType, context);
    }

    private void addInterceptor(InjectionNode injectionNode, Collection<ASTMethod> astMethods, InjectionNode interceptor) {
        if (!injectionNode.containsAspect(AOPProxyAspect.class)) {
            injectionNode.addAspect(new AOPProxyAspect());
        }

        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);

        aopProxyAspect.addInterceptors(astMethods, interceptor);
    }
}
