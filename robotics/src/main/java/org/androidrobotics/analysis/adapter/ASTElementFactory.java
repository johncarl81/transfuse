package org.androidrobotics.analysis.adapter;

import org.androidrobotics.util.TypeCollectionUtil;

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
    private TypeCollectionUtil typeCollectionUtil;
    @Inject
    private ASTElementConverterFactory astElementConverterFactory;
    @Inject
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public ASTType buildASTElementType(TypeElement typeElement) {

        List<ASTConstructor> constructors = typeCollectionUtil.wrapCollection(typeElement.getEnclosedElements(),
                astElementConverterFactory.buildASTElementConverter(ASTConstructor.class));

        List<ASTField> fields = typeCollectionUtil.wrapCollection(typeElement.getEnclosedElements(),
                astElementConverterFactory.buildASTElementConverter(ASTField.class));

        List<ASTMethod> methods = typeCollectionUtil.wrapCollection(typeElement.getEnclosedElements(),
                astElementConverterFactory.buildASTElementConverter(ASTMethod.class));

        return new ASTElementType(typeElement, constructors, methods, fields);
    }

    public ASTElementField buildASTElementVariable(VariableElement variableElement) {
        ASTType type = variableElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementField(variableElement, type);
    }

    public ASTMethod buildASTElementMethod(ExecutableElement executableElement) {

        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getTypeParameters());

        return new ASTElementMethod(executableElement, parameters);
    }

    private List<ASTParameter> buildASTElementParameters(List<? extends TypeParameterElement> typeParameters) {
        List<ASTParameter> astParameters = new ArrayList<ASTParameter>();

        for (TypeParameterElement typeParameter : typeParameters) {
            astParameters.add(buildASTElementParameter(typeParameter));
        }

        return astParameters;
    }

    public ASTParameter buildASTElementParameter(TypeParameterElement typeParameterElement) {
        ASTType type = typeParameterElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementParameter(typeParameterElement, type);
    }

    public ASTConstructor buildASTElementConstructor(ExecutableElement executableElement) {
        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getTypeParameters());
        return new ASTElementConstructor(executableElement, parameters);
    }
}
