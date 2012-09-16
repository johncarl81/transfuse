package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.annotations.Asynchronous;
import org.androidtransfuse.annotations.UIThread;
import org.androidtransfuse.aop.AsynchronousMethodInterceptor;
import org.androidtransfuse.aop.UIThreadMethodInterceptor;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AOPRepositoryProvider implements Provider<AOPRepository> {

    private final ASTClassFactory astClassFactory;

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
