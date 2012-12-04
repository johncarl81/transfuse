package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class GeneratorRepository {

    private final ImmutableMap<Class<? extends Annotation>, TransactionProcessorBuilder<Provider<ASTType>, ?>> componentBuilders;
    private final TransactionProcessor processor;

    public GeneratorRepository(ImmutableMap<Class<? extends Annotation>, TransactionProcessorBuilder<Provider<ASTType>, ?>> componentBuilders, TransactionProcessor processor) {
        this.componentBuilders = componentBuilders;
        this.processor = processor;
    }

    public TransactionProcessorBuilder<Provider<ASTType>, ?> getComponentBuilder(Class<? extends Annotation> componentAnnotation) {
        return componentBuilders.get(componentAnnotation);
    }

    public Map<Class<? extends Annotation>, TransactionProcessorBuilder<Provider<ASTType>, ?>> getComponentBuilders() {
        return componentBuilders;
    }

    public TransactionProcessor getProcessor() {
        return processor;
    }
}
