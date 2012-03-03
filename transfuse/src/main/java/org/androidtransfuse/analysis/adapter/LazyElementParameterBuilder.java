package org.androidtransfuse.analysis.adapter;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author John Ericksen
 */
public class LazyElementParameterBuilder implements LazyTypeParameterBuilder {

    private DeclaredType declaredType;
    private List<ASTType> genericParameters = null;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

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

    public List<ASTType> innerBuildGenericParameters() {

        List<ASTType> typeParameters = new ArrayList<ASTType>();

        for (TypeMirror type : declaredType.getTypeArguments()) {
            ASTType parameterType = type.accept(astTypeBuilderVisitor, null);

            if (parameterType == null) {
                //unaable to resolve concrete type
                return Collections.emptyList();
            }

            typeParameters.add(parameterType);
        }

        return typeParameters;

    }
}
