package org.androidrobotics.analysis.adapter;

import org.androidrobotics.util.CollectionConverterUtil;

import javax.inject.Inject;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;

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

            ASTType superClass = null;
            if (typeElement.getSuperclass() != null) {
                superClass = typeElement.getSuperclass().accept(astTypeBuilderVisitor, null);
            }

            Collection<ASTType> interfaces = new HashSet<ASTType>();

            for (TypeMirror interfaceTypeMirror : typeElement.getInterfaces()) {
                ASTType interfaceType = interfaceTypeMirror.accept(astTypeBuilderVisitor, null);
                interfaces.add(interfaceType);
            }

            typeCache.put(typeElement, new ASTElementType(typeElement, constructors, methods, fields, superClass, interfaces));

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
        ASTAccessModifier modifier = buildAccessModifier(variableElement);

        return new ASTElementField(variableElement, astTypeBuilderVisitor, modifier);
    }

    private ASTAccessModifier buildAccessModifier(Element element) {
        ASTAccessModifier modifier;

        for (Modifier elementModifier : element.getModifiers()) {
            switch (elementModifier) {
                case PUBLIC:
                    return ASTAccessModifier.PUBLIC;
                case PROTECTED:
                    return ASTAccessModifier.PROTECTED;
                case PRIVATE:
                    return ASTAccessModifier.PRIVATE;
            }
        }

        return ASTAccessModifier.PACKAGE_PRIVATE;
    }

    /**
     * Build an ASTMethod from the provided ExecutableElement
     *
     * @param executableElement required input element
     * @return ASTMethod
     */
    public ASTMethod buildASTElementMethod(ExecutableElement executableElement) {

        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());
        ASTAccessModifier modifier = buildAccessModifier(executableElement);

        return new ASTElementMethod(executableElement, astTypeBuilderVisitor, parameters, modifier);
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
        return new ASTElementParameter(variableElement, astTypeBuilderVisitor);
    }

    /**
     * Build an ASTParameter from the input TypeParameterElement
     *
     * @param typeParameterElement required input element
     * @return ASTParameter
     */
    public ASTParameter buildASTElementParameter(TypeParameterElement typeParameterElement) {
        return new ASTElementParameter(typeParameterElement, astTypeBuilderVisitor);
    }

    /**
     * Build an ASTConstructor from the input ExecutableElement
     *
     * @param executableElement require input element
     * @return ASTConstructor
     */
    public ASTConstructor buildASTElementConstructor(ExecutableElement executableElement) {
        List<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());
        ASTAccessModifier modifier = buildAccessModifier(executableElement);
        return new ASTElementConstructor(executableElement, parameters, modifier);
    }
}
