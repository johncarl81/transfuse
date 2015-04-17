package org.androidtransfuse.analysis.repository;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;

/**
 * @author John Ericksen
 */
public class SimplePropertyBuilder implements PropertyBuilder{

    private final String accessor;
    private final String mutator;

    public SimplePropertyBuilder(String accessor, String mutator) {
        this.accessor = accessor;
        this.mutator = mutator;
    }

    @Override
    public JExpression buildReader() {
        return null;
    }

    @Override
    public JStatement buildWriter(JInvocation extras, String name, JVar extraParam) {
        return extras.invoke(mutator).arg(name).arg(extraParam);
    }
}
