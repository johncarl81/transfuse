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

import javax.inject.Inject;

/**
 * Executes a TypeProcessor against the values of the input ASTAnnotation.  This consolidates the general
 * operation of combining multiple configurations into one processor.
 *
 * @author John Ericksen
 */
public class BindConfigurationComposite implements TypeProcessor {

    private final TypeProcessor processor;

    @Inject
    public BindConfigurationComposite(/*@Assisted*/ TypeProcessor processor) {
        this.processor = processor;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation typeAnnotation) {

        ASTAnnotation[] values = typeAnnotation.getProperty("value", ASTAnnotation[].class);

        ModuleConfigurationComposite configurations = new ModuleConfigurationComposite();

        for (ASTAnnotation interceptorBinding : values) {
            configurations.add(processor.process(interceptorBinding));
        }

        return configurations;
    }
}
