package org.androidrobotics.gen;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

/**
 * @author John Ericksen
 */
public interface DelegateInstantiationGeneratorStrategy {

    JFieldVar addDelegateInstantiation(JDefinedClass definedClass);
}
