package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;

/**
 * @author John Ericksen
 */
public class ProtectedAccessorMethod {

    private final JDefinedClass definedClass;
    private final JMethod method;

    public ProtectedAccessorMethod(JDefinedClass definedClass, JMethod method) {
        this.definedClass = definedClass;
        this.method = method;
    }

    public JInvocation invoke() {
        return definedClass.staticInvoke(method);
    }
}
