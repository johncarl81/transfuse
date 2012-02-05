package org.androidtransfuse.gen.variableBuilder.resource;

import android.content.res.ColorStateList;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.AnalysisContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class ResourceExpressionBuilderFactory {

    private static final String GET_STRING = "getString";
    private static final String GET_BOOLEAN = "getBoolean";
    private static final String GET_COLORSTATELIST = "getColorStateList";
    private static final String GET_INTEGER = "getInteger";
    private static final String GET_DRAWABLE = "getDrawable";
    private static final String GET_STRINGARRAY = "getStringArray";
    private static final String GET_INTARRAY = "getIntArray";
    private static final String GET_MOVIE = "getMovie";

    private JCodeModel codeModel;
    private Map<JType, ResourceExpressionBuilderAdaptor> resourceExpressionBuilderMap = new HashMap<JType, ResourceExpressionBuilderAdaptor>();

    @Inject
    public ResourceExpressionBuilderFactory(MethodBasedResourceExpressionBuilderAdaptorFactory adaptorFactory, JCodeModel codeModel) {
        this.codeModel = codeModel;
        addResourceBuilder(String.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_STRING));
        addResourceBuilder(Boolean.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_BOOLEAN));
        addResourceBuilder(boolean.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_BOOLEAN));
        addResourceBuilder(ColorStateList.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_COLORSTATELIST));
        addResourceBuilder(Integer.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_INTEGER));
        addResourceBuilder(int.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_INTEGER));
        addResourceBuilder(Drawable.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_DRAWABLE));
        addResourceBuilder(String[].class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_STRINGARRAY));
        addResourceBuilder(Integer[].class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_INTARRAY));
        addResourceBuilder(int[].class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_INTARRAY));
        addResourceBuilder(Movie.class, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(GET_MOVIE));
        addResourceBuilder(Animation.class, adaptorFactory.buildAnimationResourceExpressionBuilderAdaptor());
    }

    private void addResourceBuilder(Class clazz, ResourceExpressionBuilderAdaptor resourceExpressionBuilderAdaptor) {
        JType refClass = codeModel._ref(clazz);

        resourceExpressionBuilderMap.put(refClass, resourceExpressionBuilderAdaptor);
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(JType resourceType, AnalysisContext context) {

        ResourceExpressionBuilderAdaptor resourceExpressionBuilderAdaptor = resourceExpressionBuilderMap.get(resourceType);

        return resourceExpressionBuilderAdaptor.buildResourceExpressionBuilder(context);
    }
}
