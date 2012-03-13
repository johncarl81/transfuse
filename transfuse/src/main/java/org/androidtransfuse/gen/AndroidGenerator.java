package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AndroidGenerator {

    private JCodeModel codeModel;
    private GeneratedClassAnnotator generatedClassAnnotator;

    @Inject
    public AndroidGenerator(JCodeModel codeModel, GeneratedClassAnnotator generatedClassAnnotator) {
        this.codeModel = codeModel;
        this.generatedClassAnnotator = generatedClassAnnotator;
    }

    public void generate(AndroidComponentDescriptor descriptor, RResource rResource) throws JClassAlreadyExistsException {

        final JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

        generatedClassAnnotator.annotateClass(definedClass);

        codeModel.ref(descriptor.getType());

        definedClass._extends(codeModel.ref(descriptor.getType()));

        for (ComponentBuilder componentBuilder : descriptor.getComponentBuilders()) {
            componentBuilder.build(definedClass, rResource);
        }

    }
}
