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
package org.androidtransfuse.analysis;

import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.analysis.astAnalyzer.ASTAnalysis;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

/**
 * Analysis class for ASTType Injection Analysis
 *
 * @author John Ericksen
 */
public class Analyzer {

    private static final ASTType OBJECT_TYPE = new ASTStringType(Object.class.getName());

    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    private Logger log;

    public InjectionNode analyze(InjectionSignature signature, AnalysisContext context){
        return analyze(signature, signature, context);
    }

    public InjectionNode analyze(ASTType astType, ASTType concreteType, AnalysisContext context){
        return analyze(new InjectionSignature(astType), new InjectionSignature(concreteType), context);
    }

    /**
     * Analyze the given ASTType and produces a corresponding InjectionNode with the contained
     * AST injection related elements (constructor, method, field) recursively analyzed for InjectionNodes
     *
     * @param signature Injection Signature
     * @param concreteType required type
     * @param context      required
     * @return InjectionNode root
     */
    public InjectionNode analyze(final InjectionSignature signature, final InjectionSignature concreteType, final AnalysisContext context) {

        InjectionNode injectionNode;

        if (context.isDependent(concreteType.getType())) {
            //if this type is a dependency of itself, we've found a back link.
            //This dependency loop must be broken using a virtual proxy
            injectionNode = context.getInjectionNode(concreteType.getType());

            Collection<InjectionNode> loopedDependencies = context.getDependencyHistory();

            InjectionNode proxyDependency = findProxyableDependency(injectionNode, loopedDependencies);

            if (proxyDependency == null) {
                throw new TransfuseAnalysisException("Unable to find a dependency to proxy");
            }

            VirtualProxyAspect proxyAspect = getProxyAspect(proxyDependency);
            proxyAspect.getProxyInterfaces().add(proxyDependency.getUsageType());

        } else {
            injectionNode = new InjectionNode(signature, concreteType);
            //default variable builder
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

            AnalysisContext nextContext = context.addDependent(injectionNode);

            //loop over super classes (extension and implements)
            scanClassHierarchy(concreteType.getType(), injectionNode, nextContext);
        }

        return injectionNode;
    }

    private InjectionNode findProxyableDependency(InjectionNode duplicateDependency, Collection<InjectionNode> loopedDependencies) {
        //there may be better ways to identify the proxyable injection node.
        if (!duplicateDependency.getUsageType().isConcreteClass()) {
            return duplicateDependency;
        }
        for (InjectionNode loopedDependency : loopedDependencies) {
            if (!loopedDependency.getUsageType().isConcreteClass()) {
                //found interface
                return loopedDependency;
            }
        }
        return null;
    }

    private void scanClassHierarchy(ASTType type, InjectionNode injectionNode, AnalysisContext context) {
        scanClassHierarchy(new HashSet<MethodSignature>(), new HashMap<String, Set<MethodSignature>>(), type, injectionNode, context);
    }

    private void scanClassHierarchy(Set<MethodSignature> scanned, Map<String, Set<MethodSignature>> packagePrivateScanned, ASTType type, InjectionNode injectionNode, AnalysisContext context){
        log.debug("Analyzing class: " + type);
        for (ASTAnalysis analysis : context.getInjectionNodeBuilders().getAnalysisRepository()) {

            analysis.analyzeType(injectionNode, type, context);

            for (ASTMethod astMethod : type.getMethods()) {
                if(!isOverridden(scanned, packagePrivateScanned, type, astMethod)){
                    analysis.analyzeMethod(injectionNode, type, astMethod, context);
                }
            }

            for (ASTField astField : type.getFields()) {
                analysis.analyzeField(injectionNode, type, astField, context);
            }
        }

        for (ASTMethod astMethod : type.getMethods()) {
            MethodSignature signature = new MethodSignature(astMethod);
            if(astMethod.getAccessModifier() == ASTAccessModifier.PUBLIC ||
               astMethod.getAccessModifier() == ASTAccessModifier.PROTECTED){
                scanned.add(signature);
            }
            else if(astMethod.getAccessModifier() == ASTAccessModifier.PACKAGE_PRIVATE){
                if(!packagePrivateScanned.containsKey(type.getPackageClass().getPackage())){
                    packagePrivateScanned.put(type.getPackageClass().getPackage(), new HashSet<MethodSignature>());
                }
                packagePrivateScanned.get(type.getPackageClass().getPackage()).add(signature);
            }
        }

        if (type.getSuperClass() != null && !type.getSuperClass().equals(OBJECT_TYPE)) {
            scanClassHierarchy(scanned, packagePrivateScanned, type.getSuperClass(), injectionNode, context);
        }

    }

    private boolean isOverridden(Set<MethodSignature> scanned, Map<String, Set<MethodSignature>> packagePrivateScanned, ASTType type, ASTMethod method) {
        MethodSignature signature = new MethodSignature(method);

        if(method.getAccessModifier() == ASTAccessModifier.PRIVATE){
            return false;
        }

        if(method.getAccessModifier() == ASTAccessModifier.PACKAGE_PRIVATE){
            return packagePrivateScanned.containsKey(type.getPackageClass().getPackage()) &&
                   packagePrivateScanned.get(type.getPackageClass().getPackage()).contains(signature);
        }

        // PUBLIC and PROTECTED handling
        return scanned.contains(signature);
    }

    private VirtualProxyAspect getProxyAspect(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(VirtualProxyAspect.class)) {
            injectionNode.addAspect(new VirtualProxyAspect());
        }
        return injectionNode.getAspect(VirtualProxyAspect.class);
    }

    @Inject
    public void setVariableInjectionBuilderProvider(Provider<VariableInjectionBuilder> variableInjectionBuilderProvider) {
        this.variableInjectionBuilderProvider = variableInjectionBuilderProvider;
    }

    @Inject
    public void setLog(Logger log) {
        this.log = log;
    }
}
