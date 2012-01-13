package org.androidrobotics.gen.variableBuilder.resource;

import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface MethodBasedResourceExpressionBuilderFactory {

    MethodBasedResourceExpressionBuilder buildMethodBasedResourceExpressionBuilder(String accessMethod, InjectionNode resourcesNode);

    AnimationResourceExpressionBuilder buildAnimationResourceExpressionBuilder(InjectionNode applicationNode);
}
