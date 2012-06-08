package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface InjectionBuilderContextFactory {

    InjectionBuilderContext buildContext(Map<InjectionNode, TypedExpression> nodeVariableMap, JBlock block, JDefinedClass definedClass);
}
