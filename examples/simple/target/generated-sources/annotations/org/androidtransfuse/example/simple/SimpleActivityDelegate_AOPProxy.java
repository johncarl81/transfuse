
package org.androidtransfuse.example.simple;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.aop.MethodInvocation;

public class SimpleActivityDelegate_AOPProxy
    extends SimpleActivityDelegate
{

    private org.androidtransfuse.example.simple.LoggingInterceptor loggingInterceptor_0;

    public SimpleActivityDelegate_AOPProxy(LifecycleLogger lifecycleLogger_0, org.androidtransfuse.example.simple.LoggingInterceptor loggingInterceptor_1) {
        super(lifecycleLogger_0);
        loggingInterceptor_0 = loggingInterceptor_1;
    }

    protected void touch() {
        try {
            loggingInterceptor_0 .invoke(new MethodInvocation() {


                public Object proceed() {
                    SimpleActivityDelegate_AOPProxy.super.touch();
                    return null;
                }

            }
            );
        } catch (Throwable e) {
            throw new TransfuseAnalysisException("exception during method interception", e);
        }
    }

}
