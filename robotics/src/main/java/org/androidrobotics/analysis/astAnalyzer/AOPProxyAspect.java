package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class AOPProxyAspect {

    private Map<ASTMethod, ASTType> methodInterceptors = new HashMap<ASTMethod, ASTType>();

    public Map<ASTMethod, ASTType> getMethodInterceptors() {
        return methodInterceptors;
    }
}
