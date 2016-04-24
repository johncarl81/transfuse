/**
 * Copyright 2014-2015 John Ericksen
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
package org.rbridge;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.androidtransfuse.AnnotationProcessorBase;
import org.androidtransfuse.SupportedAnnotations;
import org.androidtransfuse.adapter.element.ReloadableASTElementFactory;
import org.androidtransfuse.annotations.ScopeReference;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.scope.ScopeKey;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author John Ericksen
 */
@SupportedAnnotations({Bridge.class})
@Bootstrap
//@AutoService(Processor.class)
public class RBridgeAnnotationProcessor extends AnnotationProcessorBase {

    @Inject
    private RBridgeProcessor rBridgeProcessor;
    @Inject
    private ReloadableASTElementFactory reloadableASTElementFactory;
    @Inject
    @ScopeReference(ProcessingScope.class)
    private EnterableScope processingScope;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Bootstraps.getInjector(RBridgeAnnotationProcessor.class)
                .add(Singleton.class, ScopeKey.of(ProcessingEnvironment.class), processingEnv)
                .inject(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        processingScope.enter();

        processingScope.seed(ScopeKey.of(RoundEnvironment.class), roundEnvironment);

        rBridgeProcessor.submit(Bridge.class, reloadableASTElementFactory.buildProviders(
                FluentIterable.from(roundEnvironment.getElementsAnnotatedWith(Bridge.class))
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
                        .toList()));

        rBridgeProcessor.execute();

        if (roundEnvironment.processingOver()) {
            // Throws an exception if errors still exist.
            rBridgeProcessor.checkForErrors();
        }

        processingScope.exit();

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
