package org.androidtransfuse.gen;

import org.androidtransfuse.intentFactory.AbstractIntentFactoryStrategy;

/**
 * @author John Ericksen
 */
public interface GeneratorFactory {

    IntentFactoryStrategyGenerator buildStrategyGenerator(Class<? extends AbstractIntentFactoryStrategy> baseClass);
}
