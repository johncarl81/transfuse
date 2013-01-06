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
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ASTAnnotationTypeMatcher implements Matcher<Collection<ASTAnnotation>>{

    private ImmutableSet<ASTType> types;

    public ASTAnnotationTypeMatcher(ImmutableSet<ASTType> types) {
        this.types = types;
    }

    @Override
    public boolean matches(Collection<ASTAnnotation> annotations) {

        if(annotations.size() != types.size()){
            return false;
        }

        for (ASTAnnotation astAnnotation : annotations) {
            boolean found = false;
            for (ASTType type : types) {
                if(astAnnotation.getASTType().equals(type)){
                    found = true;
                }
            }
            if(!found){
                return false;
            }
        }

        return true;
    }
}
