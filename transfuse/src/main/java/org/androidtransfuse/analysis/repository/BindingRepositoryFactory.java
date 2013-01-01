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
package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.variableBuilder.*;

import javax.inject.Inject;

/**
 * Factory to create the Binding Repository
 *
 * @author John Ericksen
 */
public class BindingRepositoryFactory {

    private final ViewInjectionNodeBuilder viewVariableBuilder;
    private final ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    private final SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    private final ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;
    private final PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder;
    private final FragmentViewInjectionNodeBuilder fragmentViewInjectionNodeBuilder;

    @Inject
    public BindingRepositoryFactory(ViewInjectionNodeBuilder viewVariableBuilder,
                                    ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                                    SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                                    ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                                    PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder,
                                    FragmentViewInjectionNodeBuilder fragmentViewInjectionNodeBuilder) {
        this.viewVariableBuilder = viewVariableBuilder;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
        this.fragmentViewInjectionNodeBuilder = fragmentViewInjectionNodeBuilder;
    }

    public void addBindingAnnotations(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {

        injectionNodeBuilderRepository.putAnnotation(Extra.class, extraInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Resource.class, resourceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(SystemService.class, systemServiceBindingInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Preference.class, preferenceInjectionNodeBuilder);

    }

    public void addViewBindingAnnotation(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {
        injectionNodeBuilderRepository.putAnnotation(View.class, viewVariableBuilder);
    }

    public void addFragmentViewBindingAnnotation(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {
        injectionNodeBuilderRepository.putAnnotation(View.class, fragmentViewInjectionNodeBuilder);
    }
}
