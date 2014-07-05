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
package org.androidtransfuse.experiment;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.analysis.AnalysisContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ComponentDescriptor {

    private final ASTType target;
    private final ASTType componentType;
    private final PackageClass packageClass;
    private InjectionGeneration injectionGenerator;
    private final List<PreInjectionGeneration> preInjectionGenerators = new ArrayList<PreInjectionGeneration>();
    private final List<PostInjectionGeneration> postInjectionGenerators = new ArrayList<PostInjectionGeneration>();
    private AnalysisContext analysisContext;

    public ComponentDescriptor(ASTType target, ASTType componentType, PackageClass packageClass) {
        this.target = target;
        this.packageClass = packageClass;
        this.componentType = componentType;
    }

    public void setInjectionGenerator(InjectionGeneration injectionGenerator) {
        this.injectionGenerator = injectionGenerator;
    }

    public InjectionGeneration getInjectionGenerator() {
        return injectionGenerator;
    }

    public Collection<PreInjectionGeneration> getPreInjectionGenerators() {
        return preInjectionGenerators;
    }

    public Collection<PostInjectionGeneration> getPostInjectionGenerators() {
        return postInjectionGenerators;
    }

    public PackageClass getPackageClass() {
        return packageClass;
    }

    public ASTType getTarget() {
        return target;
    }

    public void setAnalysisContext(AnalysisContext analysisContext) {
        this.analysisContext = analysisContext;
    }

    public AnalysisContext getAnalysisContext() {
        return analysisContext;
    }

    public ASTType getType() {
        return componentType;
    }
}
