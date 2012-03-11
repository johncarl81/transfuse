package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class MethodDescriptor {

    private JMethod method;
    private Map<ASTParameter, JExpression> parameterMap = new HashMap<ASTParameter, JExpression>();
    private ASTMethod astMethod;

    public MethodDescriptor(JMethod method, ASTMethod astMethod) {
        this.method = method;
        this.astMethod = astMethod;
    }

    public JMethod getMethod() {
        return method;
    }

    public JExpression getParameter(ASTParameter astParameter) {
        return parameterMap.get(astParameter);
    }

    public void putParameter(ASTParameter astParameter, JVar param) {
        parameterMap.put(astParameter, param);
    }

    public ASTMethod getASTMethod() {
        return astMethod;
    }
}
