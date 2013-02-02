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

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.annotations.EventListener;
import org.androidtransfuse.model.InjectionNode;

/**
 * Analyzes the given class for listener annotations.  Adds these annotated methods to a ListenerAspect for
 * code generation during the generation phase.
 *
 * @author John Ericksen
 */
public class ListenerAnalysis extends ASTAnalysisAdaptor {

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        for (ASTAnnotation annotation : astMethod.getAnnotations()) {
            ASTType annotationType = annotation.getASTType();
            if (annotationType.isAnnotated(EventListener.class)) {
                addMethod(injectionNode, annotationType, astMethod);
            }
        }
    }

    private void addMethod(InjectionNode injectionNode, ASTType annotation, ASTMethod astMethod) {

        if (!injectionNode.containsAspect(ListenerAspect.class)) {
            injectionNode.addAspect(new ListenerAspect());
        }
        ListenerAspect methodCallbackToken = injectionNode.getAspect(ListenerAspect.class);

        methodCallbackToken.addMethodCallback(annotation, astMethod);

        ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);

        if (injectionAspect == null) {
            injectionAspect = new ASTInjectionAspect();
            injectionNode.addAspect(ASTInjectionAspect.class, injectionAspect);
        }

        //injection node is now required outside of the local scope
        injectionAspect.setAssignmentType(ASTInjectionAspect.InjectionAssignmentType.FIELD);
    }
}
