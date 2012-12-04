package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Parcel;

/**
 * @author John Ericksen
 */
@Parcel
public class Tester {

    private ParcelProxyProxy proxy;

    public ParcelProxyProxy getProxy() {
        return proxy;
    }

    public void setProxy(ParcelProxyProxy proxy) {
        this.proxy = proxy;
    }
}
