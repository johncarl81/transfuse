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
package org.androidtransfuse.gen.variableBuilder;

import android.app.Activity;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtraAspect;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final InjectionPointFactory injectionPointFactory;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ExtraInjectionNodeBuilder(InjectionPointFactory injectionPointFactory,
                                     VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        super(Extra.class);
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String extraId = annotation.getProperty("value", String.class);
        Boolean optional = annotation.getProperty("optional", Boolean.class);

        if (optional == null) {
            optional = false;
        }

        boolean wrapped = astType.isAnnotated(Parcel.class);

        InjectionNode injectionNode = new InjectionNode(astType);

        InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(Activity.class, context);

        injectionNode.addAspect(IntentFactoryExtraAspect.class, new IntentFactoryExtraAspect(!optional, extraId, astType));

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildExtraVariableBuilder(extraId, activityInjectionNode, optional, wrapped));

        return injectionNode;
    }
}
