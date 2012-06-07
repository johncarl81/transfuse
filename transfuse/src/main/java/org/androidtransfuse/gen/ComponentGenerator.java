package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilder;
import org.androidtransfuse.model.ComponentDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ComponentGenerator {

    private JCodeModel codeModel;
    private GeneratedClassAnnotator generatedClassAnnotator;

    @Inject
    public ComponentGenerator(JCodeModel codeModel, GeneratedClassAnnotator generatedClassAnnotator) {
        this.codeModel = codeModel;
        this.generatedClassAnnotator = generatedClassAnnotator;
    }

    public void generate(ComponentDescriptor descriptor) throws JClassAlreadyExistsException {

        final JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

        generatedClassAnnotator.annotateClass(definedClass);

        definedClass._extends(codeModel.ref(descriptor.getType()));

        for (ComponentBuilder componentBuilder : descriptor.getComponentBuilders()) {
            componentBuilder.build(definedClass, descriptor);
        }

    }
}
