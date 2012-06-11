package org.androidtransfuse.util.matcher;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ASTTypeMatcherBuilder {

    private Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();

    public ASTTypeMatcherBuilder annotatedWith(Class<? extends Annotation> annotationClass) {
        annotations.add(annotationClass);
        return this;
    }

    public Matcher build() {
        return new ASTTypeMatcher(annotations);
    }
}
