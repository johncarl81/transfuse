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

import com.google.common.collect.ImmutableList;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.annotations.ManualSuper;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ManualSuperAnalysis extends ASTAnalysisAdaptor {

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        analyze(astType, injectionNode);
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        analyze(astMethod, injectionNode);
    }

    private void analyze(ASTBase astBase, InjectionNode injectionNode){
        if(astBase.isAnnotated(ManualSuper.class)){
            ASTAnnotation annotation = astBase.getASTAnnotation(ManualSuper.class);

            ManualSuperAspect aspect = nullSafeGet(injectionNode);
            ImmutableList.Builder<ASTType> parametersBuilder = ImmutableList.builder();
            ASTType[] parameters = annotation.getProperty("parameters", ASTType[].class);
            if(parameters != null) {
                for (ASTType parameterType : parameters) {
                    parametersBuilder.add(parameterType);
                }
            }

            aspect.add(annotation.getProperty("name", String.class), parametersBuilder.build());
        }
    }

    private ManualSuperAspect nullSafeGet(InjectionNode injectionNode){
        if(!injectionNode.containsAspect(ManualSuperAspect.class)){
            injectionNode.addAspect(new ManualSuperAspect());
        }
        return injectionNode.getAspect(ManualSuperAspect.class);
    }
}
