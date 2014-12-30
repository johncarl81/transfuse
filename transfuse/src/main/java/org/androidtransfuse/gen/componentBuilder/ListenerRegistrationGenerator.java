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
package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.analysis.astAnalyzer.RegistrationAspect;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ListenerRegistrationGenerator implements Generation {

    private final ASTMethod creationMethod;

    @Inject
    public ListenerRegistrationGenerator(/*@Assisted*/ASTMethod creationMethod) {
        this.creationMethod = creationMethod;
    }

    @Factory
    public interface ListerRegistrationGeneratorFactory {
        ListenerRegistrationGenerator build(ASTMethod creationMethod);
    }

    @Override
    public void schedule(final ComponentBuilder componentBuilder, ComponentDescriptor descriptor) {

        componentBuilder.add(creationMethod, GenerationPhase.POSTINJECTION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
            //add listener registration
            for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : componentBuilder.getExpressionMap().entrySet()) {
                if (injectionNodeJExpressionEntry.getKey().containsAspect(RegistrationAspect.class)) {
                    RegistrationAspect registrationAspect = injectionNodeJExpressionEntry.getKey().getAspect(RegistrationAspect.class);

                    for (RegistrationGenerator builder : registrationAspect.getRegistrationBuilders()) {
                        builder.build(componentBuilder, creationMethod, injectionNodeJExpressionEntry.getValue());
                    }
                }
            }
            }
        });
    }
}
