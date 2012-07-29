package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.componentBuilder.RegistrationGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Aspect to associate an element (method, field or type) with a listener registration.  This triggers the code generator to
 * register the given element with the identified resource.  Typically this means generating an on*() like so:
 *
 * {@code resource.onClickListener(field);}
 *
 * @author John Ericksen
 */
public class RegistrationAspect {

    private List<RegistrationGenerator> registrationBuilders = new ArrayList<RegistrationGenerator>();

    private Set<ActionRegistration<ASTMethod>> methodRegistrations = new HashSet<ActionRegistration<ASTMethod>>();
    private Set<ActionRegistration<ASTField>> fieldRegistrations = new HashSet<ActionRegistration<ASTField>>();
    private Set<ActionRegistration<ASTType>> typeRegistrations = new HashSet<ActionRegistration<ASTType>>();

    public void addRegistrationbuilders(List<RegistrationGenerator> builders){
        registrationBuilders.addAll(builders);
    }

    public List<RegistrationGenerator> getRegistrationBuilders() {
        return registrationBuilders;
    }

    public void addMethodRegistration(ActionRegistration<ASTMethod> registration) {
        methodRegistrations.add(registration);
    }

    public void addFieldRegistration(ActionRegistration<ASTField> registration) {
        fieldRegistrations.add(registration);
    }

    public void addTypeRegistration(ActionRegistration<ASTType> registration) {
        typeRegistrations.add(registration);
    }

    public Set<ActionRegistration<ASTMethod>> getMethodRegistrations() {
        return methodRegistrations;
    }

    public Set<ActionRegistration<ASTField>> getFieldRegistrations() {
        return fieldRegistrations;
    }

    public Set<ActionRegistration<ASTType>> getTypeRegistrations() {
        return typeRegistrations;
    }
}
