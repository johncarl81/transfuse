package org.androidrobotics.gen.proxy;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface DelegateInstantiationGeneratorStrategy {

    JFieldVar addDelegateInstantiation(JDefinedClass definedClass, InjectionNode delegateNode);
}
