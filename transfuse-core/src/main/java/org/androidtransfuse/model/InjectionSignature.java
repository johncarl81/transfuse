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
package org.androidtransfuse.model;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public class InjectionSignature {

    private final ASTType type;
    private final ImmutableSet<ASTAnnotation> annotations;
    private final int hashCode;

    public InjectionSignature(ASTType type, ImmutableSet<ASTAnnotation> annotations) {
        this.type = type;
        this.annotations = annotations;

        //immutable hash code
        this.hashCode = new HashCodeBuilder().append(type).append(annotations).hashCode();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InjectionSignature)) return false;

        InjectionSignature that = (InjectionSignature) o;

        return new EqualsBuilder().append(type, that.type).append(annotations, that.annotations).isEquals();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
