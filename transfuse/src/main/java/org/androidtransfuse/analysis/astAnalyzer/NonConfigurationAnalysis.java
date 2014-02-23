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

import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.annotations.NonConfigurationInstance;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * Analysis class to identify nonConfiguration Instances(NCI).  This analysis triggers the given NCI to be defined as
 * a field in the containing component.  This field is then used in the generated onRetainNonConfigurationInstance() and
 * getLastNonConfigurationInstance() methods.
 *
 * @author John Ericksen
 */
public class NonConfigurationAnalysis extends ASTAnalysisAdaptor {

    private final InjectionPointFactory injectionPointFactory;

    @Inject
    public NonConfigurationAnalysis(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context) {

        if(astField.isAnnotated(NonConfigurationInstance.class)){
            NonConfigurationAspect aspect = buildAspect(injectionNode);
            aspect.add(injectionPointFactory.buildInjectionPoint(injectionNode.getASTType(), concreteType, astField, context));

            if (!injectionNode.containsAspect(ASTInjectionAspect.class)) {
                injectionNode.addAspect(ASTInjectionAspect.class, new ASTInjectionAspect());
            }

            injectionNode.getAspect(ASTInjectionAspect.class).setAssignmentType(ASTInjectionAspect.InjectionAssignmentType.FIELD);
        }
    }

    private NonConfigurationAspect buildAspect(InjectionNode injectionNode){
        if(!injectionNode.containsAspect(NonConfigurationAspect.class)){
            injectionNode.addAspect(new NonConfigurationAspect());
        }
        return injectionNode.getAspect(NonConfigurationAspect.class);
    }
}
