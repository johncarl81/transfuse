package org.androidtransfuse.util.matcher;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.lang.annotation.Annotation;

/**
 * Determines matching based on the input set of annotations.  All annotations must be present to match the given type.
 *
 * @author John Ericksen
 */
public class ASTTypeMatcher implements Matcher<ASTType> {

    private final ImmutableSet<Class<? extends Annotation>> annotations;

    public ASTTypeMatcher(ImmutableSet<Class<? extends Annotation>> annotations) {
        this.annotations = annotations;
    }

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
