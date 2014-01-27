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
package org.androidtransfuse.config;

import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class SynchronizedElements implements Elements {

    private final Elements elements;

    public SynchronizedElements(Elements elements) {
        this.elements = elements;
    }

    @Override
    public synchronized PackageElement getPackageElement(CharSequence charSequence) {
        return elements.getPackageElement(charSequence);
    }

    @Override
    public synchronized TypeElement getTypeElement(CharSequence charSequence) {
        return elements.getTypeElement(charSequence);
    }

    @Override
    public synchronized Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(AnnotationMirror annotationMirror) {
        return elements.getElementValuesWithDefaults(annotationMirror);
    }

    @Override
    public synchronized String getDocComment(Element element) {
        return elements.getDocComment(element);
    }

    @Override
    public synchronized boolean isDeprecated(Element element) {
        return elements.isDeprecated(element);
    }

    @Override
    public synchronized Name getBinaryName(TypeElement typeElement) {
        return elements.getBinaryName(typeElement);
    }

    @Override
    public synchronized PackageElement getPackageOf(Element element) {
        return elements.getPackageOf(element);
    }

    @Override
    public synchronized List<? extends Element> getAllMembers(TypeElement typeElement) {
        return elements.getAllMembers(typeElement);
    }

    @Override
    public synchronized List<? extends AnnotationMirror> getAllAnnotationMirrors(Element element) {
        return elements.getAllAnnotationMirrors(element);
    }

    @Override
    public synchronized boolean hides(Element element, Element element1) {
        return elements.hides(element, element1);
    }

    @Override
    public synchronized boolean overrides(ExecutableElement executableElement, ExecutableElement executableElement1, TypeElement typeElement) {
        return elements.overrides(executableElement, executableElement1, typeElement);
    }

    @Override
    public synchronized String getConstantExpression(Object o) {
        return elements.getConstantExpression(o);
    }

    @Override
    public synchronized void printElements(Writer writer, Element... elements) {
        this.elements.printElements(writer, elements);
    }

    @Override
    public synchronized Name getName(CharSequence charSequence) {
        return elements.getName(charSequence);
    }
}
