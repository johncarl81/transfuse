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
package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JType;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.annotations.Resource;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilderFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ResourceInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final ClassGenerationUtil generationUtil;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final ResourceExpressionBuilderFactory resourceExpressionBuilderFactory;
    private final Analyzer analyzer;

    @Inject
    public ResourceInjectionNodeBuilder(ClassGenerationUtil generationUtil,
                                        VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                        ResourceExpressionBuilderFactory resourceExpressionBuilderFactory,
                                        Analyzer analyzer) {
        super(Resource.class);
        this.generationUtil = generationUtil;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.resourceExpressionBuilderFactory = resourceExpressionBuilderFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTBase target, InjectionSignature signature, AnalysisContext context, ASTAnnotation annotation) {
        Integer resourceId = annotation.getProperty("value", Integer.class);
        
        InjectionNode injectionNode = analyzer.analyze(signature, context);

        JType resourceType = generationUtil.type(signature.getType());

        ResourceExpressionBuilder resourceExpressionBuilder =
                resourceExpressionBuilderFactory.buildResourceExpressionBuilder(resourceType, context);

        injectionNode.addAspect(VariableBuilder.class,
                variableInjectionBuilderFactory.buildResourceVariableBuilder(resourceId, resourceExpressionBuilder));

        return injectionNode;
    }


}
