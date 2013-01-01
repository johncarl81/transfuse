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
package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class GeneratorRepository {

    private final ImmutableMap<Class<? extends Annotation>, TransactionProcessorBuilder<Provider<ASTType>, ?>> componentBuilders;
    private final TransactionProcessor processor;

    public GeneratorRepository(ImmutableMap<Class<? extends Annotation>, TransactionProcessorBuilder<Provider<ASTType>, ?>> componentBuilders, TransactionProcessor processor) {
        this.componentBuilders = componentBuilders;
        this.processor = processor;
    }

    public TransactionProcessorBuilder<Provider<ASTType>, ?> getComponentBuilder(Class<? extends Annotation> componentAnnotation) {
        return componentBuilders.get(componentAnnotation);
    }

    public Map<Class<? extends Annotation>, TransactionProcessorBuilder<Provider<ASTType>, ?>> getComponentBuilders() {
        return componentBuilders;
    }

    public TransactionProcessor getProcessor() {
        return processor;
    }
}
