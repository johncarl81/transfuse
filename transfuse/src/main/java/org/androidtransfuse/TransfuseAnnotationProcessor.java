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
package org.androidtransfuse;

import com.google.auto.service.AutoService;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ReloadableASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementConverterFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ReloadableASTElementFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.config.TransfuseAndroidModule;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceComposite;
import org.androidtransfuse.plugins.PluginModule;
import org.androidtransfuse.processor.GenerateModuleProcessor;
import org.androidtransfuse.processor.TransfuseProcessor;
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.ManifestLocator;
import org.androidtransfuse.util.ManifestSerializer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Collections2.transform;

/**
 * Transfuse Annotation processor.  Kicks off the process of analyzing and generating code based on the compiled
 * codebase.
 *
 * To use this class, you simply have to annotate your classes with the proper root components (Activity,
 * Application, etc) and have this annotation processor on the classpath during a full compilation.
 *
 * This approach is compatible with Java 6 and above.
 *
 * See http://androidtransfuse.org for more details
 *
 * @author John Ericksen
 */
@SupportedAnnotations({
        Activity.class,
        Application.class,
        BroadcastReceiver.class,
        Service.class,
        Fragment.class,
        TransfuseModule.class,
        Factory.class,
        ImplementedBy.class,
        Install.class})
@Bootstrap
@AutoService(Processor.class)
public class TransfuseAnnotationProcessor extends AnnotationProcessorBase {

    @Inject
    private ASTElementConverterFactory astElementConverterFactory;
    @Inject
    private ManifestSerializer manifestParser;
    @Inject
    private RBuilder rBuilder;
    @Inject
    private ReloadableASTElementFactory reloadableASTElementFactory;
    @Inject
    private ReloadableASTClassFactory reloadableASTClassFactory;
    @Inject
    private ASTElementFactory astElementFactory;
    @Inject
    private ManifestLocator manifestLocator;
    @Inject
    private Logger log;
    @Inject
    @ScopeReference(ConfigurationScope.class)
    private EnterableScope configurationScope;
    @Inject
    private Provider<TransfuseProcessor> processorProvider;
    @Inject
    private Elements elements;
    private boolean baseModuleConfiguration = false;
    private int round = 0;

    @Override
    public void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Bootstraps.getInjector(TransfuseAnnotationProcessor.class)
                .add(Singleton.class, ScopeKey.of(ProcessingEnvironment.class), processingEnv)
                .inject(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        log.debug("Annotation procesing started, round " + round++);
        long start = System.currentTimeMillis();

        //setup transfuse processor with manifest and R classes
        File manifestFile = manifestLocator.findManifest();
        Manifest manifest = manifestParser.readManifest(manifestFile);

        RResourceComposite r = new RResourceComposite(
                buildR(rBuilder, manifest.getApplicationPackage() + ".R"),
                buildR(rBuilder, manifest.getApplicationPackage() + ".RBridge"),
                buildR(rBuilder, "android.R"));

        configurationScope.enter();

        configurationScope.seed(ScopeKey.of(File.class).annotatedBy("@javax.inject.Named(value=" + TransfuseAndroidModule.MANIFEST_FILE + ")"), manifestFile);
        configurationScope.seed(ScopeKey.of(RResource.class), r);
        configurationScope.seed(ScopeKey.of(Manifest.class).annotatedBy("@javax.inject.Named(value=" + TransfuseAndroidModule.ORIGINAL_MANIFEST + ")"), manifest);
        configurationScope.seed(ScopeKey.of(Boolean.class).annotatedBy("@javax.inject.Named(value=libraryProject)"), isLibraryProject(roundEnvironment));

        TransfuseProcessor transfuseProcessor = processorProvider.get();

        if (!baseModuleConfiguration) {
            transfuseProcessor.submit(TransfuseModule.class, reloadableASTClassFactory.apply(PluginModule.class));
            transfuseProcessor.submit(TransfuseModule.class, reloadableASTElementFactory.apply(elements.getTypeElement(APIModule.class.getName())));
            baseModuleConfiguration = true;
        }

        transfuseProcessor.submit(Application.class, buildASTCollection(roundEnvironment, Application.class));
        transfuseProcessor.submit(TransfuseModule.class, buildASTCollection(roundEnvironment, TransfuseModule.class));
        transfuseProcessor.submit(ImplementedBy.class, buildASTCollection(roundEnvironment, ImplementedBy.class));
        transfuseProcessor.submit(Factory.class, buildASTCollection(roundEnvironment, Factory.class));
        transfuseProcessor.submit(Activity.class, buildASTCollection(roundEnvironment, Activity.class));
        transfuseProcessor.submit(BroadcastReceiver.class, buildASTCollection(roundEnvironment, BroadcastReceiver.class));
        transfuseProcessor.submit(Service.class, buildASTCollection(roundEnvironment, Service.class));
        transfuseProcessor.submit(Fragment.class, buildASTCollection(roundEnvironment, Fragment.class));

        transfuseProcessor.execute();

        if (roundEnvironment.processingOver()) {
            transfuseProcessor.logErrors();
        }

        log.debug("Took " + (System.currentTimeMillis() - start) + "ms to process");

        configurationScope.exit();

        return true;
    }

