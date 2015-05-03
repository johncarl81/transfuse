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
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ComponentGenerator {

    private final ComponentBuilderFactory componentBuilderFactory;
    private final Logger log;

    @Inject
    public ComponentGenerator(ComponentBuilderFactory componentBuilderFactory, Logger log) {
        this.componentBuilderFactory = componentBuilderFactory;
        this.log = log;
    }

    public JDefinedClass build(ComponentDescriptor descriptor) {

        if (descriptor == null) {
            return null;
        }

        log.debug(descriptor.toString());

        ComponentBuilder builder = componentBuilderFactory.build(descriptor);

        //pre injection phase
        for (Generation generator : descriptor.getGenerators()) {
            generator.schedule(builder, descriptor);
        }
        builder.build();

        return builder.getDefinedClass();
    }
}
