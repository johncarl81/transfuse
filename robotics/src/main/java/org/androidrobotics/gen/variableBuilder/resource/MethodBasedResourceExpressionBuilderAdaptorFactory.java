package org.androidrobotics.gen.variableBuilder.resource;

/**
 * @author John Ericksen
 */
public interface MethodBasedResourceExpressionBuilderAdaptorFactory {

    MethodBasedResourceExpressionBuilderAdaptor buildMethodBasedResourceExpressionBuilderAdaptor(String accessMethod);

    AnimationResourceExpressionBuilderAdaptor buildAnimationResourceExpressionBuilderAdaptor();
}
