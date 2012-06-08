package org.androidtransfuse.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionBuilderContext {

    private Map<InjectionNode, TypedExpression> variableMap;
    private JBlock block;
    private JDefinedClass definedClass;

    @Inject
    public InjectionBuilderContext(@Assisted Map<InjectionNode, TypedExpression> variableMap,
                                   @Assisted JBlock block,
                                   @Assisted JDefinedClass definedClass) {
        this.variableMap = variableMap;
        this.block = block;
        this.definedClass = definedClass;
    }

    public Map<InjectionNode, TypedExpression> getVariableMap() {
        return variableMap;
    }

    public JBlock getBlock() {
        return block;
    }

    public JDefinedClass getDefinedClass() {
        return definedClass;
    }
}
