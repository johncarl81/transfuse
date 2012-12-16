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
 * @author John Ericksen
 */
public class ASTTypeMatcherBuilder {

    private final ImmutableSet.Builder<Class<? extends Annotation>> annotations = ImmutableSet.builder();
    private ASTType astType = null;
    private boolean ignoreGenerics = false;

    public ASTTypeMatcherBuilder() {
    }

    public ASTTypeMatcherBuilder(ASTType astType) {
        this.astType = astType;
    }

    public ASTTypeMatcherBuilder annotatedWith(Class<? extends Annotation> annotationClass) {
        annotations.add(annotationClass);
        return this;
    }

    public Matcher<ASTType> build() {
        return new ASTTypeMatcher(annotations.build(), astType, ignoreGenerics);
    }

    public ASTTypeMatcherBuilder ignoreGenerics(){
        ignoreGenerics = true;
        return this;
    }
}
