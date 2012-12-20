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
package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public class InjectionSignature {

    private final ASTType type;
    private final Collection<ASTAnnotation> annotations;

    public InjectionSignature(ASTType type, Collection<ASTAnnotation> annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    public ASTType getType() {
        return type;
    }

    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "InjectionSignature{" +
                "type=" + type +
                ", annotations=" + StringUtils.join(annotations, ",") +
                '}';
    }
}
