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
