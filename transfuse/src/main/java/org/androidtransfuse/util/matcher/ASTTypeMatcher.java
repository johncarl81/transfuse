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
import org.androidtransfuse.analysis.adapter.ASTType;

import java.lang.annotation.Annotation;

/**
 * Determines matching based on the input set of annotations.  All annotations must be present to match the given type.
 *
 * @author John Ericksen
 */
public class ASTTypeMatcher implements Matcher<ASTType> {

    private final ImmutableSet<Class<? extends Annotation>> annotations;
    private final ASTType astType;
    private final boolean ignoreGenericParameters;

    public ASTTypeMatcher(ImmutableSet<Class<? extends Annotation>> annotations, ASTType astType, boolean ignoreGenericParameters) {
        this.annotations = annotations;
        this.astType = astType;
        this.ignoreGenericParameters = ignoreGenericParameters;
    }

    public boolean matches(ASTType astType) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (!astType.isAnnotated(annotation)) {
                return false;
            }
        }

        if(this.astType != null){
            if(ignoreGenericParameters){
                if(!astType.getName().equals(this.astType.getName())){
                    return false;
                }
            }
            else{
                if(!astType.equals(this.astType)){
                    return false;
                }
            }
        }

        return true;
    }
}
