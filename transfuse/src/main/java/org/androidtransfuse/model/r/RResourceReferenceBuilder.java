/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.model.r;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class RResourceReferenceBuilder {

    private final JCodeModel codeModel;
    private final RResource rResource;

    @Inject
    public RResourceReferenceBuilder(JCodeModel codeModel, RResource rResource) {
        this.codeModel = codeModel;
        this.rResource = rResource;
    }

    public JExpression buildReference(Integer id){
        return buildReference(rResource.getResourceIdentifier(id));
    }

    public JExpression buildReference(ResourceIdentifier viewResourceIdentifier) {
        ASTType rInnerType = viewResourceIdentifier.getRInnerType();

        JClass rInnerRef = codeModel.ref(rInnerType.getName());

        return rInnerRef.staticRef(viewResourceIdentifier.getName());
    }
}
