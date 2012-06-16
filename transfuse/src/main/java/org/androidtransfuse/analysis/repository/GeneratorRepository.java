package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.Generator;
import org.androidtransfuse.util.matcher.Matcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class GeneratorRepository {

    private Map<Matcher<ASTType>, Generator<ASTType>> repository = new HashMap<Matcher<ASTType>, Generator<ASTType>>();

    public void add(Matcher<ASTType> matcher, Generator<ASTType> generator) {
        repository.put(matcher, generator);
    }

    public Map<Matcher<ASTType>, Generator<ASTType>> getRepository() {
        return repository;
    }
}
