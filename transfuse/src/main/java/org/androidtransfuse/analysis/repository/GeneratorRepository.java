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

    private Map<Matcher<ASTType>, Generator> repository = new HashMap<Matcher<ASTType>, Generator>();

    public void add(Matcher<ASTType> matcher, Generator generator){
        repository.put(matcher, generator);
    }

    public Map<Matcher<ASTType>, Generator> getRepository() {
        return repository;
    }
}
