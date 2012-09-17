package org.androidtransfuse.gen.componentBuilder;

import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;

/**
 * @author John Ericksen
 */
public interface InjectionNodeFactory {

    InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor);

}
