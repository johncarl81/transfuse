package org.androidtransfuse.analysis.adapter;

import javax.lang.model.element.ExecutableElement;
import java.util.Collection;
import java.util.List;

/**
 * Element specific implementation of the AST Constructor
 *
 * @author John Ericksen
 */
public class ASTElementConstructor extends ASTElementBase implements ASTConstructor {

    private List<ASTParameter> parameters;
    private ASTAccessModifier modifier;
    private List<ASTType> throwsTypes;

    public ASTElementConstructor(ExecutableElement executableElement, List<ASTParameter> parameters, ASTAccessModifier modifier, Collection<ASTAnnotation> annotations, List<ASTType> throwsTypes) {
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
