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
package org.androidtransfuse.model.r;

import org.androidtransfuse.adapter.ASTType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class ResourceIdentifier {

    private final ASTType rInnerType;
    private final String name;

    public ResourceIdentifier(ASTType rInnerType, String name) {
        this.name = name;
        this.rInnerType = rInnerType;
    }

    public String getName() {
        return name;
    }

    public ASTType getRInnerType() {
        return rInnerType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ResourceIdentifier)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ResourceIdentifier rhs = (ResourceIdentifier) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(rInnerType, rhs.rInnerType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(rInnerType).hashCode();
    }
}
