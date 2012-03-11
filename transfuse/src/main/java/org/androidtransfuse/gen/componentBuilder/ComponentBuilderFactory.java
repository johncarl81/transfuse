package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface ComponentBuilderFactory {

    OnCreateComponentBuilder buildOnCreateComponentBuilder(InjectionNode injectionNode, LayoutBuilder layoutBuilder, Class<?>... methodParameters);

    MethodCallbackGeneratorImpl buildMethodCallbackGenerator(String eventName, MethodGenerator methodGenerator, Class<?>... parameterTypes);

    RLayoutBuilder buildRLayoutBuilder(Integer layout);

    SimpleMethodGenerator buildSimpleMethodGenerator(String methodName);

    ReturningMethodGenerator buildReturningMethodGenerator(String methodName, JType primitiveType, JExpression expression);
}
