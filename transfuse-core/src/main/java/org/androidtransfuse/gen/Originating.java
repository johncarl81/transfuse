package org.androidtransfuse.gen;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ElementHolder;

import javax.inject.Singleton;
import javax.lang.model.element.Element;
import java.util.*;

@Singleton
public class Originating {

    private Map<String, Set<Element>> originatingElements = new HashMap<String, Set<Element>>();

    public Element[] getOriginatingElements(String generatedClassName) {
        if(originatingElements.containsKey(generatedClassName)) {
            return originatingElements.get(generatedClassName).toArray(new Element[0]);
        }
        return new Element[0];
    }

    public void associate(String generatedClassName, ASTType original) {
        if(original instanceof ElementHolder) {
            if (!originatingElements.containsKey(generatedClassName)) {
                originatingElements.put(generatedClassName, new HashSet<Element>());
            }
            originatingElements.get(generatedClassName).add(((ElementHolder)original).getElement());
        }
    }
}
