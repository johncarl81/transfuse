package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.gen.ComponentDescriptor;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface ExpressionVariableDependentGenerator {

    void generate(JDefinedClass definedClass, JBlock block, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor, RResource rResource);
}
