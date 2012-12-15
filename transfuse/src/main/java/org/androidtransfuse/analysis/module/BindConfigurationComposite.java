package org.androidtransfuse.analysis.module;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BindConfigurationComposite implements TypeProcessor {

    private final TypeProcessor processor;

    @Inject
    public BindConfigurationComposite(@Assisted TypeProcessor processor) {
        this.processor = processor;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation typeAnnotation) {

        ASTAnnotation[] values = typeAnnotation.getProperty("value", ASTAnnotation[].class);

        ModuleConfigurationComposite configurations = new ModuleConfigurationComposite();

        for (ASTAnnotation interceptorBinding : values) {
            configurations.add(processor.process(interceptorBinding));
        }

        return configurations;
    }
}
