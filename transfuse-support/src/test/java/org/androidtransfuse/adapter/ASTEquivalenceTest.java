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
package org.androidtransfuse.adapter;

import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.compiletest.MemoryClassLoader;
import org.androidtransfuse.adapter.compiletest.SimpleClassLoader;
import org.androidtransfuse.adapter.compiletest.TestCompiler;
import org.androidtransfuse.adapter.element.*;
import org.androidtransfuse.util.MessagerLogger;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.*;
import javax.inject.Provider;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class ASTEquivalenceTest {

    private ASTClassFactory classFactory;

    @Before
    public void setup() {
        classFactory = new ASTClassFactory();
    }

    @SupportedAnnotationTypes("*")
    private static class CompareProcessor extends AbstractProcessor {

        private ASTType comparison;
        private Messager messager;
        private ASTTypeBuilderVisitor astTypeBuilderVisitor;
        private ASTElementFactory astElementFactory;
        private Set<String> scanned = new HashSet<String>();

        public CompareProcessor(ASTType comparison) {
            this.comparison = comparison;
        }

        @Override
        public synchronized void init(ProcessingEnvironment processingEnv) {
            super.init(processingEnv);

            messager = processingEnv.getMessager();
            Provider<ASTElementFactory> elementFactoryProvider = new Provider<ASTElementFactory>() {
                @Override
                public ASTElementFactory get() {
                    return astElementFactory;
                }
            };

            astTypeBuilderVisitor = new ASTTypeBuilderVisitor(elementFactoryProvider);

            ConcreteASTFactory concreteASTFactory = new ConcreteASTFactory();

            ElementConverterFactory elementConverterFactory = new ElementConverterFactory(astTypeBuilderVisitor, elementFactoryProvider, concreteASTFactory);

            ASTElementConverterFactory astElementConverterFactory =
                    new ASTElementConverterFactory(elementConverterFactory);


            concreteASTFactory.setAstElementFactoryProvider(elementFactoryProvider);
            concreteASTFactory.setElementConverterFactory(elementConverterFactory);

            astElementFactory = new ASTElementFactory(processingEnv.getElementUtils(),
                    concreteASTFactory,
                    astTypeBuilderVisitor,
                    astElementConverterFactory,
                    new MessagerLogger(processingEnv.getMessager(), false));
        }

        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            roundEnv.getRootElements();
            for (Element element : roundEnv.getRootElements()) {
                if(element.getSimpleName().toString().equals("TestClass")) {

                    ASTType astType =  element.asType().accept(astTypeBuilderVisitor, null);
                    equals(comparison, astType);


                }
            }
            return false;
        }

        private void equals(ASTGenericArgument comparison, ASTGenericArgument argument) {
            if(!comparison.equals(argument)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Argument " + comparison.getName() + " not equal to argument " + argument);
            }
        }

        private void equals(ASTType comparison, ASTType astType) {
            if(scanned.contains(comparison.getName())){
                return;
            }
            scanned.add(comparison.getName());
            if(comparison.getMethods().size() != astType.getMethods().size()) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Method count differs " + comparison.getMethods().size() + " vs " + astType.getMethods().size());
            }
            for (ASTMethod comparisonMethod : comparison.getMethods()) {
                MethodSignature comparisonMethodSignature = new MethodSignature(comparisonMethod);
                ASTMethod method = findMethod(astType, comparisonMethodSignature);
                if(method == null)  {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Method " + comparison.getName() + "." + comparisonMethod + " not found when comparing " + comparison.getName() + " to " + astType.getName());
                }
                else {
                    MethodSignature methodSignature = new MethodSignature(method);
                    equals("getMethod() -> " + comparisonMethodSignature.toString(), comparisonMethodSignature, methodSignature);
                    equals(comparisonMethod.getReturnType(), method.getReturnType());
                    equals("AccessModifier", comparisonMethod.getAccessModifier(), method.getAccessModifier());
                    equals("isStatic()", comparisonMethod.isStatic(), method.isStatic());
                    equals("isFinal()", comparisonMethod.isFinal(), method.isFinal());
                }
            }
            for (ASTField comparisonField : comparison.getFields()) {
                ASTField field = findField(astType, comparisonField.getName());
                if(field == null)  {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Field " + comparisonField + " not found.");
                }
                else {
                    equals(comparisonField.getASTType(), field.getASTType());
                    equals("getField() -> " + field.getASTType() + " Name ", comparisonField.getName(), field.getName());
                    equals("AccessModifier", comparisonField.getAccessModifier(), field.getAccessModifier());
                    equals("isTransient()", comparisonField.isTransient(), field.isTransient());
                    equals("isStatic()", comparisonField.isStatic(), field.isStatic());
                    equals("isFinal()", comparisonField.isFinal(), field.isFinal());
                }
            }
            for (ASTConstructor comparisonConstructor : comparison.getConstructors()) {
                ASTConstructor constructor = findConstructor(astType, comparisonConstructor);

                if( constructor == null) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Constructor " + comparisonConstructor + " not found.");
                }
                else {
                    for (int i = 0; i < comparisonConstructor.getParameters().size(); i++) {
                        ASTType comparisonParameterType = comparisonConstructor.getParameters().get(i).getASTType();
                        ASTType parameterType = constructor.getParameters().get(i).getASTType();

                        equals(comparisonParameterType, parameterType);
                    }
                }
            }
            equals("isConcreteClass", comparison.isConcreteClass(), astType.isConcreteClass());
            equals("isInterface", comparison.isInterface(), astType.isInterface());
            equals("isFinal", comparison.isFinal(), astType.isFinal());
            equals("isStatic", comparison.isStatic(), astType.isStatic());
            equals("isInnerClass", comparison.isInnerClass(), astType.isInnerClass());
            equals("isEnum", comparison.isEnum(), astType.isEnum());
            equals("isAbstract", comparison.isAbstract(), astType.isAbstract());
            equals("getSuperClass", comparison.getSuperClass(), astType.getSuperClass());
            if(comparison.getInterfaces().size() == astType.getInterfaces().size()) {
                for (ASTType comparisonInterfaceType : comparison.getInterfaces()) {
                    ASTType interfaceType = findType(comparisonInterfaceType, astType.getInterfaces());
                    equals(comparisonInterfaceType, interfaceType);
                }
            }
            else {
                messager.printMessage(Diagnostic.Kind.ERROR, "Interface lists of unequal size.");
            }

            if(comparison.getGenericArgumentTypes().size() == astType.getGenericArgumentTypes().size()) {
                for(int i = 0; i < comparison.getGenericArgumentTypes().size(); i++){
                    equals(comparison.getGenericArgumentTypes().get(i), astType.getGenericArgumentTypes().get(i));
                }
            }
            else {
                messager.printMessage(Diagnostic.Kind.ERROR, comparison.getName() + " and " + astType.getName() + "Generic Parametrs count differs" + comparison.getGenericArgumentTypes().size() + " vs " + astType.getGenericArgumentTypes().size());
            }

            if(comparison.getGenericArguments().size() == astType.getGenericArguments().size()) {
                for(int i = 0; i < comparison.getGenericArguments().size(); i++){
                    equals(comparison.getGenericArguments().get(i), astType.getGenericArguments().get(i));
                }
            }
            else {
                messager.printMessage(Diagnostic.Kind.ERROR, comparison.getName() + " and " + astType.getName() + "Generic Parametrs count differs" + comparison.getGenericArgumentTypes().size() + " vs " + astType.getGenericArgumentTypes().size());
            }


            equals("getPackageClass", comparison.getPackageClass(), astType.getPackageClass());
        }

        private ASTConstructor findConstructor(ASTType astType, ASTConstructor comparisonConstructor) {
            for (ASTConstructor constructor : astType.getConstructors()) {
                if(comparisonConstructor.getParameters().size() == constructor.getParameters().size()) {
                    boolean listEquals = true;
                    for (int i = 0; i < comparisonConstructor.getParameters().size(); i++) {
                        ASTType comparisonParameterType = comparisonConstructor.getParameters().get(i).getASTType();
                        ASTType parameterType = constructor.getParameters().get(i).getASTType();

                        if(!comparisonParameterType.equals(parameterType)){
                            listEquals = false;
                        }
                    }
                    if(listEquals) {
                        return constructor;
                    }
                }
            }
            return null;
        }

        private ASTField findField(ASTType astType, String name) {
            for (ASTField field : astType.getFields()) {
                if(field.getName().equals(name)){
                    return field;
                }
            }
            return null;
        }

        private ASTMethod findMethod(ASTType astType, MethodSignature comparisonMethodSignature) {
            for (ASTMethod method : astType.getMethods()) {
                MethodSignature methodSignature = new MethodSignature(method);
                if(methodSignature.equals(comparisonMethodSignature)){
                    return method;
                }
            }
            return null;
        }

        private ASTType findType(ASTType astType, Set<ASTType> interfaces) {
            for (ASTType type : interfaces) {
                if(astType.equals(type)){
                    return type;
                }
            }
            return null;
        }

        private void equals(String name, Object expected, Object comparison) {
            if(expected != comparison && expected != null && !expected.equals(comparison)) {
                messager.printMessage(Diagnostic.Kind.WARNING, "ASTType property " + name + " is not equals (" + expected + " != " + comparison + ")");
            }
        }

        @Override
        public SourceVersion getSupportedSourceVersion() {
            return SourceVersion.latestSupported();
        }
    }

    private static class ConcreteASTFactory implements ASTFactory {

        private ElementConverterFactory elementConverterFactory;
        private Provider<ASTElementFactory> astElementFactoryProvider;

        public void setElementConverterFactory(ElementConverterFactory elementConverterFactory){
            this.elementConverterFactory = elementConverterFactory;
        }

        public void setAstElementFactoryProvider(Provider<ASTElementFactory> astElementFactoryProvider) {
            this.astElementFactoryProvider = astElementFactoryProvider;
        }

        @Override
        public ASTElementAnnotation buildASTElementAnnotation(AnnotationMirror annotationMirror, ASTType type) {
            return new ASTElementAnnotation(annotationMirror, type, elementConverterFactory);
        }

        @Override
        public LazyElementParameterBuilder buildParameterBuilder(DeclaredType declaredType) {
            ASTTypeBuilderVisitor astTypeBuilderVisitor = new ASTTypeBuilderVisitor(astElementFactoryProvider);
            return new LazyElementParameterBuilder(declaredType, astTypeBuilderVisitor);
        }

        @Override
        public ASTGenericTypeWrapper buildGenericTypeWrapper(ASTType astType, LazyTypeParameterBuilder lazyTypeParameterBuilder) {
            return new ASTGenericTypeWrapper(astType, lazyTypeParameterBuilder);
        }
    }

    @Test
    public void testClassEquivalence() throws Exception {

        PackageClass testImplClassName = new PackageClass("example.test", "TestClass");
        PackageClass baseClassName = new PackageClass("example.test", "Base");
        PackageClass testClassName = new PackageClass("example.test", "Test");

        final String testImplValue = IOUtils.toString(ASTEquivalenceTest.class.getClassLoader().getResourceAsStream(testImplClassName.getCanonicalName().replace(".", "/") + ".java"));
        final String baseValue = IOUtils.toString(ASTEquivalenceTest.class.getClassLoader().getResourceAsStream(baseClassName.getCanonicalName().replace(".", "/") + ".java"));
        final String testValue = IOUtils.toString(ASTEquivalenceTest.class.getClassLoader().getResourceAsStream(testClassName.getCanonicalName().replace(".", "/") + ".java"));

        MemoryClassLoader classLoader = new MemoryClassLoader();

        Map<PackageClass, String> targetClassMap = new HashMap<PackageClass, String>();
        targetClassMap.put(testImplClassName, testImplValue);
        targetClassMap.put(baseClassName, baseValue);
        targetClassMap.put(testClassName, testValue);

        classLoader.add(targetClassMap);

        Class<?> testClass = Class.forName(testImplClassName.getCanonicalName(), true, classLoader);

        ASTType testClassType = classFactory.getType(testClass);

        MemoryClassLoader processorRunningClassLoader = new MemoryClassLoader();

        DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
        boolean pass = processorRunningClassLoader.add(targetClassMap, diagnosticListener, Collections.singletonList(new CompareProcessor(testClassType)));

        StringBuilder builder = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticListener.getDiagnostics()) {
            builder.append(diagnostic.getMessage(Locale.US));
        }

        assertTrue(builder.toString(), pass);
    }

    @Test
    public void example() throws ClassNotFoundException {
        TestCompiler compiler = new TestCompiler();
        String className = "example.test.TestClass";
        String source = "package example.test; public class TestClass{}";

        compiler.add(className, source);
        compiler.compile();

        byte[] byteCode = compiler.getByteCode(className);

        Class<?> aClass = Class.forName(className, true, new SimpleClassLoader(className, byteCode));
        assertEquals("example.test", aClass.getPackage().getName());
        assertEquals("TestClass", aClass.getSimpleName());
    }
}
