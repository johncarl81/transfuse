package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class RegistrationAspect {

    private Set<ListenerRegistration<ASTMethod>> methodRegistrations = new HashSet<ListenerRegistration<ASTMethod>>();
    private Set<ListenerRegistration<ASTField>> fieldRegistrations = new HashSet<ListenerRegistration<ASTField>>();
    private Set<ListenerRegistration<ASTType>> typeRegistrations = new HashSet<ListenerRegistration<ASTType>>();


    public void addMethodRegistration(ListenerRegistration<ASTMethod> registration) {
        methodRegistrations.add(registration);
    }

    public void addFieldRegistration(ListenerRegistration<ASTField> registration) {
        fieldRegistrations.add(registration);
    }

    public void addTypeRegistration(ListenerRegistration<ASTType> registration) {
        typeRegistrations.add(registration);
    }

    public Set<ListenerRegistration<ASTMethod>> getMethodRegistrations() {
        return methodRegistrations;
    }

    public Set<ListenerRegistration<ASTField>> getFieldRegistrations() {
        return fieldRegistrations;
    }

    public Set<ListenerRegistration<ASTType>> getTypeRegistrations() {
        return typeRegistrations;
    }
}
