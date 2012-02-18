package org.androidtransfuse.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.proxy.VirtualProxyGenerator;
import org.androidtransfuse.gen.variableBuilder.ProxyVariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.ProxyDescriptor;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionBuilderContext {

    private Map<InjectionNode, JExpression> variableMap;
    private JBlock block;
    private JDefinedClass definedClass;
    private RResource rResource;

    @Inject
    public InjectionBuilderContext(@Assisted Map<InjectionNode, JExpression> variableMap,
                                   @Assisted JBlock block,
                                   @Assisted JDefinedClass definedClass,
                                   @Assisted RResource rResource) {
        this.variableMap = variableMap;
        this.block = block;
        this.definedClass = definedClass;
        this.rResource = rResource;
    }

    public Map<InjectionNode, JExpression> getVariableMap() {
        return variableMap;
    }

    public JBlock getBlock() {
        return block;
    }

    public JDefinedClass getDefinedClass() {
        return definedClass;
    }

    public RResource getRResource() {
        return rResource;
    }
}
