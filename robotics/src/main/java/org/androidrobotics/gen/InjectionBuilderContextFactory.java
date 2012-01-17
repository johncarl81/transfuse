package org.androidrobotics.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.r.RResource;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface InjectionBuilderContextFactory {

    InjectionBuilderContext buildContext(Map<InjectionNode, JExpression> nodeVariableMap, JBlock block, JDefinedClass definedClass, RResource rResource);
}
