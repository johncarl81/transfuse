package org.androidtransfuse.integrationTest.externalGenerator;

import org.androidtransfuse.annotations.Parcel;

/**
 * @author John Ericksen
 */
@Parcel
public class Tester {

    private ProxiedProxy proxy;

    public ProxiedProxy getProxy() {
        return proxy;
    }

    public void setProxy(ProxiedProxy proxy) {
        this.proxy = proxy;
    }
}
