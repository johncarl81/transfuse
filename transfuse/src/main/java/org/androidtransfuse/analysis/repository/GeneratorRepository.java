package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.processor.TransactionProcessorBuilder;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class GeneratorRepository {

    private final Map<Class<? extends Annotation>, TransactionProcessorBuilder> repository = new HashMap<Class<? extends Annotation>, TransactionProcessorBuilder>();

    public void add(Class<? extends Annotation> componentAnnotation, TransactionProcessorBuilder generator) {
        repository.put(componentAnnotation, generator);
    }

    public TransactionProcessorBuilder getBuilder(Class<? extends Annotation> componentAnnotation) {
        return repository.get(componentAnnotation);
    }

    public Map<Class<? extends Annotation>, TransactionProcessorBuilder> getRepository() {
        return repository;
    }
}
