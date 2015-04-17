package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.util.matcher.Matcher;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class BundlePropertyBuilderRepository {

    private final Map<Matcher<ASTType>, PropertyBuilder> builders = new LinkedHashMap<Matcher<ASTType>, PropertyBuilder>();

    public void add(Matcher<ASTType> matcher, PropertyBuilder builder){
        builders.put(matcher, builder);
    }

    public boolean matches(ASTType type){
        for (Matcher<ASTType> matcher : builders.keySet()) {
            if(matcher.matches(type)){
                return true;
            }
        }
        return false;
    }

    public PropertyBuilder get(ASTType type) {
        for (Map.Entry<Matcher<ASTType>, PropertyBuilder> generatorEntry : builders.entrySet()) {
            if(generatorEntry.getKey().matches(type)){
                return generatorEntry.getValue();
            }
        }
        return null;
    }
}
