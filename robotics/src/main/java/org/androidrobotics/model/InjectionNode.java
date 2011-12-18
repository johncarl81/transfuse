package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNode {

    private ASTType astType;
    private Map<Class<?>, Object> aspects = new HashMap<Class<?>, Object>();

    public InjectionNode(ASTType astType) {
        this.astType = astType;
    }

    public String getClassName() {
        return astType.getName();
    }

    public ASTType getAstType() {
        return astType;
    }

    public <T> T getAspect(Class<T> clazz) {
        return (T) aspects.get(clazz);
    }

    public void addAspect(Object object) {
        aspects.put(object.getClass(), object);
    }

    public boolean containsAspect(Class<?> clazz) {
        return aspects.containsKey(clazz);
    }
}
