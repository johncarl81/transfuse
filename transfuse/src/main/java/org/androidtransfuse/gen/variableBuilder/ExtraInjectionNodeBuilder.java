package org.androidtransfuse.gen.variableBuilder;

import android.app.Activity;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtra;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ExtraInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter<Extra> {

    private JCodeModel codeModel;
    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ExtraInjectionNodeBuilder(JCodeModel codeModel,
                                     ASTClassFactory astClassFactory,
                                     InjectionPointFactory injectionPointFactory,
                                     VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        super(Extra.class);
        this.codeModel = codeModel;
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String extraId = annotation.getProperty("value", String.class);
        Boolean optional = annotation.getProperty("optional", Boolean.class);

        if (optional == null) {
            optional = false;
        }

        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType activityType = astClassFactory.buildASTClassType(Activity.class);
        InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(activityType, context);

        injectionNode.addAspect(IntentFactoryExtra.class, new IntentFactoryExtra(!optional, extraId, astType));

        try {
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildExtraVariableBuilder(extraId, activityInjectionNode, codeModel.parseType(astType.getName()), optional));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }

    private ASTAnnotation getAnnotation(Class<? extends Annotation> clazz, Collection<ASTAnnotation> annotations) {
        ASTAnnotation annotation = null;

        for (ASTAnnotation astAnnotation : annotations) {
            if (astAnnotation.getName().equals(clazz.getName())) {
                annotation = astAnnotation;
            }
        }

        return annotation;
    }
}
