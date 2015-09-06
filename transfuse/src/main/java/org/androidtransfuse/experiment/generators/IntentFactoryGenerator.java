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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.repository.BundlePropertyBuilderRepository;
import org.androidtransfuse.analysis.repository.ParcelerPropertyBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.intentFactory.ComponentFactory;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class IntentFactoryGenerator extends AbstractExtraFactoryGenerator {

    private static final String BUILD_INTENT = "build";

    private final ClassGenerationUtil generationUtil;
    private final JCodeModel codeModel;
    private final UniqueVariableNamer namer;

    @Inject
    public IntentFactoryGenerator(ClassGenerationUtil generationUtil, UniqueVariableNamer namer, BundlePropertyBuilderRepository repository, ParcelerPropertyBuilder parcelerPropertyBuilder, JCodeModel codeModel) {
        super(generationUtil, namer, repository, parcelerPropertyBuilder);
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.codeModel = codeModel;
    }

    @Override
    public String getName() {
        return "IntentFactory Generator";
    }

    @Override
    protected void setupClass(ComponentDescriptor descriptor, JDefinedClass factoryClass) {
        factoryClass._extends(generationUtil.ref(ComponentFactory.class));
    }

    protected void createMethods(ComponentDescriptor descriptor, JDefinedClass factoryClass, JFieldVar bundle) {
        createBuildMethod(descriptor, factoryClass, bundle);
        createAddFlagMethod(factoryClass);
        createAddCategoryMethod(factoryClass);
    }

    private void createAddFlagMethod(JDefinedClass factoryClass) {
        JMethod getFlagMethod = factoryClass.method(JMod.PUBLIC, factoryClass, ComponentFactory.ADD_FLAG_METHOD);
        JVar flagParam = getFlagMethod.param(codeModel.INT, namer.generateName(codeModel.INT));
        JBlock body = getFlagMethod.body();

        body.invoke(ComponentFactory.INTERNAL_ADD_FLAG_METHOD).arg(flagParam);

        body._return(JExpr._this());
    }

    private void createAddCategoryMethod(JDefinedClass factoryClass) {
        JMethod getFlagMethod = factoryClass.method(JMod.PUBLIC, factoryClass, ComponentFactory.ADD_CATEGORY_METHOD);
        JVar categoryParam = getFlagMethod.param(generationUtil.ref(String.class), namer.generateName(String.class));
        JBlock body = getFlagMethod.body();

        body.invoke(ComponentFactory.INTERNAL_ADD_CATEGORY_METHOD).arg(categoryParam);

        body._return(JExpr._this());
    }

    private void createBuildMethod(ComponentDescriptor descriptor, JDefinedClass factoryClass, JFieldVar bundle) {
        JClass targetRef = generationUtil.ref(descriptor.getPackageClass());
        JClass intentRef = generationUtil.ref(AndroidLiterals.INTENT);
        JMethod buildMethod = factoryClass.method(JMod.PUBLIC, intentRef, BUILD_INTENT);
        JVar contextParam = buildMethod.param(generationUtil.ref(AndroidLiterals.CONTEXT), namer.generateName(AndroidLiterals.CONTEXT));
        JBlock buildMethodBody = buildMethod.body();

        JVar intentVar = buildMethodBody.decl(intentRef, namer.generateName(intentRef), JExpr._new(intentRef).arg(contextParam).arg(targetRef.dotclass()));
        buildMethodBody.invoke(intentVar, "setFlags").arg(JExpr.invoke(ComponentFactory.INTERNAL_GET_FLAGS_METHOD));
        buildMethodBody.invoke(intentVar, "putExtras").arg(bundle);

        buildMethodBody._return(intentVar);
    }
}
