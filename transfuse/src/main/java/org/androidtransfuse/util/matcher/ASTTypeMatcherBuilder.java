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

import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * Builder for an ASTTypeMatcher
 *
 * @author John Ericksen
 */
public class ASTTypeMatcherBuilder {

    private final ASTType astType;
    private boolean ignoreGenerics = false;

    public ASTTypeMatcherBuilder(ASTType astType) {
        this.astType = astType;
    }

    public Matcher<ASTType> build() {
        return new ASTTypeMatcher(astType, ignoreGenerics);
    }

    public ASTTypeMatcherBuilder ignoreGenerics() {
        ignoreGenerics = true;
        return this;
    }

    public InjectionSignatureMatcherBuilder annotated(){
        return new InjectionSignatureMatcherBuilder(build());
    }
}
