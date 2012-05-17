package org.androidtransfuse.analysis.astAnalyzer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class RegistrationAspect {

    private Set<ListenerRegistration> registrations = new HashSet<ListenerRegistration>();


    public void addRegistration(ListenerRegistration registration) {
        registrations.add(registration);
    }

    public Set<ListenerRegistration> getRegistrations() {
        return registrations;
    }
}
