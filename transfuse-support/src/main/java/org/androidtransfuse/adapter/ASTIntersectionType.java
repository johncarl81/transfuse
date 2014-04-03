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

import java.util.List;

/**
 * @author Dan Bachelder
 */
public class ASTIntersectionType extends ASTEmptyType {
    public static final ASTIntersectionType INTERSECTION = new ASTIntersectionType();

    private final List<ASTType> bounds;

    private ASTIntersectionType() {
        this(null);
    }

    public ASTIntersectionType(List<ASTType> bounds) {
        super("?");
        this.bounds = bounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTIntersectionType)) {
            return false;
        }

        ASTIntersectionType that = (ASTIntersectionType) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(bounds, that.bounds)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(bounds).hashCode();
    }
}