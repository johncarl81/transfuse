package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BindProvidersProcessor implements TypeProcessor {

    private final BindProviderProcessor bindProviderProcessor;

    @Inject
    public BindProvidersProcessor(BindProviderProcessor bindProviderProcessor) {
        this.bindProviderProcessor = bindProviderProcessor;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation typeAnnotation) {

        ASTAnnotation[] values = typeAnnotation.getProperty("value", ASTAnnotation[].class);

        ModuleConfigurationComposite configurations = new ModuleConfigurationComposite();

        for (ASTAnnotation interceptorBinding : values) {
            configurations.add(bindProviderProcessor.process(interceptorBinding));
        }

        return configurations;
    }
}
