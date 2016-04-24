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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.JBlock;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LayoutGenerator implements Generation {

    private final RResourceReferenceBuilder rResourceReferenceBuilder;
    private final RResource rResource;
    private final ASTElementFactory astElementFactory;

    @Inject
    public LayoutGenerator(RResourceReferenceBuilder rResourceReferenceBuilder,
                           RResource rResource,
                           ASTElementFactory astElementFactory) {
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.rResource = rResource;
        this.astElementFactory = astElementFactory;
    }

    @Override
    public String getName() {
        return "Layout Generator";
    }

    @Override
    public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {

        ASTType target = descriptor.getTarget();

        if(target.isAnnotated(Layout.class)) {
            ASTAnnotation layoutAnnotation = target.getASTAnnotation(Layout.class);

            Integer layout = layoutAnnotation == null ? null : layoutAnnotation.getProperty("value", Integer.class);

            //layout setting
            final ResourceIdentifier layoutIdentifier = rResource.getResourceIdentifier(layout);

            ASTMethod onCreateMethod = astElementFactory.findMethod(AndroidLiterals.ACTIVITY, "onCreate", AndroidLiterals.BUNDLE);
            builder.add(onCreateMethod, GenerationPhase.LAYOUT, new ComponentMethodGenerator() {
                @Override
                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                    block.invoke("setContentView").arg(rResourceReferenceBuilder.buildReference(layoutIdentifier));
                }
            });
        }

    }
}
