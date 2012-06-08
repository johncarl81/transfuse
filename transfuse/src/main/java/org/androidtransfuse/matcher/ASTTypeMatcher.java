package org.androidtransfuse.matcher;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ASTTypeMatcher implements Match {

    private Set<Class<? extends Annotation>> annotations;

    public ASTTypeMatcher(Set<Class<? extends Annotation>> annotations) {
        this.annotations = new HashSet<Class<? extends Annotation>>(annotations);
    }

    @Override
    public boolean matches(ASTType astType) {
        boolean matched = true;

        for (Class<? extends Annotation> annotation : annotations) {
            if (!astType.isAnnotated(annotation)) {
                matched = false;
            }
        }

        return matched;
    }
}
