package org.androidtransfuse.util.matcher;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public class ASTTypeMatcherBuilder {

    private final ImmutableSet.Builder<Class<? extends Annotation>> annotations = ImmutableSet.builder();
    private ASTType astType = null;
    private boolean ignoreGenericParameters = false;

    public ASTTypeMatcherBuilder() {
    }

    public ASTTypeMatcherBuilder(ASTType astType) {
        this.astType = astType;
    }

    public ASTTypeMatcherBuilder annotatedWith(Class<? extends Annotation> annotationClass) {
        annotations.add(annotationClass);
        return this;
    }

    public Matcher<ASTType> build() {
        return new ASTTypeMatcher(annotations.build(), astType, ignoreGenericParameters);
    }

    public ASTTypeMatcherBuilder ignoreGenericParameters(){
        ignoreGenericParameters = true;
        return this;
    }
}
