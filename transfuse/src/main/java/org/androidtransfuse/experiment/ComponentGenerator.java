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
package org.androidtransfuse.experiment;

import com.sun.codemodel.JDefinedClass;

import javax.inject.Inject;

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

        if (descriptor == null) {
            return null;
        }

        ComponentBuilder builder = componentBuilderFactory.build(descriptor);

        //pre injection phase
        for (Generation generator : descriptor.getGenerators()) {
            generator.schedule(builder, descriptor);
        }
        builder.build();

        return builder.getDefinedClass();
    }
}
