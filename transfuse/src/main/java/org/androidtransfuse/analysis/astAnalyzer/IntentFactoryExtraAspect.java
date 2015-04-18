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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.adapter.ASTType;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Aspect representing an extra parameter required by a Component.  This will trigger the code generator to
 * build an IntentFactoryStrategy containing the appropriate extras in the constructor or setters.
 *
 * @author John Ericksen
 */
public class IntentFactoryExtraAspect implements Comparable<IntentFactoryExtraAspect> {

    private final boolean required;
    private final String name;
    private final ASTType type;
    private final boolean forceParceler;

    public IntentFactoryExtraAspect(boolean required, String name, boolean forceParceler, ASTType type) {
        this.required = required;
        this.name = name;
        this.type = type;
        this.forceParceler = forceParceler;
    }

    public boolean isRequired() {
        return required;
    }

    public String getName() {
        return name;
    }

    public ASTType getType() {
        return type;
    }

    public boolean isForceParceler() {
        return forceParceler;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntentFactoryExtraAspect)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IntentFactoryExtraAspect rhs = (IntentFactoryExtraAspect) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(IntentFactoryExtraAspect intentFactoryExtra) {
        return getName().compareTo(intentFactoryExtra.getName());
    }
}
