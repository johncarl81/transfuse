package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnnotationProcessor;
import org.androidtransfuse.model.r.RResource;

import javax.annotation.Generated;
import javax.inject.Inject;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author John Ericksen
 */
public class AndroidGenerator {

    private JCodeModel codeModel;

    @Inject
    public AndroidGenerator(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public void generate(AndroidComponentDescriptor descriptor, RResource rResource) throws JClassAlreadyExistsException {

        final JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

        definedClass.annotate(Generated.class)
                .param("value", TransfuseAnnotationProcessor.class.getName())
                .param("date", DateFormat.getInstance().format(new Date()));

        codeModel.ref(descriptor.getType());

        definedClass._extends(codeModel.ref(descriptor.getType()));

        for (ComponentBuilder componentBuilder : descriptor.getComponentBuilders()) {
            componentBuilder.build(definedClass, rResource);
        }

    }
}
