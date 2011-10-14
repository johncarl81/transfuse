package org.androidrobotics.analysis;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ClassAnalysisBridge extends ReflectionAnalysisBridgeBase implements TypeAnalysisBridge {

    private Class<?> clazz;

    public ClassAnalysisBridge(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Collection<AnalysisBridge> getEnclosedElements() {
        ArrayList<AnalysisBridge> analysisBridges = new ArrayList<AnalysisBridge>();
        analysisBridges.addAll(wrap(clazz.getDeclaredFields()));
        analysisBridges.addAll(wrap(clazz.getDeclaredMethods()));
        analysisBridges.addAll(wrap(clazz.getConstructors()));
        return analysisBridges;
    }

    private Collection<AnalysisBridge> wrap(Constructor<?>[] constructors) {
        List<AnalysisBridge> bridgedElements = new ArrayList<AnalysisBridge>();

        for (Constructor constructor : constructors) {
            bridgedElements.add(new ConstructorAnalysisBridge(constructor));
        }
        return bridgedElements;
    }

    private Collection<AnalysisBridge> wrap(Method[] declaredMethods) {
        List<AnalysisBridge> bridgedElements = new ArrayList<AnalysisBridge>();

        for (Method method : declaredMethods) {
            bridgedElements.add(new MethodAnalysisBridge(method));
        }
        return bridgedElements;
    }

    private Collection<AnalysisBridge> wrap(Field[] declaredFields) {
        List<AnalysisBridge> bridgedElements = new ArrayList<AnalysisBridge>();

        for (Field fieldt : declaredFields) {
            bridgedElements.add(new FieldAnalysisBridge(fieldt));
        }
        return bridgedElements;
    }

    @Override
    public ElementKind getType() {
        return ElementKind.CLASS;
    }
}
