package org.androidtransfuse.adapter;

import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LazyASTTypeParameterBuilder implements org.androidtransfuse.adapter.LazyTypeParameterBuilder {

    private final ASTType type;

    @Inject
    public LazyASTTypeParameterBuilder(/*@Assisted*/ ASTType type) {
        this.type = type;
    }

    public synchronized ImmutableList<ASTType> buildGenericParameters() {
        return ImmutableList.of(type);
    }
}
