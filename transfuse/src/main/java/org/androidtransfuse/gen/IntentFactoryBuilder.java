package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class IntentFactoryBuilder implements ComponentBuilder {

    private static final String INTENT_FACTORY_NAME = "IntentFactory";
    private JDefinedClass intentFactoryClass = null;
    private JCodeModel codeModel;

    @Inject
    public IntentFactoryBuilder(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    @Override
    public void build(JDefinedClass definedClass, AndroidComponentDescriptor descriptor, RResource rResource) {

        JDefinedClass ifClass = getIntentFactoryClass();

        //start<Component Name>
        ifClass.method(JMod.PUBLIC, codeModel.VOID, "start");
    }

    private JDefinedClass getIntentFactoryClass() {
        if (intentFactoryClass == null) {
            try {
                intentFactoryClass = codeModel._class(JMod.PUBLIC, INTENT_FACTORY_NAME, ClassType.CLASS);

                //constructor

            } catch (JClassAlreadyExistsException e) {
                throw new TransfuseAnalysisException("Class already exists while trying to build IntentFactory", e);
            }
        }
        return intentFactoryClass;
    }
}
