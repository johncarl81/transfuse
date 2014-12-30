/**
 * Copyright 2011-2015 John Ericksen
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

import com.sun.codemodel.JType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.util.AndroidLiterals;

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

    private final ClassGenerationUtil generationUtil;
    private final Map<JType, ResourceExpressionBuilderAdaptor> resourceExpressionBuilderMap = new HashMap<JType, ResourceExpressionBuilderAdaptor>();
    private final MethodBasedResourceExpressionBuilderAdaptorFactory adaptorFactory;
    private final ASTClassFactory astClassFactory;

    @Inject
    public ResourceExpressionBuilderFactory(MethodBasedResourceExpressionBuilderAdaptorFactory adaptorFactory, ClassGenerationUtil generationUtil, ASTClassFactory astClassFactory) {
        this.generationUtil = generationUtil;
        this.adaptorFactory = adaptorFactory;
        this.astClassFactory = astClassFactory;

        addMethodBasedResourceBuider(String.class, GET_STRING);
        addMethodBasedResourceBuider(String.class, GET_STRING);
        addMethodBasedResourceBuider(Boolean.class, GET_BOOLEAN);
        addMethodBasedResourceBuider(boolean.class, GET_BOOLEAN);
        addMethodBasedResourceBuider(AndroidLiterals.COLOR_STATE_LIST, GET_COLORSTATELIST);
        addMethodBasedResourceBuider(Integer.class, GET_INTEGER);
        addMethodBasedResourceBuider(int.class, GET_INTEGER);
        addMethodBasedResourceBuider(AndroidLiterals.GRAPHICS_DRAWABLE, GET_DRAWABLE);
        addMethodBasedResourceBuider(String[].class, GET_STRINGARRAY);
        addMethodBasedResourceBuider(Integer[].class, GET_INTARRAY);
        addMethodBasedResourceBuider(int[].class, GET_INTARRAY);
        addMethodBasedResourceBuider(AndroidLiterals.GRAPHICS_MOVIE, GET_MOVIE);
        addAnimationResourceBuilder(AndroidLiterals.ANIMATION);
    }

    private void addMethodBasedResourceBuider(ASTType type, String method) {
        JType refClass = generationUtil.type(type);
        resourceExpressionBuilderMap.put(refClass, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(type, method));
    }

    private void addMethodBasedResourceBuider(Class clazz, String method) {
        JType refClass = generationUtil.type(clazz);
        resourceExpressionBuilderMap.put(refClass, adaptorFactory.buildMethodBasedResourceExpressionBuilderAdaptor(astClassFactory.getType(clazz), method));
    }

    private void addAnimationResourceBuilder(ASTType type) {
        JType refClass = generationUtil.type(type);
        resourceExpressionBuilderMap.put(refClass, adaptorFactory.buildAnimationResourceExpressionBuilderAdaptor());
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(JType resourceType, AnalysisContext context) {

        ResourceExpressionBuilderAdaptor resourceExpressionBuilderAdaptor = resourceExpressionBuilderMap.get(resourceType);

        return resourceExpressionBuilderAdaptor.buildResourceExpressionBuilder(context);
    }
}
