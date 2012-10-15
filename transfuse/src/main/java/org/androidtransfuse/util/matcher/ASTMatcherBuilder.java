package org.androidtransfuse.util.matcher;

import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ASTMatcherBuilder {

    private final ASTClassFactory astClassFactory;

    @Inject
    public ASTMatcherBuilder(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    public ASTTypeMatcherBuilder type() {
        return new ASTTypeMatcherBuilder();
    }

    public ASTTypeMatcherBuilder type(ASTType astType) {
        return new ASTTypeMatcherBuilder(astType);
    }

    public ASTTypeMatcherBuilder type(Class<?> clazz) {
        return type(astClassFactory.buildASTClassType(clazz));
    }
}
