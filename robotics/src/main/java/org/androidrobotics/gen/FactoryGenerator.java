package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.TypeAnalysisBridge;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FactoryGenerator {

    public static final String BUILDER_METHOD = "buildInstance";

    private JCodeModel codeModel;
    private SingletonCodeBuilder singletonCodeBuilder;

    @Inject
    public FactoryGenerator(JCodeModel codeModel, SingletonCodeBuilder singletonCodeBuilder) {
        this.codeModel = codeModel;
        this.singletonCodeBuilder = singletonCodeBuilder;
    }

    public FactoryDescriptor buildFactory(TypeAnalysisBridge analysisBridge) throws ClassNotFoundException, JClassAlreadyExistsException {

        JDefinedClass factoryClass = codeModel._class(JMod.PUBLIC, analysisBridge.getName() + "Factory", ClassType.CLASS);

        //singleton constructor
        SingletonDescriptor singletonDescriptor = singletonCodeBuilder.makeSingleton(factoryClass);

        String name = analysisBridge.getName().substring(analysisBridge.getName().lastIndexOf('.') + 1);
        String packageName = analysisBridge.getName().substring(0, analysisBridge.getName().lastIndexOf('.'));

        JType returnType = codeModel.parseType(name);
        JMethod buildInstanceMethod = factoryClass.method(JMod.PUBLIC, codeModel.parseType(name), BUILDER_METHOD);
        JBlock buildIntanceBody = buildInstanceMethod.body();


        JVar variable = buildIntanceBody.decl(codeModel.parseType(analysisBridge.getName()), "activityDelegate");

        //constructor injection
        buildIntanceBody.assign(variable, JExpr._new(codeModel.parseType(name)));

        //parameter injection

        //method injection
        {
            JTryBlock jTryBlock = buildIntanceBody._try();

            JBlock tryBuildInstancebody = jTryBlock.body();

            JVar fieldVariable = tryBuildInstancebody.decl(codeModel.parseType("java.lang.reflect.Field"), "field");
            tryBuildInstancebody.assign(fieldVariable, codeModel.ref(analysisBridge.getName()).dotclass()
                    .invoke("getDeclaredField").arg("controller"));


            tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.TRUE));
            tryBuildInstancebody.add(fieldVariable.invoke("set").arg(variable).arg(JExpr._new(codeModel.parseType((packageName + ".TestController")))));
            tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.FALSE));

            jTryBlock._catch(codeModel.directClass("java.lang.IllegalAccessException"));
            jTryBlock._catch(codeModel.directClass("java.lang.NoSuchFieldException"));
        }


        buildIntanceBody._return(variable);

        return new FactoryDescriptor(factoryClass, singletonDescriptor.getGetInstanceMethod().name(), returnType, buildInstanceMethod.name());
    }
}
