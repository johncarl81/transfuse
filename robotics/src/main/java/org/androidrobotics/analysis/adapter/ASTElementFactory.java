package org.androidrobotics.analysis.adapter;

import org.androidrobotics.util.CollectionConverterUtil;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class to build a specific AST tree element from the provided Element base type
 *
 * @author John Ericksen
 */
public class ASTElementFactory {

    private Map<TypeElement, ASTType> typeCache = new HashMap<TypeElement, ASTType>();

    @Inject
    private CollectionConverterUtil collectionConverterUtil;
    @Inject
    private ASTElementConverterFactory astElementConverterFactory;
    @Inject
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    /**
     * Build a ASTType from the provided TypeElement.
     *
     * @param typeElement required input Element
     * @return ASTType constructed using teh input Element
     */
    public ASTType buildASTElementType(TypeElement typeElement) {
        if (!typeCache.containsKey(typeElement)) {

            //build placeholder for ASTElementType and contained data structures to allow for children population
            //while avoiding back link loops
            List<ASTConstructor> constructors = new ArrayList<ASTConstructor>();
            List<ASTField> fields = new ArrayList<ASTField>();
            List<ASTMethod> methods = new ArrayList<ASTMethod>();

            typeCache.put(typeElement, new ASTElementType(typeElement, constructors, methods, fields));

            //iterate and build the contained elements within this TypeElement
            constructors.addAll(collectionConverterUtil.wrapCollection(typeElement.getEnclosedElements(),
                    astElementConverterFactory.buildASTElementConverter(ASTConstructor.class)));

            fields.addAll(collectionConverterUtil.wrapCollection(typeElement.getEnclosedElements(),
                    astElementConverterFactory.buildASTElementConverter(ASTField.class)));

            methods.addAll(collectionConverterUtil.wrapCollection(typeElement.getEnclosedElements(),
                    astElementConverterFactory.buildASTElementConverter(ASTMethod.class)));

        }

        return typeCache.get(typeElement);
    }

    /**
     * Build a ASTElementField from the given VariableElement
     *
     * @param variableElement required input Element
     * @return ASTElementField
     */
    public ASTElementField buildASTElementVariable(VariableElement variableElement) {
        ASTType type = variableElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementField(variableElement, type);
    }

    /**
     * Build an ASTMethod from the provided ExecutableElement
     *
     * @param executableElement required input element
     * @return ASTMethod
     */
    public ASTMethod buildASTElementMethod(ExecutableElement executableElement) {

        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());

        return new ASTElementMethod(executableElement, executableElement.getReturnType().accept(astTypeBuilderVisitor, null), parameters);
    }

    /**
     * Build a list of ASTParameters corresponding tot he input VariableElement list elements
     *
     * @param variableElements required input element
     * @return list of ASTParameters
     */
    private List<ASTParameter> buildASTElementParameters(List<? extends VariableElement> variableElements) {
        List<ASTParameter> astParameters = new ArrayList<ASTParameter>();

        for (VariableElement variables : variableElements) {
            astParameters.add(buildASTElementParameter(variables));
        }

        return astParameters;
    }

    /**
     * Build an ASTParameter from the input VariableElement
     *
     * @param variableElement required input element
     * @return ASTParameter
     */
    private ASTParameter buildASTElementParameter(VariableElement variableElement) {
        ASTType type = variableElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementParameter(variableElement, type);
    }

    /**
     * Build an ASTParameter from the input TypeParameterElement
     *
     * @param typeParameterElement required input element
     * @return ASTParameter
     */
    public ASTParameter buildASTElementParameter(TypeParameterElement typeParameterElement) {
        ASTType type = typeParameterElement.asType().accept(astTypeBuilderVisitor, null);
        return new ASTElementParameter(typeParameterElement, type);
    }

    /**
     * Build an ASTConstructor from the input ExecutableElement
     *
     * @param executableElement require input element
     * @return ASTConstructor
     */
    public ASTConstructor buildASTElementConstructor(ExecutableElement executableElement) {
        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());
        return new ASTElementConstructor(executableElement, parameters);
    }
}
