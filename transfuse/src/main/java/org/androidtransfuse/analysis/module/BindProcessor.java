package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;

/**
 * Associates the given return type with the annotated field as a binding.
 *
 * @author John Ericksen
 */
public class BindProcessor implements TypeProcessor {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public BindProcessor(VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionNodeBuilderRepositoryFactory injectionNodeBuilders) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation bindAnnotation) {
        ASTType type = bindAnnotation.getProperty("type", ASTType.class);
        ASTType to = bindAnnotation.getProperty("to", ASTType.class);

        return new BindingModuleConfiguration(type, to);
    }

    private final class BindingModuleConfiguration implements ModuleConfiguration{

        private final ASTType type;
        private final ASTType to;

        private BindingModuleConfiguration(ASTType type, ASTType to) {
            this.type = type;
            this.to = to;
        }

        @Override
        public void setConfiguration() {
            injectionNodeBuilders.putModuleConfig(type,
                    variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(to));
        }
    }
}