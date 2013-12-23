package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;

/**
 * @author John Ericksen
 */
public interface InstantiationStrategy {

    JExpression instantiate(JDefinedClass providerClass);

}
