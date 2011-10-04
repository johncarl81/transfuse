package org.androidrobotics.analysis;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ElementAnalysisBridge implements AnalysisBridge {

    private Element element;

    public ElementAnalysisBridge(Element element) {
        this.element = element;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return element.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return element.getSimpleName().toString();
    }

    @Override
    public Collection<AnalysisBridge> getEnclosedElements() {
        return wrap(element.getEnclosedElements());
    }

    private Collection<AnalysisBridge> wrap(Collection<? extends Element> collection) {
        List<AnalysisBridge> bridgedElements = new ArrayList<AnalysisBridge>();

        for (Element element : collection) {
            bridgedElements.add(new ElementAnalysisBridge(element));
        }
        return bridgedElements;
    }
}
