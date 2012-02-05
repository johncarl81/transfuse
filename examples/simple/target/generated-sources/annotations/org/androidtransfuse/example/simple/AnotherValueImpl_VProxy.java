
package org.androidtransfuse.example.simple;

import org.androidtransfuse.gen.proxy.DelayedLoad;
import org.androidtransfuse.util.VirtualProxyException;

public class AnotherValueImpl_VProxy
    implements AnotherValue, DelayedLoad<AnotherValueImpl>
{

    private AnotherValueImpl delegate = null;

    public void load(AnotherValueImpl delegateInput) {
        delegate = delegateInput;
    }

    public Boolean doWork() {
        if (delegate == null) {
            throw new VirtualProxyException("Trying to use a proxied instance before initialization");
        }
        return delegate.doWork();
    }

}
