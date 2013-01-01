/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.gen.variableBuilder.resource;

import android.content.res.ColorStateList;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.AnalysisContext;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
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
