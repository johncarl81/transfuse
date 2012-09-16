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

    private final JCodeModel codeModel;
    private final Map<JType, ResourceExpressionBuilderAdaptor> resourceExpressionBuilderMap = new HashMap<JType, ResourceExpressionBuilderAdaptor>();
    private final MethodBasedResourceExpressionBuilderAdaptorFactory adaptorFactory;

    @Inject
    public ResourceExpressionBuilderFactory(MethodBasedResourceExpressionBuilderAdaptorFactory adaptorFactory, JCodeModel codeModel) {
        this.codeModel = codeModel;
        this.adaptorFactory = adaptorFactory;

        addMethodBasedResourceBuider(String.class, GET_STRING);
        addMethodBasedResourceBuider(String.class, GET_STRING);
        addMethodBasedResourceBuider(Boolean.class, GET_BOOLEAN);
        addMethodBasedResourceBuider(boolean.class, GET_BOOLEAN);
        addMethodBasedResourceBuider(ColorStateList.class, GET_COLORSTATELIST);
        addMethodBasedResourceBuider(Integer.class, GET_INTEGER);
        addMethodBasedResourceBuider(int.class, GET_INTEGER);
        addMethodBasedResourceBuider(Drawable.class, GET_DRAWABLE);
        addMethodBasedResourceBuider(String[].class, GET_STRINGARRAY);
        addMethodBasedResourceBuider(Integer[].class, GET_INTARRAY);
        addMethodBasedResourceBuider(int[].class, GET_INTARRAY);
        addMethodBasedResourceBuider(Movie.class, GET_MOVIE);
        addAnimationResourceBuilder(Animation.class);
    }

    private void addMethodBasedResourceBuider(Class clazz, String method) {
        JType refClass = codeModel._ref(clazz);
        resourceExpressionBuilderMap.put(refClass, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(clazz, method));
    }

    private void addAnimationResourceBuilder(Class clazz) {
        JType refClass = codeModel._ref(clazz);
        resourceExpressionBuilderMap.put(refClass, adaptorFactory.buildAnimationResourceExpressionBuilderAdaptor());
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(JType resourceType, AnalysisContext context) {

        ResourceExpressionBuilderAdaptor resourceExpressionBuilderAdaptor = resourceExpressionBuilderMap.get(resourceType);

        return resourceExpressionBuilderAdaptor.buildResourceExpressionBuilder(context);
    }
}
