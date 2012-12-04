package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.model.PackageClass;

/**
 * @author John Ericksen
 */
public class ProtectedAccessorMethod {

    private final PackageClass helperClass;
    private final String method;

    public ProtectedAccessorMethod(PackageClass helperClass, String method) {
        this.helperClass = helperClass;
        this.method = method;
    }

    public JInvocation invoke(JCodeModel codeModel) {
        return codeModel.ref(helperClass.getFullyQualifiedName()).staticInvoke(method);
    }
}
