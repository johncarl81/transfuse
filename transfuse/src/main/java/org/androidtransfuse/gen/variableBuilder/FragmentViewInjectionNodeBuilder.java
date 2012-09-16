package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FragmentViewInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter {

    private final JCodeModel codeModel;
    private final InjectionPointFactory injectionPointFactory;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final Analyzer analyzer;

    @Inject
    public FragmentViewInjectionNodeBuilder(JCodeModel codeModel,
                                            InjectionPointFactory injectionPointFactory,
                                            VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                            Analyzer analyzer) {
        super(View.class);
        this.codeModel = codeModel;
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        Integer viewId = annotation.getProperty("value", Integer.class);
        String viewTag = annotation.getProperty("tag", String.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);

        InjectionNode viewInjectionNode = injectionPointFactory.buildInjectionNode(android.view.View.class, context);

        try {
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildFragmentViewVariableBuilder(viewId, viewTag, viewInjectionNode, codeModel.parseType(astType.getName())));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }
}
