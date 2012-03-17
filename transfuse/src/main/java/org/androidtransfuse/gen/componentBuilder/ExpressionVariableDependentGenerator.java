package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.AndroidComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface ExpressionVariableDependentGenerator {

    void generate(JDefinedClass definedClass, Map<InjectionNode, JExpression> expressionMap, AndroidComponentDescriptor descriptor, RResource rResource);
}
