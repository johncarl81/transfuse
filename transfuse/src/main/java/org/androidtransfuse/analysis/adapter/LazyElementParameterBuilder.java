package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import javax.lang.model.type.DeclaredType;
import java.util.List;

/**
 * @author John Ericksen
 */
public class LazyElementParameterBuilder implements LazyTypeParameterBuilder {

    private final DeclaredType declaredType;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private ImmutableList<ASTType> genericParameters = null;

    @Inject
    public LazyElementParameterBuilder(@Assisted DeclaredType declaredType,
                                       ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.declaredType = declaredType;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    public List<ASTType> buildGenericParameters() {
        if (genericParameters == null) {
            genericParameters = innerBuildGenericParameters();
        }
        return genericParameters;
    }

    public ImmutableList<ASTType> innerBuildGenericParameters() {
        return FluentIterable.from(declaredType.getTypeArguments())
                .transform(astTypeBuilderVisitor)
                .toImmutableList();
    }
}
