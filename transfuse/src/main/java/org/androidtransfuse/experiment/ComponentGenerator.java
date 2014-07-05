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
package org.androidtransfuse.experiment;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentGenerator {

    private ComponentBuilderFactory componentBuilderFactory;

    @Inject
    public ComponentGenerator(ComponentBuilderFactory componentBuilderFactory) {
        this.componentBuilderFactory = componentBuilderFactory;
    }

    public JDefinedClass build(ComponentDescriptor descriptor) {

        if (descriptor == null || descriptor.getTarget() == null) {
            return null;
        }

        ComponentBuilder builder = componentBuilderFactory.build(descriptor);

        //pre injection phase
        for (PreInjectionGeneration generator : descriptor.getPreInjectionGenerators()) {
            generator.schedule(builder, descriptor);
        }
        for(GenerationPhase preInjectionPhase : GenerationPhase.preInjectionPhases()) {
            builder.buildPhase(preInjectionPhase);
        }

        //injection phase
        Map<InjectionNode, TypedExpression> expressionMap = new HashMap<InjectionNode, TypedExpression>();

        if (descriptor.getInjectionGenerator() != null) {
            descriptor.getInjectionGenerator().build(builder, descriptor, expressionMap);
        }
        builder.buildPhase(GenerationPhase.INJECTION);

        //post injection phase
        for (PostInjectionGeneration generator : descriptor.getPostInjectionGenerators()) {
            generator.schedule(builder, descriptor, expressionMap);
        }
        for(GenerationPhase preInjectionPhase : GenerationPhase.postInjectionPhases()) {
            builder.buildPhase(preInjectionPhase);
        }
        builder.build();

        return builder.getDefinedClass();
    }
}
