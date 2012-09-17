package org.androidtransfuse.analysis.adapter;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class to build a specific AST tree element from the provided Element base type
 *
 * @author John Ericksen
 */
@Singleton
public class ASTElementFactory {

    private final Map<TypeElement, ASTType> typeCache = new HashMap<TypeElement, ASTType>();

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
            ImmutableList.Builder<ASTConstructor> constructors = ImmutableList.builder();
            ImmutableList.Builder<ASTField> fields = ImmutableList.builder();
            ImmutableList.Builder<ASTMethod> methods = ImmutableList.builder();

            ASTType superClass = null;
            if (typeElement.getSuperclass() != null) {
                superClass = typeElement.getSuperclass().accept(astTypeBuilderVisitor, null);
            }

            ImmutableList<ASTType> interfaces = FluentIterable.from(typeElement.getInterfaces())
                    .transform(astTypeBuilderVisitor)
                    .toImmutableList();

            ImmutableList.Builder<ASTAnnotation> annotations =  ImmutableList.builder();

            ASTTypeVirtualProxy astTypeProxy = new ASTTypeVirtualProxy(typeElement.getSimpleName().toString());
            typeCache.put(typeElement, astTypeProxy );

            //iterate and build the contained elements within this TypeElement
            constructors.addAll(transformAST(typeElement.getEnclosedElements(), ASTConstructor.class));
            fields.addAll(transformAST(typeElement.getEnclosedElements(), ASTField.class));
            methods.addAll(transformAST(typeElement.getEnclosedElements(), ASTMethod.class));

            annotations.addAll(buildAnnotations(typeElement));

            ASTType astType = new ASTElementType(typeElement,
                    constructors.build(),
                    methods.build(),
                    fields.build(),
                    superClass,
                    interfaces,
                    annotations.build());

            astTypeProxy.load(astType);
            typeCache.put(typeElement, astType);
        }

        return typeCache.get(typeElement);
    }

    private <T extends ASTBase>  List<T> transformAST(List<? extends Element> enclosedElements, Class<T> astType){
        return FluentIterable
                .from(enclosedElements)
                .transform(astElementConverterFactory.buildASTElementConverter(astType))
                .filter(Predicates.notNull())
                .toImmutableList();
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

        ImmutableList<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());
        ASTAccessModifier modifier = buildAccessModifier(executableElement);
        ImmutableList<ASTType> throwsTypes = buildASTElementTypes(executableElement.getThrownTypes());

        return new ASTElementMethod(executableElement, astTypeBuilderVisitor, parameters, modifier, buildAnnotations(executableElement), throwsTypes);
    }

    private ImmutableList<ASTType> buildASTElementTypes(List<? extends TypeMirror> mirrorTypes) {
        return FluentIterable.from(mirrorTypes)
                .transform(astTypeBuilderVisitor)
                .toImmutableList();
    }

    /**
     * Build a list of ASTParameters corresponding tot he input VariableElement list elements
     *
     * @param variableElements required input element
     * @return list of ASTParameters
     */
    private ImmutableList<ASTParameter> buildASTElementParameters(List<? extends VariableElement> variableElements) {
        ImmutableList.Builder<ASTParameter> astParameterBuilder = ImmutableList.builder();

        for (VariableElement variables : variableElements) {
            astParameterBuilder.add(buildASTElementParameter(variables));
        }

        return astParameterBuilder.build();
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
        ImmutableList<ASTParameter> parameters = buildASTElementParameters(executableElement.getParameters());
        ASTAccessModifier modifier = buildAccessModifier(executableElement);
        ImmutableList<ASTType> throwsTypes = buildASTElementTypes(executableElement.getThrownTypes());

        return new ASTElementConstructor(executableElement, parameters, modifier, buildAnnotations(executableElement), throwsTypes);
    }

    private ImmutableCollection<ASTAnnotation> buildAnnotations(Element element) {
        ImmutableList.Builder<ASTAnnotation> annotationBuilder = ImmutableList.builder();

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            ASTType type = buildASTElementType((TypeElement) annotationMirror.getAnnotationType().asElement());

            annotationBuilder.add(astFactory.buildASTElementAnnotation(annotationMirror, type));
        }

        return annotationBuilder.build();
    }
}
