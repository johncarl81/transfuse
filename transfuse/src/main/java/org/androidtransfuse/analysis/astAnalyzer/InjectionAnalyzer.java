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

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTConstructor;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * Analyzer to setup InjectionPoints for all @Inject annotated elements
 *
 * @author John Ericksen
 */
public class InjectionAnalyzer implements ASTAnalysis {

    private final InjectionPointFactory injectionPointFactory;

    @Inject
    public InjectionAnalyzer(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType concreteType, AnalysisContext context) {

        if (injectionNode.getASTType().equals(concreteType)) {
            //only analyze the root class for constructor injection
            ASTConstructor noArgConstructor = null;
            ASTConstructor annotatedConstructor = null;

            for (ASTConstructor astConstructor : concreteType.getConstructors()) {
                if (astConstructor.isAnnotated(Inject.class)) {
                    annotatedConstructor = astConstructor;
                    getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(concreteType, astConstructor, context));
                }
                if (astConstructor.getParameters().size() == 0) {
                    noArgConstructor = astConstructor;
                }
            }

            //only allow zero or one annotated constructors.
            if (annotatedConstructor == null && noArgConstructor != null) {
                getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(concreteType, noArgConstructor, context));
            }
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        if (astMethod.isAnnotated(Inject.class)) {
            getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(concreteType, astMethod, context));
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context) {
        if (astField.isAnnotated(Inject.class)) {
            getInjectionToken(injectionNode).add(injectionPointFactory.buildInjectionPoint(concreteType, astField, context));
        }
    }

    private ASTInjectionAspect getInjectionToken(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(ASTInjectionAspect.class)) {
            injectionNode.addAspect(new ASTInjectionAspect());
        }
        return injectionNode.getAspect(ASTInjectionAspect.class);
    }
}
