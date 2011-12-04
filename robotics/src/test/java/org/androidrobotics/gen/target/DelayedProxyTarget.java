package org.androidrobotics.gen.target;

/**
 * @author John Ericksen
 */
public class DelayedProxyTarget implements DelayedProxy {

    private DelayedProxyDependency delayedProxyDependency;

    public DelayedProxyTarget(DelayedProxyDependency delayedProxyDependency) {
        this.delayedProxyDependency = delayedProxyDependency;
    }

    public DelayedProxyDependency getDelayedProxyDependency() {
        return delayedProxyDependency;
    }
}
