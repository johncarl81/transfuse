package org.androidtransfuse.analysis.repository;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;

/**
 * @author John Ericksen
 */
public interface PropertyBuilder {

    JExpression buildReader();

    JStatement buildWriter(JInvocation extras, String name, JVar extraParam);
}
