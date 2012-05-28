package org.androidtransfuse.gen.variableBuilder.resource;

import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface MethodBasedResourceExpressionBuilderFactory {

    MethodBasedResourceExpressionBuilder buildMethodBasedResourceExpressionBuilder(Class returnType, String accessMethod, InjectionNode resourcesNode);

    AnimationResourceExpressionBuilder buildAnimationResourceExpressionBuilder(InjectionNode applicationNode);
}
