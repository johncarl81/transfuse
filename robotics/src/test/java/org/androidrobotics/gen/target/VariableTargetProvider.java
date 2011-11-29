package org.androidrobotics.gen.target;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class VariableTargetProvider implements Provider<VariableTarget> {

    @Override
    public VariableTarget get() {
        return new VariableTarget();
    }
}
