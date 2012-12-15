package org.androidtransfuse.analysis.module;

/**
 * @author John Ericksen
 */
public interface BindingConfigurationFactory {

    BindConfigurationComposite buildConfigurationComposite(TypeProcessor processor);
}
