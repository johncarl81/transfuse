package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.matcher.Matcher;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ComponentProcessor {

    private Matcher generatorMatcher;

    @Inject
    public ComponentProcessor(@Named("componentGeneratorMatcher") Matcher generatorMatcher) {
        this.generatorMatcher = generatorMatcher;
    }

    public void process(Collection<? extends ASTType> astTypes) {
        for (ASTType astType : astTypes) {
            generatorMatcher.match(astType);
        }
    }
}
