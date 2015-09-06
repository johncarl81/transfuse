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
import org.androidtransfuse.intentFactory.FragmentFactory;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FragmentFactoryGenerator extends AbstractExtraFactoryGenerator {

    private static final String BUILD_FRAGMENT = "build";

    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer namer;

    @Inject
    public FragmentFactoryGenerator(ClassGenerationUtil generationUtil, UniqueVariableNamer namer, BundlePropertyBuilderRepository repository, ParcelerPropertyBuilder parcelerPropertyBuilder) {
        super(generationUtil, namer, repository, parcelerPropertyBuilder);
        this.generationUtil = generationUtil;
        this.namer = namer;
    }

    @Override
    public String getName() {
        return "FragmentFactory Generator";
    }

    @Override
    protected void setupClass(ComponentDescriptor descriptor, JDefinedClass factoryClass) {
        factoryClass._implements(generationUtil.ref(FragmentFactory.class).narrow(generationUtil.ref(descriptor.getPackageClass())));
    }

    protected void createMethods(ComponentDescriptor descriptor, JDefinedClass factoryClass, JFieldVar bundle) {
        JClass targetRef = generationUtil.ref(descriptor.getPackageClass());
        JBlock buildMethodBody = factoryClass.method(JMod.PUBLIC, targetRef, BUILD_FRAGMENT).body();

        JVar fragmentVar = buildMethodBody.decl(targetRef, namer.generateName(targetRef), JExpr._new(targetRef));
        buildMethodBody.invoke(fragmentVar, "setArguments").arg(bundle);

        buildMethodBody._return(fragmentVar);
    }
}
