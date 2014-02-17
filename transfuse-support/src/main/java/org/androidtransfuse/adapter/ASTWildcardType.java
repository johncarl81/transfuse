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
package org.androidtransfuse.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class ASTWildcardType extends ASTEmptyType {

    public static final ASTWildcardType WILDCARD = new ASTWildcardType();

    private final ASTType superBound;
    private final ASTType extendsBound;

    private ASTWildcardType() {
        this(null, null);
    }

    public ASTWildcardType(ASTType superBound, ASTType extendsBound) {
        super("?");
        this.superBound = superBound;
        this.extendsBound = extendsBound;
    }

    public ASTType getSuperBound() {
        return superBound;
    }

    public ASTType getExtendsBound() {
        return extendsBound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTWildcardType)) {
            return false;
        }

        ASTWildcardType that = (ASTWildcardType) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(superBound, that.superBound)
                .append(extendsBound, that.extendsBound)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(superBound).append(extendsBound).hashCode();
    }
}