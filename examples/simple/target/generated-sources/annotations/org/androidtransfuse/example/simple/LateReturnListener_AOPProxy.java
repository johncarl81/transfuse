
package org.androidtransfuse.example.simple;

import android.view.View;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.aop.AsynchronousMethodInterceptor;
import org.androidtransfuse.aop.MethodInvocation;
import org.androidtransfuse.aop.UIThreadMethodInterceptor;

public class LateReturnListener_AOPProxy
    extends LateReturnListener
{

    private UIThreadMethodInterceptor uIThreadMethodInterceptor_0;
    private AsynchronousMethodInterceptor asynchronousMethodInterceptor_0;

    public LateReturnListener_AOPProxy(UIThreadMethodInterceptor uIThreadMethodInterceptor_1, AsynchronousMethodInterceptor asynchronousMethodInterceptor_1) {
        super();
        uIThreadMethodInterceptor_0 = uIThreadMethodInterceptor_1;
        asynchronousMethodInterceptor_0 = asynchronousMethodInterceptor_1;
    }

    public void notifyUIThread() {
        try {
            uIThreadMethodInterceptor_0 .invoke(new MethodInvocation() {


                public Object proceed() {
                    LateReturnListener_AOPProxy.super.notifyUIThread();
                    return null;
                }

            }
            );
        } catch (Throwable e) {
            throw new TransfuseAnalysisException("exception during method interception", e);
        }
    }

    public void onClick(final View view_1) {
        try {
            asynchronousMethodInterceptor_0 .invoke(new MethodInvocation() {


                public Object proceed() {
                    LateReturnListener_AOPProxy.super.onClick(view_1);
                    return null;
                }

            }
            );
        } catch (Throwable e) {
            throw new TransfuseAnalysisException("exception during method interception", e);
        }
    }

}
