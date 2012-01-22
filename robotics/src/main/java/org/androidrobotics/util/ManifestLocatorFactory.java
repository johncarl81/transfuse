package org.androidrobotics.util;

import javax.annotation.processing.Filer;

/**
 * @author John Ericksen
 */
public interface ManifestLocatorFactory {

    ManifestLocator buildManifestLocator(Filer filer);
}
