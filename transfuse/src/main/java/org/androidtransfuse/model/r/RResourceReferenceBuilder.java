package org.androidtransfuse.model.r;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class RResourceReferenceBuilder {

    private JCodeModel codeModel;

    @Inject
    public RResourceReferenceBuilder(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public JExpression buildReference(ResourceIdentifier viewResourceIdentifier) {
        ASTType rInnerType = viewResourceIdentifier.getRInnerType();

        JClass rInnerRef = codeModel.ref(rInnerType.getName());

        return rInnerRef.staticRef(viewResourceIdentifier.getName());
    }
}
