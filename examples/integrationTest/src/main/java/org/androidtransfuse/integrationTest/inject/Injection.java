/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.Injectors;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.inject.LibraryDependency1;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
@Activity(name = "InjectionActivity", label = "Injection")
@Layout(R.layout.main)
@DeclareField
public class Injection extends InjectionBase {

    @Inject
    private InjectTarget privateInjection;
    @Inject
    protected InjectTarget protectedInjection;
    @Inject
    InjectTarget packagePrivateInjection;
    @Inject
    public InjectTarget publicInjection;
    private InjectTarget constructorInjection;
    private InjectTarget methodInjectionOne;
    private InjectTarget methodInjectionTwo;
    @Inject
    private LoopOne dependencyLoopOne;
    @Inject
    private ProvidedInjectTarget providedInjectTarget;
    @Inject
    private Provider<ProvidedInjectTarget> providedInjectTargetProvider;
    @Inject
    private Provider<InjectTarget> generatedProvider;
    @Inject
    private LibraryDependency1 libraryDependency;
    @Inject
    private GenericType<Concrete> genericInjection;
    @Inject
    private GenericType<Concrete2> genericInjection2;
    @Inject
    private ImplBy implBy;

    @Inject
    protected InnerClass innerClass;

    public static class InnerClass {
        @Inject
        android.app.Activity activity;

        public android.app.Activity getActivity() {
            return activity;
        }
    }


    @Inject
    public Injection(InjectTarget constructorInjection) {
        this.constructorInjection = constructorInjection;
    }

    @Inject
    public void setMethodInjection(InjectTarget methodInjectionOne, InjectTarget methodInjectionTwo) {
        this.methodInjectionOne = methodInjectionOne;
        this.methodInjectionTwo = methodInjectionTwo;
    }

    public InjectTarget getPrivateInjection() {
        return privateInjection;
    }

    public InjectTarget getProtectedInjection() {
        return protectedInjection;
    }

    public InjectTarget getPackagePrivateInjection() {
        return packagePrivateInjection;
    }

    public InjectTarget getPublicInjection() {
        return publicInjection;
    }

    public InjectTarget getConstructorInjection() {
        return constructorInjection;
    }

    public InjectTarget getMethodInjectionOne() {
        return methodInjectionOne;
    }

    public InjectTarget getMethodInjectionTwo() {
        return methodInjectionTwo;
    }

    public LoopOne getDependencyLoopOne() {
        return dependencyLoopOne;
    }

    public ProvidedInjectTarget getProvidedInjectTarget() {
        return providedInjectTarget;
    }

    public Provider<InjectTarget> getGeneratedProvider() {
        return generatedProvider;
    }

    public Provider<ProvidedInjectTarget> getProvidedInjectTargetProvider() {
        return providedInjectTargetProvider;
    }

    public InjectTarget getStaticInjectionTarget() {
        return Injectors.get(Injector.class).getTarget();
    }

    public LoopOne getStaticInjectionLoop() {
        return Injectors.get(Injector.class).getLoop();
    }

    public Provider<LoopThree> getStaticInjectionProvider() {
        return Injectors.get(Injector.class).getLoopThreeProvider();
    }

    public LibraryDependency1 getLibraryDependency() {
        return libraryDependency;
    }

    public GenericType<Concrete> getGenericInjection() {
        return genericInjection;
    }

    public GenericType<Concrete2> getGenericInjection2() {
        return genericInjection2;
    }

    public ImplBy getImplBy() {
        return implBy;
    }
}
