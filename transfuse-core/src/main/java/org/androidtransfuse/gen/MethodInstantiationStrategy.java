package org.androidtransfuse.gen;

import com.sun.codemodel.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class MethodInstantiationStrategy implements InstantiationStrategy {

    private final Map<JDefinedClass, JExpression> variables = new HashMap<JDefinedClass, JExpression>();
    private final JExpression scopesVar;
    private final UniqueVariableNamer namer;
    private final JDefinedClass definedClass;
    private final JBlock block;

    @Inject
    public MethodInstantiationStrategy(
            /*@Assisted*/ JDefinedClass definedClass,
            /*@Assisted*/ JBlock block,
            /*@Assisted*/ JExpression scopes,
            UniqueVariableNamer namer

    ) {
        this.definedClass = definedClass;
        this.block = block;
        this.scopesVar = scopes;
        this.namer = namer;
    }

    @Override
    public JExpression instantiate(JDefinedClass providerClass) {
        if(!variables.containsKey(providerClass)){
            JVar variable = block.decl(providerClass, namer.generateName(providerClass));
            block.assign(variable, JExpr._new(providerClass).arg(scopesVar));
            variables.put(providerClass, variable);
        }
        return variables.get(providerClass);
    }
}
