package org.androidtransfuse.integrationTest.externalGenerator;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class Target {

    private ProxiedProxy proxy;

    @Inject
    public Target(ProxiedProxy proxy) {
        this.proxy = proxy;
    }
}
