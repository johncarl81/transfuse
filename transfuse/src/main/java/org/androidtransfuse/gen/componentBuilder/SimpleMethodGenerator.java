package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;

import javax.inject.Inject;

public class SimpleMethodGenerator implements MethodGenerator {
    private String methodName;
    private JCodeModel codeModel;

    @Inject
    public SimpleMethodGenerator(@Assisted String methodName, JCodeModel codeModel) {
        this.methodName = methodName;
        this.codeModel = codeModel;
    }

    @Override
    public JMethod buildMethod(JDefinedClass definedClass) {
        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.VOID, methodName);
        JBlock body = method.body();
        body.add(JExpr._super().invoke(methodName));
        return method;
    }

    @Override
    public void closeMethod(JMethod method) {
        //noop
    }
}