package org.androidrobotics.gen.target;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableBuilderInjectable {

    @Inject
    private VariableTarget target;

    public VariableTarget getTarget() {
        return target;
    }
}
