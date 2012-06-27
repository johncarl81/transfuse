package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.analysis.TransfuseAnalysisException;

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

    private ASTElementFactory astElementFactory;

    @Override
    public ASTType visitPrimitive(PrimitiveType primitiveType, Void aVoid) {
        return ASTPrimitiveType.valueOf(primitiveType.getKind().name());
    }

    @Override
    public ASTType visitNull(NullType nullType, Void aVoid) {
        throw new TransfuseAnalysisException("Encountered NullType, unable to recover");
    }

    @Override
    public ASTType visitArray(ArrayType arrayType, Void aVoid) {
        return new ASTArrayType(arrayType.getComponentType().accept(this, null));
    }

    @Override
    public ASTType visitDeclared(DeclaredType declaredType, Void aVoid) {
        return astElementFactory.buildASTElementType(declaredType);
    }

    @Override
    public ASTType visitError(ErrorType errorType, Void aVoid) {
        throw new TransfuseAnalysisException("Encountered ErrorType, unable to recover");
    }

    @Override
    public ASTType visitTypeVariable(TypeVariable typeVariable, Void aVoid) {
        throw new TransfuseAnalysisException("Encountered TypeVariable, unable to recover");
    }

    @Override
    public ASTType visitWildcard(WildcardType wildcardType, Void aVoid) {
        throw new TransfuseAnalysisException("Encountered Wildcard Type, unable to represent in graph");
    }

    @Override
    public ASTType visitExecutable(ExecutableType executableType, Void aVoid) {
        if (executableType instanceof TypeElement) {
            return astElementFactory.buildASTElementType((TypeElement) executableType);
        } else {
            throw new TransfuseAnalysisException("Encountered non-TypeElement");
        }
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
        throw new TransfuseAnalysisException("Encountered unknown TypeMirror, unable to recover");
    }

    @Inject
    public void setAstElementFactory(ASTElementFactory astElementFactory) {
        this.astElementFactory = astElementFactory;
    }
}
