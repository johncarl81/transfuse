package org.androidtransfuse.analysis.repository;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.IntentFactoryStrategyGenerator;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ParcelerPropertyBuilder implements PropertyBuilder{

    private final ClassGenerationUtil generationUtil;

    @Inject
    public ParcelerPropertyBuilder(ClassGenerationUtil generationUtil) {
        this.generationUtil = generationUtil;
    }

    @Override
    public JExpression buildReader() {
        return null;
    }

    @Override
    public JStatement buildWriter(JInvocation extras, String name, JVar extraParam) {
        JInvocation wrappedParcel = generationUtil.ref(IntentFactoryStrategyGenerator.PARCELS_NAME)
                .staticInvoke(IntentFactoryStrategyGenerator.WRAP_METHOD).arg(extraParam);

        return extras.invoke("putParcelable").arg(name).arg(wrappedParcel);
    }
}
