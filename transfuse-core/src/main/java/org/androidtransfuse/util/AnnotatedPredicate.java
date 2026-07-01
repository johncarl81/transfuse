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
package org.androidtransfuse.util;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;

import java.util.Collection;

/**
 * Predicate that matches an ASTAnnotation meta-annotated by any of the given annotation types.
 *
 * @author John Ericksen
 */
public class AnnotatedPredicate implements Predicate<ASTAnnotation> {

    private final ImmutableSet<ASTType> annotationTypes;

    public AnnotatedPredicate(ImmutableSet<ASTType> annotationTypes){
        this.annotationTypes = annotationTypes;
    }

    @Override
    public boolean apply(ASTAnnotation input) {
        Collection<ASTAnnotation> annotations = input.getASTType().getAnnotations();

        for (ASTAnnotation astAnnotation : annotations) {
            if(annotationTypes.contains(astAnnotation.getASTType())){
                return true;
            }
        }

        return false;
    }
}
