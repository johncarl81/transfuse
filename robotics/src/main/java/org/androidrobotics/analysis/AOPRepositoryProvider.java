package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.annotations.Asynchronous;
import org.androidrobotics.annotations.UIThread;
import org.androidrobotics.aop.AsynchronousMethodInterceptor;
import org.androidrobotics.aop.UIThreadMethodInterceptor;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AOPRepositoryProvider implements Provider<AOPRepository> {

    private ASTClassFactory astClassFactory;

    @Inject
    public AOPRepositoryProvider(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    @Override
    public AOPRepository get() {
        AOPRepository aopRepository = new AOPRepository();

        aopRepository.put(astClassFactory.buildASTClassType(Asynchronous.class), astClassFactory.buildASTClassType(AsynchronousMethodInterceptor.class));
        aopRepository.put(astClassFactory.buildASTClassType(UIThread.class), astClassFactory.buildASTClassType(UIThreadMethodInterceptor.class));

        return aopRepository;
    }
}
