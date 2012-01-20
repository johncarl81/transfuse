package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;

/**
 * Builder of an ASTType from a TypeMirror input
 *
 * @author John Ericksen
 */
public class ASTTypeBuilderVisitor implements TypeVisitor<ASTType, Void> {

    @Inject
    private ASTElementFactory astElementFactory;

    @Override
    public ASTType visit(TypeMirror typeMirror, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visit(TypeMirror typeMirror) {
        return null;  //todo
    }

    @Override
    public ASTType visitPrimitive(PrimitiveType primitiveType, Void aVoid) {
        String name = primitiveType.getKind().name().toLowerCase();
        return new ASTPrimitiveType(name);
    }

    @Override
    public ASTType visitNull(NullType nullType, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visitArray(ArrayType arrayType, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visitDeclared(DeclaredType declaredType, Void aVoid) {
        return astElementFactory.buildASTElementType((TypeElement) declaredType.asElement());
    }

    @Override
    public ASTType visitError(ErrorType errorType, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visitTypeVariable(TypeVariable typeVariable, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visitWildcard(WildcardType wildcardType, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visitExecutable(ExecutableType executableType, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visitNoType(NoType noType, Void aVoid) {
        return null;  //todo
    }

    @Override
    public ASTType visitUnknown(TypeMirror typeMirror, Void aVoid) {
        return null;  //todo
    }
}
