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
package org.androidtransfuse.model;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.util.Contract;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author John Ericksen
 */
public class InjectionSignature {

    private final ASTType type;
    private final ImmutableSet<ASTAnnotation> annotations;

    public InjectionSignature(ASTType type){
        this(type, ImmutableSet.<ASTAnnotation>of());
    }

    public InjectionSignature(ASTType type, ImmutableSet<ASTAnnotation> annotations) {
        Contract.notNull(type, "type");
        Contract.notNull(annotations, "annotations");
        this.type = type;
        this.annotations = annotations;
    }

    public ASTType getType() {
        return type;
    }

    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return buildScopeKeySignature();
    }

    public String buildScopeKeySignature(){
        StringBuilder builder = new StringBuilder();

        builder.append(type.getName());

        for (ASTAnnotation annotation : annotations) {
            builder.append('@');
            builder.append(annotation.getASTType().getName());
            builder.append('(');
            List<String> propertyNames = new ArrayList<String>(annotation.getPropertyNames());
            Collections.sort(propertyNames);
            boolean first = true;
            for (String property : propertyNames) {
                if(first){
                    first = false;
                }
                else{
                    builder.append(',');
                }
                builder.append(property);
                builder.append('=');
                builder.append(annotation.getProperty(property, Object.class).toString());
            }
            builder.append(')');
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof InjectionSignature)){
            return false;
        }

        InjectionSignature that = (InjectionSignature) o;

        return new EqualsBuilder()
                .append(type, that.type)
                .append(annotations, that.annotations).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).append(annotations).hashCode();
    }
}
