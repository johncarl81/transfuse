package org.androidrobotics.analysis.adapter;

import org.androidrobotics.util.CollectionConverterUtil;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTElementFactory {

    @Inject
    private CollectionConverterUtil collectionConverterUtil;
    @Inject
    private ASTElementConverterFactory astElementConverterFactory;
    @Inject
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public ASTType buildASTElementType(TypeElement typeElement) {

        List<ASTConstructor> constructors = collectionConverterUtil.wrapCollection(typeElement.getEnclosedElements(),
                astElementConverterFactory.buildASTElementConverter(ASTConstructor.class));

        List<ASTField> fields = collectionConverterUtil.wrapCollection(typeElement.getEnclosedElements(),
                astElementConverterFactory.buildASTElementConverter(ASTField.class));

        List<ASTMethod> methods = collectionConverterUtil.wrapCollection(typeElement.getEnclosedElements(),
                astElementConverterFactory.buildASTElementConverter(ASTMethod.class));

        return new ASTElementType(typeElement, constructors, methods, fields);
    }

    public ASTElementField buildASTElementVariable(VariableElement variableElement) {
        ASTType type = variableElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementField(variableElement, type);
    }

    public ASTMethod buildASTElementMethod(ExecutableElement executableElement) {

        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());

        return new ASTElementMethod(executableElement, parameters);
    }

    private List<ASTParameter> buildASTElementParameters(List<? extends VariableElement> variableElements) {
        List<ASTParameter> astParameters = new ArrayList<ASTParameter>();

        for (VariableElement variables : variableElements) {
            astParameters.add(buildASTElementParameter(variables));
        }

        return astParameters;
    }

    private ASTParameter buildASTElementParameter(VariableElement variableElement) {
        ASTType type = variableElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementParameter(variableElement, type);
    }

    public ASTParameter buildASTElementParameter(TypeParameterElement typeParameterElement) {
        ASTType type = typeParameterElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementParameter(typeParameterElement, type);
    }

    public ASTConstructor buildASTElementConstructor(ExecutableElement executableElement) {
        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());
        return new ASTElementConstructor(executableElement, parameters);
    }
}
