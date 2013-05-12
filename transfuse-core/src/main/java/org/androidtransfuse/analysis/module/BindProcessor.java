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
package org.androidtransfuse.analysis.module;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.VariableASTImplementationFactory;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;

/**
 * Associates the given return type with the annotated field as a binding.
 *
 * @author John Ericksen
 */
public class BindProcessor implements TypeProcessor {

    private final VariableASTImplementationFactory variableInjectionBuilderFactory;
    private final Validator validator;

    @Inject
    public BindProcessor(VariableASTImplementationFactory variableInjectionBuilderFactory, Validator validator) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.validator = validator;
    }

    @Override
    public ModuleConfiguration process(ASTType moduleType, ASTAnnotation bindAnnotation) {
        ASTType type = bindAnnotation.getProperty("type", ASTType.class);
        ASTType to = bindAnnotation.getProperty("to", ASTType.class);

        if(!to.inheritsFrom(type)){
            validator.error("@Bind to parameter class must inherit from type parameter")
                    .element(moduleType).annotation(bindAnnotation).parameter("to").build();
        }

        return new BindingModuleConfiguration(type, to);
    }

    private final class BindingModuleConfiguration implements ModuleConfiguration{

        private final ASTType type;
        private final ASTType to;

        private BindingModuleConfiguration(ASTType type, ASTType to) {
            this.type = type;
            this.to = to;
        }

        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            configurationRepository.putType(type, variableInjectionBuilderFactory.buildVariableASTBuilder(to));
        }
    }
}