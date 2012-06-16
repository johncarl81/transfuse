package org.androidtransfuse.gen.componentBuilder;

import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface InjectionNodeFactory {

    InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor);

}
