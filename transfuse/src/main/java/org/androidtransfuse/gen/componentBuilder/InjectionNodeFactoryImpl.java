package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectionNodeFactoryImpl implements InjectionNodeFactory {

    private ASTType astType;
    private AnalysisContext context;
    private InjectionPointFactory injectionPointFactory;

    @Inject
    public InjectionNodeFactoryImpl(@Assisted ASTType astType, @Assisted AnalysisContext context, InjectionPointFactory injectionPointFactory) {
        this.astType = astType;
        this.context = context;
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor) {
        return injectionPointFactory.buildInjectionNode(astType, context);
    }
}
