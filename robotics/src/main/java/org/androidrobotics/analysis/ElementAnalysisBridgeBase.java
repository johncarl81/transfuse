package org.androidrobotics.analysis;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public abstract class ElementAnalysisBridgeBase<T extends Element> implements AnalysisBridge {

    private T element;

    protected ElementAnalysisBridgeBase(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return element.getAnnotation(annotationClass);
    }

    @Override
    public <A extends Annotation> boolean isAnnotated(Class<A> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    @Override
    public String getName() {
        return element.getSimpleName().toString();
    }

    @Override
    public Collection<AnalysisBridge> getEnclosedElements() {
        return wrap(element.getEnclosedElements());
    }

    @Override
    public ElementKind getType() {
        return element.getKind();
    }

    public abstract void accept(AnalysisBridgeVisitor visitor);

    private Collection<AnalysisBridge> wrap(Collection<? extends Element> collection) {
        List<AnalysisBridge> bridgedElements = new ArrayList<AnalysisBridge>();

        for (Element element : collection) {
            bridgedElements.add(
                    element.accept(new AnalysisBridgeWrapperVisitor(), null));
        }
        return bridgedElements;
    }
}
