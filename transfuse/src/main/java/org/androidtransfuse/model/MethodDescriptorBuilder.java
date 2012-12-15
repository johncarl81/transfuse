package org.androidtransfuse.model;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JMethod;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class MethodDescriptorBuilder {

    private final JMethod codeModelMethod;
    private final ASTMethod astMethod;
    private final ImmutableMap.Builder<ASTType, TypedExpression> typeMapBuilder = ImmutableMap.builder();
    private final ImmutableMap.Builder<ASTParameter, TypedExpression> parameterMapBuilder = ImmutableMap.builder();


    public MethodDescriptorBuilder(JMethod codeModelMethod, ASTMethod astMethod) {
        this.codeModelMethod = codeModelMethod;
        this.astMethod = astMethod;
    }

    public void putType(ASTType astType, TypedExpression expression) {
        typeMapBuilder.put(astType, expression);
    }

    public void putParameter(ASTParameter astParameter, TypedExpression expression) {
        parameterMapBuilder.put(astParameter, expression);
        typeMapBuilder.put(astParameter.getASTType(), expression);
    }

    public MethodDescriptor build() {
        return new MethodDescriptor(codeModelMethod, astMethod, parameterMapBuilder.build(), typeMapBuilder.build());
    }
}
