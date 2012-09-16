package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

public class MirroredMethodGenerator implements MethodGenerator {
    private final ASTMethod overrideMethod;
    private final boolean superCall;
    private final JCodeModel codeModel;
    private final UniqueVariableNamer variableNamer;

    @Inject
    public MirroredMethodGenerator(@Assisted ASTMethod overrideMethod, @Assisted boolean superCall, JCodeModel codeModel, UniqueVariableNamer variableNamer) {
        this.overrideMethod = overrideMethod;
        this.superCall = superCall;
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.ref(overrideMethod.getReturnType().getName()), overrideMethod.getName());

        MethodDescriptor methodDescriptor = new MethodDescriptor(method, overrideMethod);

        //parameters
        for (ASTParameter astParameter : overrideMethod.getParameters()) {
            JVar param = method.param(codeModel.ref(astParameter.getASTType().getName()), variableNamer.generateName(astParameter.getASTType()));
            methodDescriptor.putParameter(astParameter, new TypedExpression(astParameter.getASTType(), param));
        }

        if (superCall) {
            JBlock body = method.body();
            JInvocation superInvocation = JExpr._super().invoke(overrideMethod.getName());
            body.add(superInvocation);

            for (ASTParameter astParameter : overrideMethod.getParameters()) {
                superInvocation.arg(methodDescriptor.getParameter(astParameter).getExpression());
            }
        }
        return methodDescriptor;
    }

    @Override
    public void closeMethod(MethodDescriptor methodDescriptor) {
        //noop
    }
}