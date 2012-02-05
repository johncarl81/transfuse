package org.androidtransfuse.gen.variableBuilder.resource;

import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface MethodBasedResourceExpressionBuilderFactory {

    MethodBasedResourceExpressionBuilder buildMethodBasedResourceExpressionBuilder(String accessMethod, InjectionNode resourcesNode);

    AnimationResourceExpressionBuilder buildAnimationResourceExpressionBuilder(InjectionNode applicationNode);
}
