package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.gen.componentBuilder.RegistrationGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Aspect to associate an element (method, field or type) with a listener registration.  This triggers the code generator to
 * register the given element with the identified resource.  Typically this means generating an on*() like so:
 *
 * {@code resource.onClickListener(field);}
 *
 * @author John Ericksen
 */
public class RegistrationAspect {

    private final List<RegistrationGenerator> registrationBuilders = new ArrayList<RegistrationGenerator>();

    public void addRegistrationbuilders(List<RegistrationGenerator> builders){
        registrationBuilders.addAll(builders);
    }

    public List<RegistrationGenerator> getRegistrationBuilders() {
        return registrationBuilders;
    }
}
