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
package org.androidtransfuse.adapter.element;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.ASTUtils;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/**
 * Element specific ASTBase implementation
 *
 * @author John Ericksen
 */
public class ASTElementBase implements ASTBase {

    private final Element element;
    private final ImmutableSet<ASTAnnotation> annotations;

    public ASTElementBase(Element element, ImmutableSet<ASTAnnotation> annotations) {
        this.element = element;
        this.annotations = annotations;
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return getASTAnnotation(annotation) != null;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return element.getAnnotation(annotation);
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    public Element getElement() {
        return element;
    }
}
