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
package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.gen.classloader.MemoryClassLoader;
import org.androidtransfuse.gen.invocationBuilder.PackageHelperGenerator;
import org.androidtransfuse.model.TypedExpression;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@Bootstrap
public class InvocationBuilderTest {

    private static final Map<String, List<String>> PROPERTIES = new HashMap<String, List<String>>(){{
        put("Target", Arrays.asList("One", "Two", "Three", "Four"));
        put("Subclass", Arrays.asList("Four"));
        put("SubDiffPackage", Arrays.asList(/*"Three",*/ "Four"));
    }};

    private static final Map<String, String> METHOD_EXPECTATIONS = new HashMap<String, String>(){{
        put("getOne", "org.androidtransfuse.gen.Target:One");
        put("getTwo", "org.androidtransfuse.gen.Target:Two");
        put("getThree", "org.androidtransfuse.gen.Target:Three");
        put("getPrivateFour", "org.androidtransfuse.gen.Target:Four");
        put("getSuperFour", "org.androidtransfuse.gen.Subclass:Four");
        //put("getDiffPkgThree", "org.androidtransfuse.gen.SubDiffPackage:Three");
        put("getDiffPkgFour()", "org.androidtransfuse.gen.SubDiffPackage:Four");
    }};

    private static final List<String> METHOD_NULL_EXPECTATIONS = new ArrayList<String>(){{
        add("getSuperOne");
        add("getSuperTwo");
        add("getSuperThree");
        add("getDiffPkgOne");
        add("getDiffPkgTwo");
        add("getDiffPkgFour");
    }};

    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private InvocationBuilder invocationBuilder;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private ClassGenerationUtil generationUtil;
    @Inject
    private PackageHelperGenerator packageHelperGenerator;

    private Class targetClass;

    @Before
    public void setup() throws ClassNotFoundException, IOException {
        Bootstraps.inject(this);

        MemoryClassLoader classLoader = codeGenerationUtil.getClassLoader();

        PackageClass subDiffPackageClass = new PackageClass("org.androidtransfuse.gen", "SubDiffPackage");
        PackageClass subPackageClass = new PackageClass("org.androidtransfuse.gen", "Subclass");
        PackageClass targetPackageClass = new PackageClass("org.androidtransfuse.gen", "Target");

        Map<String, String> source = new LinkedHashMap<String, String>();

        source.put(subDiffPackageClass.getCanonicalName(), loadJavaResource(subDiffPackageClass));
        source.put(subPackageClass.getCanonicalName(), loadJavaResource(subPackageClass));
        source.put(targetPackageClass.getCanonicalName(), loadJavaResource(targetPackageClass));

        classLoader.add(source);

        targetClass = classLoader.loadClass(targetPackageClass.getCanonicalName());
        Class subDiffClass = classLoader.loadClass(targetPackageClass.getCanonicalName());

        assertNotNull(subDiffClass);
    }

    private String loadJavaResource(PackageClass packageClass) throws IOException {

        String path = "/" + packageClass.getCanonicalName().replace(".", "/") + ".java";

        return IOUtils.toString(InvocationBuilderTest.class.getResourceAsStream(path));
    }

