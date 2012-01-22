package org.androidrobotics.analysis.adapter;

import org.androidrobotics.analysis.RoboticsAnalysisException;

import javax.inject.Inject;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * Builder of an ASTType from a TypeMirror input
 *
 * @author John Ericksen
 */
public class ASTTypeBuilderVisitor extends SimpleTypeVisitor6<ASTType, Void> {

    @Inject
    private ASTElementFactory astElementFactory;

    @Override
    public ASTType visitPrimitive(PrimitiveType primitiveType, Void aVoid) {
        return ASTPrimitiveType.valueOf(primitiveType.getKind().name());
    }

    @Override
    public ASTType visitNull(NullType nullType, Void aVoid) {
        return null;
    }

    @Override
    public ASTType visitArray(ArrayType arrayType, Void aVoid) {
        return new ASTArrayType(arrayType.getComponentType().accept(this, null));
    }

    @Override
    public ASTType visitDeclared(DeclaredType declaredType, Void aVoid) {
        return astElementFactory.buildASTElementType((TypeElement) declaredType.asElement());
    }

    @Override
    public ASTType visitError(ErrorType errorType, Void aVoid) {
        throw new RoboticsAnalysisException("Encountered ErrorType, unable to recover");
    }

    @Override
    public ASTType visitTypeVariable(TypeVariable typeVariable, Void aVoid) {
        System.out.println("TypeVariable:" + typeVariable.toString());
        return null;
    }

    @Override
    public ASTType visitWildcard(WildcardType wildcardType, Void aVoid) {
        throw new RoboticsAnalysisException("Encountered Wildcard Type, unable to represent in graph");
    }

    @Override
    public ASTType visitExecutable(ExecutableType executableType, Void aVoid) {
        return astElementFactory.buildASTElementType((TypeElement) executableType);
    }

    @Override
    public ASTType visitNoType(NoType noType, Void aVoid) {
        if (noType.getKind().equals(TypeKind.VOID)) {
            return ASTVoidType.VOID;
        }
        return null;
    }

    @Override
    public ASTType visitUnknown(TypeMirror typeMirror, Void aVoid) {
        System.out.println("type Mirror:" + typeMirror.toString());
        return null;
    }
}