    private RResource buildR(RBuilder rBuilder, String className) {
        TypeElement rTypeElement = elements.getTypeElement(className);
        if (rTypeElement != null) {
            return rBuilder.buildR(astElementFactory.getType(rTypeElement).getInnerTypes());
        }
        return null;
    }

    private Collection<Provider<ASTType>> buildASTCollection(RoundEnvironment round, Class<? extends Annotation> annotation) {
        ImmutableSet<? extends Element> components = ImmutableSet.<Element>builder()
                .addAll(findInstalledComponents(round, annotation))
                .addAll(round.getElementsAnnotatedWith(annotation))
                .build();

        return reloadableASTElementFactory.buildProviders(
                FluentIterable.from(components)
                        .filter(new Predicate<Element>() {
                            public boolean apply(Element element) {
                                //we're only dealing with TypeElements
                                return element instanceof TypeElement;
                            }
                        })
                        .transform(new Function<Element, TypeElement>() {
                            public TypeElement apply(Element element) {
                                return (TypeElement) element;
                            }
                        })
                        .toList());
    }

    private Set<? extends TypeElement> findInstalledComponents(RoundEnvironment round, final Class<? extends Annotation> annotation) {
        Collection<ASTType> annotatedClasses =transform(round.getElementsAnnotatedWith(Install.class),
                astElementConverterFactory.buildASTElementConverter(ASTType.class));

        ImmutableSet.Builder<TypeElement> builder = ImmutableSet.builder();

        for (ASTType annotatedClass : annotatedClasses) {
            ASTAnnotation installAstAnnotation = annotatedClass.getASTAnnotation(Install.class);

            ASTType[] values = installAstAnnotation.getProperty("value", ASTType[].class);

            for (ASTType value : values) {
                if(value.isAnnotated(annotation)) {
                    builder.add(elements.getTypeElement(value.getName()));
                }
            }
        }

        return builder.build();
    }

    private Collection<ASTType> wrapASTCollection(Collection<? extends TypeElement> elementCollection) {
        return transform(elementCollection,
                astElementConverterFactory.buildASTElementConverter(ASTType.class)
        );
    }

    @Override
    public Set<String> getSupportedOptions() {
        return ImmutableSet.of(
                GenerateModuleProcessor.MANIFEST_PROCESSING_OPTION,
                ManifestLocator.ANDROID_MANIFEST_FILE_OPTION);
    }

    private boolean isLibraryProject(RoundEnvironment roundEnvironment) {
        for(ASTType modules : wrapASTCollection(findInstalledComponents(roundEnvironment, TransfuseModule.class))) {
            if(modules.getAnnotation(TransfuseModule.class).library()) {
                return true;
            }
        }
        return false;
    }
}
