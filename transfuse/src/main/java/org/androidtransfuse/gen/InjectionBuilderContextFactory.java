package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public interface InjectionBuilderContextFactory {

    InjectionBuilderContext buildContext(JBlock block, JDefinedClass definedClass);
}
