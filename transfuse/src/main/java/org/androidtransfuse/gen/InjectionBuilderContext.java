package org.androidtransfuse.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionBuilderContext {

    private final Map<InjectionNode, TypedExpression> variableMap = new HashMap<InjectionNode, TypedExpression>();
    private final JBlock block;
    private final JDefinedClass definedClass;
    private final List<InjectionNode> proxyLoad = new ArrayList<InjectionNode>();

    @Inject
    public InjectionBuilderContext(@Assisted JBlock block,
                                   @Assisted JDefinedClass definedClass) {
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

    public List<InjectionNode> getProxyLoad() {
        return proxyLoad;
    }
}
