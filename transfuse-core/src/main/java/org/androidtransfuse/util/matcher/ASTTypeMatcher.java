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

import org.androidtransfuse.adapter.ASTType;

/**
 * Matches a ASTType based on the given type.  Both ASTTypes must be equal.
 *
 * @author John Ericksen
 */
public class ASTTypeMatcher implements Matcher<ASTType> {

    private final ASTType astType;
    private final boolean ignoreGenerics;
    private final boolean subtypesAllowed;

    public ASTTypeMatcher(ASTType astType, boolean ignoreGenerics, boolean subtypesAllowed){
        this.astType = astType;
        this.ignoreGenerics = ignoreGenerics;
        this.subtypesAllowed = subtypesAllowed;
    }

    public boolean matches(ASTType astType) {

        if(this.astType != null){
            if(ignoreGenerics){
                if(!astType.getName().equals(this.astType.getName())){
                    return false;
                }
            }
            else{
                if(subtypesAllowed){
                    return this.astType.inheritsFrom(astType);
                }
                if(!astType.equals(this.astType)){
                    return false;
                }
            }
        }

        return true;
    }
}
