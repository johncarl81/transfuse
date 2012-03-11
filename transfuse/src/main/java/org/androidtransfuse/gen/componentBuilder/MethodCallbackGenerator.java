package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.InjectionNode;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface MethodCallbackGenerator {

    void addLifecycleMethod(JDefinedClass definedClass, Map<InjectionNode, JExpression> expressionMap);
}
