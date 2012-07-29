package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public interface ViewRegistrationInvocationBuilder {

    void buildInvocation(JBlock block, TypedExpression expression, JExpression viewExpression, String method, InjectionNode injectionNode);
}
