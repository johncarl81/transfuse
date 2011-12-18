package org.androidrobotics.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.model.InjectionNode;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface InjectionBuilderContextFactory {

    InjectionBuilderContext buildContext(Map<InjectionNode, JExpression> nodeVariableMap, JBlock block, JDefinedClass definedClass, VariableBuilderRepository variableBuilderMap);
}
