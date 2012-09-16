package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.GeneratorRepository;
import org.androidtransfuse.gen.Generator;
import org.androidtransfuse.util.matcher.Matcher;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentProcessor {

    private final GeneratorRepository generatorRepository;

    @Inject
    public ComponentProcessor(GeneratorRepository generatorRepository) {
        this.generatorRepository = generatorRepository;
    }

    public void process(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            for (Map.Entry<Matcher<ASTType>, Generator<ASTType>> generatorEntry : generatorRepository.getRepository().entrySet()) {
                if (generatorEntry.getKey().matches(astType)) {
                    generatorEntry.getValue().generate(astType);
                }
            }
        }
    }
}
