package org.androidtransfuse;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.AbstractProcessor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author John Ericksen
 */
public abstract class AnnotationProcessorBase extends AbstractProcessor {

    /**
     * Gets the supported annotations from the @SupportedAnnotations annotation, which deals with classes instead of
     * strings.
     *
     * @return Set of supported annotation names
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Class<? extends Annotation>[] supportedAnnotations = getClass().getAnnotation(SupportedAnnotations.class).value();

        return FluentIterable
                .from(Arrays.asList(supportedAnnotations))
                .transform(new ClassToNameTransform())
                .toImmutableSet();
    }

    private static class ClassToNameTransform implements Function<Class, String> {
        @Override
        public String apply(Class input) {
            return input.getName();
        }
    }
}
