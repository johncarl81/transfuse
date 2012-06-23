package org.androidtransfuse.gen.variableBuilder;

import android.app.Activity;
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
public class ViewInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter<View> {

    private JCodeModel codeModel;
    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private Analyzer analyzer;

    @Inject
    public ViewInjectionNodeBuilder(JCodeModel codeModel,
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

        InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(Activity.class, context);

        try {
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildViewVariableBuilder(viewId, viewTag, activityInjectionNode, codeModel.parseType(astType.getName())));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }
}
