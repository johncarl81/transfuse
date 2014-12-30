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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.annotations.Observes;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;

/**
 * Analysis class to find the methods annotated with @Observes.  When found, an ObservesAspect is populated with the
 * observing method.  Multiple observer methods could be defined per class and per InjectionNode
 *
 * @author John Ericksen
 */
public class ObservesAnalysis extends ASTAnalysisAdaptor {

    private final Validator validator;

    @Inject
    public ObservesAnalysis(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {

        ASTParameter firstParameter = null;
        if (astMethod.getParameters().size() > 0) {
            firstParameter = astMethod.getParameters().get(0);
        }
        for (int i = 1; i < astMethod.getParameters().size(); i++) {
            if (astMethod.getParameters().get(i).isAnnotated(Observes.class)) {
                //don't accept @Observes outside of the first parameter
                validator.error("@Observes methods must annotate either the method or first method parameter")
                                               .element(astMethod.getParameters().get(i))
                                               .build();
                throw new TransfuseAnalysisException("Malformed event Observer found on " + astMethod.getName());
            }
        }

        if (firstParameter != null && (firstParameter.isAnnotated(Observes.class) || astMethod.isAnnotated(Observes.class))) {
            //don't accept @Observes with more than one parameter
            if (astMethod.getParameters().size() != 1) {
                validator.error("@Observes methods must contain one and only one event parameter")
                        .element(astMethod)
                        .build();
                throw new TransfuseAnalysisException("Malformed event Observer found on " + astMethod.getName());
            }
            if (!injectionNode.containsAspect(ObservesAspect.class)) {
                injectionNode.addAspect(new ObservesAspect());
            }
            ObservesAspect aspect = injectionNode.getAspect(ObservesAspect.class);

            aspect.addObserver(firstParameter.getASTType(), astMethod);
        }
    }
}
