package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.annotations.Factory;

/**
 * @author John Ericksen
 */
@Factory
public interface InstantiationStrategyFactory {

    FieldInstantiationStrategy buildFieldStrategy(JDefinedClass definedClass, JBlock constructorBlock, JExpression scopes);

    MethodInstantiationStrategy buildMethodStrategy(JDefinedClass definedClass, JBlock block, JExpression scopes);
}
