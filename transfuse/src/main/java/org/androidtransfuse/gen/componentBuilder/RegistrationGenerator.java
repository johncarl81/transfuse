package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public interface RegistrationGenerator {

    void build(JDefinedClass definedClass, JBlock block, TypedExpression value);
}
