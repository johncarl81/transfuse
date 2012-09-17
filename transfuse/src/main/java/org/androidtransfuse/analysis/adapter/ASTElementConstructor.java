package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Element specific implementation of the AST Constructor
 *
 * @author John Ericksen
 */
public class ASTElementConstructor extends ASTElementBase implements ASTConstructor {

    private final ImmutableList<ASTParameter> parameters;
    private final ASTAccessModifier modifier;
    private final ImmutableList<ASTType> throwsTypes;

    public ASTElementConstructor(ExecutableElement executableElement,
                                 ImmutableList<ASTParameter> parameters,
                                 ASTAccessModifier modifier,
                                 ImmutableCollection<ASTAnnotation> annotations,
                                 ImmutableList<ASTType> throwsTypes) {
        super(executableElement, annotations);
        this.parameters = parameters;
        this.modifier = modifier;
        this.throwsTypes = throwsTypes;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public List<ASTType> getThrowsTypes() {
        return throwsTypes;
    }
}
