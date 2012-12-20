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
package org.androidtransfuse.util.matcher;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;

import java.util.Collection;

/**
 * Matches a collection of annotations exactly to the given set of annotations.  This matches both the type
 * of the annotation and the contained parameters.
 *
 * @author John Ericksen
 */
public class ASTAnnotationMatcher implements Matcher<Collection<ASTAnnotation>> {

    private final ImmutableSet<ASTAnnotation> annotation;

    public ASTAnnotationMatcher(ImmutableSet<ASTAnnotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean matches(Collection<ASTAnnotation> input) {

        if(input.size() != annotation.size()){
            return false;
        }

        for (ASTAnnotation astAnnotation : annotation) {
            boolean found = false;
            for (ASTAnnotation inputAnnotations : input) {
                if(annotationsEquals(astAnnotation, inputAnnotations)){
                    found = true;
                }
            }
            if(!found){
                return false;
            }
        }

        return true;
    }

    private boolean annotationsEquals(ASTAnnotation astAnnotation, ASTAnnotation inputAnnotations) {
        if(!astAnnotation.getASTType().equals(inputAnnotations.getASTType())){
            return false;
        }

        for (String propertyName : astAnnotation.getPropertyNames()) {

            Object annotationProperty = astAnnotation.getProperty(propertyName, Object.class);
            Object inputProperty = inputAnnotations.getProperty(propertyName, Object.class);

            if(!annotationProperty.equals(inputProperty)){
                return false;
            }
        }

        return true;
    }
}
