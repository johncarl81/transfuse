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
package org.androidtransfuse.bootstrap;

import com.google.common.collect.ImmutableSet;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.AnnotationProcessorBase;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementConverterFactory;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.module.ModuleProcessor;
import org.androidtransfuse.annotations.Injector;
import org.androidtransfuse.gen.InjectorGenerator;
import org.androidtransfuse.gen.InjectorsGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.Providers;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.inject.Provider;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Collections2.transform;

/**
 * @author John Ericksen
 */
@SupportedAnnotations({Bootstrap.class, BootstrapModule.class})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BootstrapProcessor extends AnnotationProcessorBase {

    private CoreFactory coreFactory;
    private boolean ran = false;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        coreFactory = new CoreFactory(processingEnv.getElementUtils(), processingEnv.getMessager(), processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if(!ran){

            try {

                InjectionPointFactory injectionPointFactory = coreFactory.buildInjectionPointFactory();

                Collection<? extends ASTType> moduleTypes =  wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(BootstrapModule.class));

                //configure injections
                ModuleProcessor moduleProcessor = coreFactory.buildModuleProcessor();

                for (ASTType moduleType : moduleTypes) {
                    moduleProcessor.process(moduleType);
                }

                ImmutableSet.Builder<ASTType> injectorTypesBuilder = ImmutableSet.builder();
                injectorTypesBuilder.addAll(wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(Injector.class)));
                injectorTypesBuilder.addAll(coreFactory.getModuleRepository().getInstalledAnnotatedWith(Injector.class));

                ImmutableSet<ASTType> injectorTypes = injectorTypesBuilder.build();

                coreFactory.registerInjectors(injectorTypes);

                InjectorGenerator injectorGenerator = coreFactory.buildInjectorGenerator();
                Map<Provider<ASTType>, JDefinedClass> injectorAggregate = new HashMap<Provider<ASTType>, JDefinedClass>();
                for (ASTType injectorType : injectorTypes) {
                    JDefinedClass generated = injectorGenerator.generate(injectorType);
                    injectorAggregate.put(Providers.of(injectorType), generated);
                }

                Collection<? extends ASTType> astTypes = wrapASTCollection(roundEnvironment.getElementsAnnotatedWith(Bootstrap.class));
                BootstrapsInjectorGenerator bootstrapsInjectorGenerator = coreFactory.buildBootstrapsInjectorGenerator();

                AnalysisContext context = coreFactory.buildAnalysisContext();
                for (ASTType astType : astTypes) {

                    ASTAnnotation astAnnotation = astType.getASTAnnotation(Bootstrap.class);
                    Boolean testInjector = astAnnotation.getProperty("test", Boolean.class);
                    testInjector = testInjector == null? false : testInjector;

                    InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(astType, context);

                    bootstrapsInjectorGenerator.generate(injectionNode, testInjector);
                }

                InjectorsGenerator injectorsGenerator = coreFactory.buildInjectorsGenerator();
                injectorsGenerator.generateInjectors(injectorAggregate);

                JCodeModel codeModel = coreFactory.getCodeModel();
                codeModel.build(coreFactory.buildCodeWriter(), coreFactory.buildResourceWriter());

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            ran = true;
        }


        return true;
    }

    private Collection<ASTType> wrapASTCollection(Collection<? extends Element> elementCollection) {
        ASTElementConverterFactory converterFactory = coreFactory.buildConverterFactory();

        return transform(elementCollection, converterFactory.buildASTElementConverter(ASTType.class)
        );
    }
}
