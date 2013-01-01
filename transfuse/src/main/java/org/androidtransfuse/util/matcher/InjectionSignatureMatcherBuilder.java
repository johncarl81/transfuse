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
package org.androidtransfuse.util.matcher;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.Collection;

/**
 * Builder for an InjectionSignatureMatcher.
 *
 * @author John Ericksen
 */
public class InjectionSignatureMatcherBuilder {

    private final Matcher<? super ASTType> typeMatcher;
    private final ImmutableSet.Builder<Matcher<Collection<ASTAnnotation>>> matchers = ImmutableSet.builder();

    public InjectionSignatureMatcherBuilder(Matcher<? super ASTType> typeMatcher) {
        this.typeMatcher = typeMatcher;
    }

    /**
     * Build the Matcher
     *
     * @return Matcher
     */
    public InjectionSignatureMatcher build() {
        return new InjectionSignatureMatcher(typeMatcher, matchers.build());
    }

    public InjectionSignatureMatcherBuilder byType(ASTType... type){
        ImmutableSet.Builder<ASTType> builder = ImmutableSet.builder();
        if(type != null){
            for (ASTType astType : type) {
                builder.add(astType);
            }
        }
        return byType(builder.build());
    }

    public InjectionSignatureMatcherBuilder byType(ImmutableSet<ASTType> types){

        matchers.add(new ASTAnnotationTypeMatcher(types));

        return this;
    }

    public InjectionSignatureMatcherBuilder byAnnotation(ASTAnnotation... qualifiers) {
        ImmutableSet.Builder<ASTAnnotation> builder = ImmutableSet.builder();
        if(qualifiers != null){
            for (ASTAnnotation astAnnotation : qualifiers) {
                builder.add(astAnnotation);
            }
        }
        return byAnnotation(builder.build());
    }

    public InjectionSignatureMatcherBuilder byAnnotation(ImmutableSet<ASTAnnotation> qualifiers) {

        matchers.add(new ASTAnnotationMatcher(qualifiers));

        return this;
    }
}
