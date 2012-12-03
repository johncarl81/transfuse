package org.androidtransfuse.analysis.repository;

import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.processor.TransactionProcessor;
import org.androidtransfuse.processor.TransactionProcessorBuilder;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class GeneratorRepository {

    private final ImmutableMap<Class<? extends Annotation>, TransactionProcessorBuilder> componentBuilders;
    private final TransactionProcessor processor;

    public GeneratorRepository(ImmutableMap<Class<? extends Annotation>, TransactionProcessorBuilder> componentBuilders, TransactionProcessor processor) {
        this.componentBuilders = componentBuilders;
        this.processor = processor;
    }

    public TransactionProcessorBuilder getComponentBuilder(Class<? extends Annotation> componentAnnotation) {
        return componentBuilders.get(componentAnnotation);
    }

    public Map<Class<? extends Annotation>, TransactionProcessorBuilder> getComponentBuilders() {
        return componentBuilders;
    }

    public TransactionProcessor getProcessor() {
        return processor;
    }
}
