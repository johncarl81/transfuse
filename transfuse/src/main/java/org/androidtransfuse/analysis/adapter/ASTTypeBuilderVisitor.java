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

    @Inject
    private ASTElementFactory astElementFactory;
    @Inject
    private ASTFactory astFactory;

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

        ASTType astType = astElementFactory.buildASTElementType((TypeElement) declaredType.asElement());

        if (declaredType.getTypeArguments().size() > 0) {
            astType = astFactory.buildGenericTypeWrapper(astType, astFactory.buildParameterBuilder(declaredType));
        }
        return astType;
    }

    @Override
    public ASTType visitError(ErrorType errorType, Void aVoid) {
        throw new TransfuseAnalysisException("Encountered ErrorType, unable to recover");
    }

    @Override
    public ASTType visitTypeVariable(TypeVariable typeVariable, Void aVoid) {
        return null;
    }

    @Override
    public ASTType visitWildcard(WildcardType wildcardType, Void aVoid) {
        throw new TransfuseAnalysisException("Encountered Wildcard Type, unable to represent in graph");
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
        return null;
    }
}
