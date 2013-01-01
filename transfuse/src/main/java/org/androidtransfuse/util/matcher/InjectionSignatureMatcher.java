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

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionSignature;

import java.util.Collection;

/**
 * Matches an InjectionSignature based on the input astType Matcher and ASTAnnotation Collection Matchers.
 * Both matchers must return true for this matcher to return true.
 *
 * @author John Ericksen
 */
public class InjectionSignatureMatcher implements Matcher<InjectionSignature> {

    private final Matcher<? super ASTType> astTypeMatcher;
    private final Collection<Matcher<Collection<ASTAnnotation>>> annotationMatchers;

    public InjectionSignatureMatcher(Matcher<? super ASTType> astTypeMatcher, Collection<Matcher<Collection<ASTAnnotation>>> annotationMatchers) {
        this.astTypeMatcher = astTypeMatcher;
        this.annotationMatchers = annotationMatchers;
    }

    @Override
    public boolean matches(InjectionSignature signature) {
        if(!astTypeMatcher.matches(signature.getType())){
            return false;
        }

        for (Matcher<Collection<ASTAnnotation>> annotationMatcher : annotationMatchers) {
            if(!annotationMatcher.matches(signature.getAnnotations())){
                return false;
            }
        }

        return true;
    }
}
