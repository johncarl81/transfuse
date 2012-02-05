
package org.androidtransfuse.example.simple;

import android.view.View;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.aop.MethodInvocation;

public class VibrateOnClickListener_AOPProxy
    extends VibrateOnClickListener
{

    private org.androidtransfuse.example.simple.LoggingInterceptor loggingInterceptor_3;

    public VibrateOnClickListener_AOPProxy(org.androidtransfuse.example.simple.LoggingInterceptor loggingInterceptor_4) {
        super();
        loggingInterceptor_3 = loggingInterceptor_4;
    }

    public void onClick(final View view_0) {
        try {
            loggingInterceptor_3 .invoke(new MethodInvocation() {


                public Object proceed() {
                    VibrateOnClickListener_AOPProxy.super.onClick(view_0);
                    return null;
                }

            }
            );
        } catch (Throwable e) {
            throw new TransfuseAnalysisException("exception during method interception", e);
        }
    }

}
