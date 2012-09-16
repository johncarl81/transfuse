package org.androidtransfuse.analysis.adapter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static org.androidtransfuse.util.CollectionConverterUtil.transform;

/**
 * Factory class to build a specific AST tree element from the provided Element base type
 *
 * @author John Ericksen
 */
@Singleton
public class ASTElementFactory {

    private Map<TypeElement, ASTType> typeCache = new HashMap<TypeElement, ASTType>();

    @Inject
    private ASTElementConverterFactory astElementConverterFactory;
    @Inject
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;
    @Inject
    private ASTFactory astFactory;

    public ASTType buildASTElementType(DeclaredType declaredType) {

        ASTType astType = buildASTElementType((TypeElement) declaredType.asElement());

        if (declaredType.getTypeArguments().size() > 0) {
            return astFactory.buildGenericTypeWrapper(astType, astFactory.buildParameterBuilder(declaredType));
        }
        return astType;
    }

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

            List<ASTAnnotation> annotations = new ArrayList<ASTAnnotation>();

            typeCache.put(typeElement, new ASTElementType(typeElement, constructors, methods, fields, superClass, interfaces, annotations));

            //iterate and build the contained elements within this TypeElement
            constructors.addAll(transform(typeElement.getEnclosedElements(),
                    astElementConverterFactory.buildASTElementConverter(ASTConstructor.class)));

            fields.addAll(transform(typeElement.getEnclosedElements(),
                    astElementConverterFactory.buildASTElementConverter(ASTField.class)));

            methods.addAll(transform(typeElement.getEnclosedElements(),
                    astElementConverterFactory.buildASTElementConverter(ASTMethod.class)));

            annotations.addAll(buildAnnotations(typeElement));

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

        return new ASTElementField(variableElement, astTypeBuilderVisitor, modifier, buildAnnotations(variableElement));
    }

    private ASTAccessModifier buildAccessModifier(Element element) {
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
        List<ASTType> throwsTypes = buildASTElementTypes(executableElement.getThrownTypes());

        return new ASTElementMethod(executableElement, astTypeBuilderVisitor, parameters, modifier, buildAnnotations(executableElement), throwsTypes);
    }

    private List<ASTType> buildASTElementTypes(List<? extends TypeMirror> mirrorTypes) {
        List<ASTType> types = new ArrayList<ASTType>();

        for (TypeMirror mirrorType : mirrorTypes) {
            types.add(mirrorType.accept(astTypeBuilderVisitor, null));
        }

        return types;
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
        return new ASTElementParameter(variableElement, astTypeBuilderVisitor, buildAnnotations(variableElement));
    }

    /**
     * Build an ASTParameter from the input TypeParameterElement
     *
     * @param typeParameterElement required input element
     * @return ASTParameter
     */
    public ASTParameter buildASTElementParameter(TypeParameterElement typeParameterElement) {
        return new ASTElementParameter(typeParameterElement, astTypeBuilderVisitor, buildAnnotations(typeParameterElement));
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
        List<ASTType> throwsTypes = buildASTElementTypes(executableElement.getThrownTypes());

        return new ASTElementConstructor(executableElement, parameters, modifier, buildAnnotations(executableElement), throwsTypes);
    }

    private Collection<ASTAnnotation> buildAnnotations(Element element) {
        List<ASTAnnotation> annotations = new ArrayList<ASTAnnotation>();

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            ASTType type = buildASTElementType((TypeElement) annotationMirror.getAnnotationType().asElement());

            annotations.add(astFactory.buildASTElementAnnotation(annotationMirror, type));
        }

        return annotations;
    }
}
