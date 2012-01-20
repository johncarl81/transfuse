package org.androidrobotics.analysis.adapter;

import org.androidrobotics.util.CollectionConverter;
import org.androidrobotics.util.ElementVisitorAdaptor;

import javax.lang.model.element.*;

/**
 * Element to AST converter, converts the given type of javax.lang.model.element.Element to the
 * robotics internal AST representation.
 *
 * @author John Ericksen
 */
public class ASTElementConverter<T> implements CollectionConverter<Element, T> {

    private static final String CONSTRUCTOR_IDENTIFIER = "<init>";

    private Class<T> astTypeClass;
    private ASTElementFactory astElementFactory;

    public ASTElementConverter(Class<T> astTypeClass, ASTElementFactory astElementFactory) {
        this.astTypeClass = astTypeClass;
        this.astElementFactory = astElementFactory;
    }

    @Override
    public T convert(Element element) {
        //visit the given element to determine its type, feed it into the appropriate
        //ASTElementFactory method and return the result
        return element.accept(new ASTTypeElementConverterAdaptor(), null);
    }

    private final class ASTTypeElementConverterAdaptor extends ElementVisitorAdaptor<T, Void> {
        @Override
        public T visitType(TypeElement typeElement, Void aVoid) {
            if (astTypeClass.isAssignableFrom(ASTElementType.class)) {
                return (T) astElementFactory.buildASTElementType(typeElement);
            }
            return null;
        }

        @Override
        public T visitVariable(VariableElement variableElement, Void aVoid) {
            if (astTypeClass.isAssignableFrom(ASTElementField.class)) {
                return (T) astElementFactory.buildASTElementVariable(variableElement);
            }
            return null;
        }

        @Override
        public T visitExecutable(ExecutableElement executableElement, Void aVoid) {
            //constructors and methods share this Element, the indication that the method is a constructor
            //is that it is named <init>
            if (executableElement.getSimpleName().contentEquals(CONSTRUCTOR_IDENTIFIER)) {
                if (astTypeClass.isAssignableFrom(ASTConstructor.class)) {
                    return (T) astElementFactory.buildASTElementConstructor(executableElement);
                }
            } else if (astTypeClass.isAssignableFrom(ASTMethod.class)) {
                return (T) astElementFactory.buildASTElementMethod(executableElement);
            }
            return null;
        }

        @Override
        public T visitTypeParameter(TypeParameterElement typeParameterElement, Void aVoid) {
            if (astTypeClass.isAssignableFrom(ASTParameter.class)) {
                return (T) astElementFactory.buildASTElementParameter(typeParameterElement);
            }
            return null;
        }
    }
}
