package org.androidtransfuse.gen.variableBuilder.resource;

/**
 * @author John Ericksen
 */
public interface MethodBasedResourceExpressionBuilderAdaptorFactory {

    MethodBasedResourceExpressionBuilderAdaptor buildMethodBasedResourceExpressionBuilderAdaptor(Class clazz, String accessMethod);

    AnimationResourceExpressionBuilderAdaptor buildAnimationResourceExpressionBuilderAdaptor();
}
