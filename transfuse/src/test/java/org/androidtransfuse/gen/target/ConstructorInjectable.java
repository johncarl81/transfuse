package org.androidtransfuse.gen.target;

/**
 * @author John Ericksen
 */
public class ConstructorInjectable {

    private InjectionTarget injectionTarget;

    public ConstructorInjectable(InjectionTarget injectionTarget) {
        this.injectionTarget = injectionTarget;
    }

    public InjectionTarget getInjectionTarget() {
        return injectionTarget;
    }
}