    @Test
    public void testBuilderFieldModifiers() throws Exception {

        ASTType targetType = astClassFactory.getType(targetClass);
        PackageClass testerName = targetType.getPackageClass().append("Tester");

        // define class to test field invocations
        JDefinedClass testerClass = generationUtil.defineClass(testerName);
        testerClass._implements(Runnable.class);

        JMethod runMethod = testerClass.method(JMod.PUBLIC, void.class, "run");
        JBlock body = runMethod.body();

        JVar targetVar = body.decl(generationUtil.ref(targetType), "target", invocationBuilder.buildConstructorCall(targetType.getConstructors().iterator().next(), targetType, Collections.EMPTY_LIST));

        // build field setters
        for(ASTType superIter = targetType; !superIter.equals(astClassFactory.getType(Object.class)); superIter = superIter.getSuperClass()){
            TypedExpression container = new TypedExpression(superIter, targetVar);
            for (ASTField field : superIter.getFields()) {
                body.add(invocationBuilder.buildFieldSet(field, container, new TypedExpression(astClassFactory.getType(String.class), JExpr.lit(superIter.getName() + ":" + field.getName()))));
            }
        }

        // build asserts against generated field getters
        for(ASTType superIter = targetType; !superIter.equals(astClassFactory.getType(Object.class)); superIter = superIter.getSuperClass()){
            TypedExpression container = new TypedExpression(superIter, targetVar);
            for (ASTField field : superIter.getFields()) {
                body.staticInvoke(generationUtil.ref(Assert.class), "assertEquals").arg(JExpr.lit(superIter.getName() + ":" + field.getName())).arg(invocationBuilder.buildFieldGet(field, container));
            }
        }

        packageHelperGenerator.generate();

        ClassLoader classLoader = codeGenerationUtil.build(true);
        Class<Runnable> tester = (Class<Runnable>) classLoader.loadClass(testerName.getCanonicalName());

        tester.newInstance().run();
    }

    @Test
    public void testBuilderMethodModifiers() throws Exception {

        ASTType targetType = astClassFactory.getType(targetClass);
        PackageClass testerName = targetType.getPackageClass().append("Tester");

        // define class to test field invocations
        JDefinedClass testerClass = generationUtil.defineClass(testerName);
        testerClass._implements(Runnable.class);

        JMethod runMethod = testerClass.method(JMod.PUBLIC, void.class, "run");
        JBlock body = runMethod.body();

        JVar targetVar = body.decl(generationUtil.ref(targetType), "target", invocationBuilder.buildConstructorCall(targetType.getConstructors().iterator().next(), targetType, Collections.EMPTY_LIST));

        // build field setters
        for(ASTType superIter = targetType; !superIter.equals(astClassFactory.getType(Object.class)); superIter = superIter.getSuperClass()){
            TypedExpression container = new TypedExpression(superIter, targetVar);
            if(PROPERTIES.containsKey(superIter.getPackageClass().getClassName())){
                List<String> setMethods = PROPERTIES.get(superIter.getPackageClass().getClassName());
                for (ASTMethod method : superIter.getMethods()) {
                    String property = method.getName().replace("set", "");
                    if(method.getName().startsWith("set") && setMethods.contains(property)){
                        body.add(invocationBuilder.buildMethodCall(method, Collections.singletonList(JExpr.lit(superIter.getName() + ":" + property)), container));
                    }
                }
            }
        }

        // build asserts against generated field getters
        TypedExpression container = new TypedExpression(targetType, targetVar);
        for(ASTType superIter = targetType; !superIter.equals(astClassFactory.getType(Object.class)); superIter = superIter.getSuperClass()){
            for (ASTMethod method : superIter.getMethods()) {
                if(METHOD_EXPECTATIONS.containsKey(method.getName())){
                    body.staticInvoke(generationUtil.ref(Assert.class), "assertEquals").arg(JExpr.lit(METHOD_EXPECTATIONS.get(method.getName()))).arg(invocationBuilder.buildMethodCall(method, Collections.EMPTY_LIST, container));
                }
            }
        }

        for (ASTMethod method : targetType.getMethods()) {
            if(METHOD_NULL_EXPECTATIONS.contains(method.getName())){
                body.staticInvoke(generationUtil.ref(Assert.class), "assertNull").arg(invocationBuilder.buildMethodCall(method, Collections.EMPTY_LIST, container));
            }
        }

        packageHelperGenerator.generate();

        ClassLoader classLoader = codeGenerationUtil.build();
        Class<Runnable> tester = (Class<Runnable>) classLoader.loadClass(testerName.getCanonicalName());

        tester.newInstance().run();
    }

}
