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
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class IntentFactoryGenerator extends AbstractExtraFactoryGenerator {

    private static final String BUILD_INTENT = "build";

    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;

    @Inject
    public IntentFactoryGenerator(ClassGenerationUtil generationUtil, UniqueVariableNamer namer, BundlePropertyBuilderRepository repository, ParcelerPropertyBuilder parcelerPropertyBuilder) {
        super(generationUtil, namer, repository, parcelerPropertyBuilder);
        this.generationUtil = generationUtil;
        this.namer = namer;
    }

    @Override
    public String getName() {
        return "IntentFactory Generator";
    }

    protected void createBuilderMethod(ComponentDescriptor descriptor, JDefinedClass factoryClass, JFieldVar bundle) {
        JClass targetRef = generationUtil.ref(descriptor.getPackageClass());
        JClass intentRef = generationUtil.ref(AndroidLiterals.INTENT);
        JMethod buildMethod = factoryClass.method(JMod.PUBLIC, intentRef, BUILD_INTENT);
        JVar contextParam = buildMethod.param(generationUtil.ref(AndroidLiterals.CONTEXT), namer.generateName(AndroidLiterals.CONTEXT));
        JBlock buildMethodBody = buildMethod.body();

        JVar intentVar = buildMethodBody.decl(intentRef, namer.generateName(descriptor.getType()), JExpr._new(intentRef).arg(contextParam).arg(targetRef.dotclass()));
        buildMethodBody.invoke(intentVar, "putExtras").arg(bundle);

        buildMethodBody._return(intentVar);
    }
}
