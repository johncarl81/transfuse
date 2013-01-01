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
package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class RLayoutBuilder implements LayoutBuilder {

    private final Integer layout;
    private final RResourceReferenceBuilder rResourceReferenceBuilder;
    private final RResource rResource;

    @Inject
    public RLayoutBuilder(@Assisted Integer layout, RResourceReferenceBuilder rResourceReferenceBuilder, RResource rResource) {
        this.layout = layout;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.rResource = rResource;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block) {
        //layout setting
        ResourceIdentifier layoutIdentifier = rResource.getResourceIdentifier(layout);
        if (layoutIdentifier != null) {
            block.invoke("setContentView").arg(rResourceReferenceBuilder.buildReference(layoutIdentifier));
        }
    }
}
