package org.androidtransfuse.gen.variableBuilder;

import android.app.Activity;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.IntentFactoryExtra;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter<Extra> {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ExtraInjectionNodeBuilder(ASTClassFactory astClassFactory,
                                     InjectionPointFactory injectionPointFactory,
                                     VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        super(Extra.class);
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

        boolean wrapped = astType.isAnnotated(Parcel.class);

        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType activityType = astClassFactory.buildASTClassType(Activity.class);
        InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(activityType, context);

        injectionNode.addAspect(IntentFactoryExtra.class, new IntentFactoryExtra(!optional, extraId, astType));

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildExtraVariableBuilder(extraId, activityInjectionNode, optional, wrapped));

        return injectionNode;
    }
}
