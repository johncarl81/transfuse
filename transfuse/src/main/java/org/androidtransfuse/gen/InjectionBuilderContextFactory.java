package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface InjectionBuilderContextFactory {

    InjectionBuilderContext buildContext(Map<InjectionNode, JExpression> nodeVariableMap, JBlock block, JDefinedClass definedClass, RResource rResource);
}
