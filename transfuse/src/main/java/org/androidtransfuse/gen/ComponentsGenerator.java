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
package org.androidtransfuse.gen;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.Components;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.util.Repository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ComponentsGenerator extends AbstractRepositoryGenerator<JDefinedClass> {

    private static final PackageClass REPOSITORY_NAME = new PackageClass(Components.COMPONENTS_PACKAGE, Components.COMPONENTS_REPOSITORY_NAME);
    private final Boolean libraryProject;
    @Inject
    public ComponentsGenerator(ClassGenerationUtil generationUtil, UniqueVariableNamer namer, @Named("libraryProject") Boolean libraryProject) {
        super(Repository.class, generationUtil, namer, REPOSITORY_NAME, Class.class);
        this.libraryProject = libraryProject;
    }

    @Override
    public JDefinedClass generate(Map<Provider<ASTType>, JDefinedClass> aggregate) {
        if(libraryProject) {
            return null;
        }
        return super.generate(aggregate);
    }

    @Override
    protected Map<? extends JExpression, ? extends JExpression> generateMapping(JDefinedClass factoryRepositoryClass, JClass interfaceClass, JDefinedClass concreteType) throws JClassAlreadyExistsException {
        return ImmutableMap.of(interfaceClass.dotclass(), concreteType.dotclass());
    }
}
