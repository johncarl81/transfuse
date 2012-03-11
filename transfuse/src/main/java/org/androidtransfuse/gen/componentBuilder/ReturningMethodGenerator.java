package org.androidtransfuse.gen.componentBuilder;

import android.view.MotionEvent;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ReturningMethodGenerator implements MethodGenerator {
    private String methodName;
    private JType primitiveType;
    private JExpression expression;

    @Inject
    public ReturningMethodGenerator(@Assisted String methodName, @Assisted JType primitiveType, @Assisted JExpression expression) {
        this.methodName = methodName;
        this.primitiveType = primitiveType;
        this.expression = expression;
    }

    @Override
    public JMethod buildMethod(JDefinedClass definedClass) {
        JMethod method = definedClass.method(JMod.PUBLIC, primitiveType, methodName);
        method.param(MotionEvent.class, "motionEvent");
        return method;
    }

    @Override
    public void closeMethod(JMethod method) {
        if (method != null) {
            method.body()._return(expression);
        }
    }
}
