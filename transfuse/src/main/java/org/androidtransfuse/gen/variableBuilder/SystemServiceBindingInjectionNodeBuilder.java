/**
 * Copyright 2012 John Ericksen
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

import android.content.Context;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.SystemService;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SystemServiceBindingInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final InjectionPointFactory injectionPointFactory;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public SystemServiceBindingInjectionNodeBuilder(InjectionPointFactory injectionPointFactory,
                                                    VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        super(SystemService.class);
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String systemService = annotation.getProperty("value", String.class);

        InjectionNode injectionNode = new InjectionNode(astType);

        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(Context.class, context);

        injectionNode.addAspect(VariableBuilder.class,
                variableInjectionBuilderFactory.buildSystemServiceVariableBuilder(
                        systemService,
                        contextInjectionNode));

        return injectionNode;
    }
}
