package org.androidtransfuse.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.invocationBuilder.TypeInvocationHelper;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ExpressionMatchingIterable implements Iterable<JExpression> {

    private List<JExpression> matchedExpressions = new ArrayList<JExpression>();

    @Inject
    public ExpressionMatchingIterable(TypeInvocationHelper invocationHelper, @Assisted Map<InjectionNode, TypedExpression> variableMap, @Assisted List<InjectionNode> keys) {
        for (InjectionNode key : keys) {
            matchedExpressions.add(invocationHelper.getExpression(key.getASTType(), variableMap, key));
        }
    }

    @Override
    public Iterator<JExpression> iterator() {
        return matchedExpressions.iterator();
    }
}
