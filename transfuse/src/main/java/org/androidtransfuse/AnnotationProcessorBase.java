package org.androidtransfuse;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.androidtransfuse.analysis.adapter.ASTElementConverterFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Collections2.transform;

/**
 * @author John Ericksen
 */
public abstract class AnnotationProcessorBase extends AbstractProcessor {
    @Inject
    private ASTElementConverterFactory astElementConverterFactory;

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

    protected Collection<? extends ASTType> getASTTypesAnnotatedWith(RoundEnvironment roundEnvironment, Class<? extends Annotation> annotation) {
        return wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(annotation));
    }

    protected Collection<? extends ASTType> wrapASTCollection(Collection<? extends Element> elementCollection) {
        return transform(elementCollection,
                astElementConverterFactory.buildASTElementConverter(ASTType.class)
        );
    }
}
