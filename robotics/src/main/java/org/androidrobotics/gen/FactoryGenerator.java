package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FactoryDescriptor;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.SingletonDescriptor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class FactoryGenerator {

    public static final String BUILDER_METHOD = "buildInstance";

    private JCodeModel codeModel;
    private SingletonCodeBuilder singletonCodeBuilder;
    private Map<String, FactoryDescriptor> factoryMap = new HashMap<String, FactoryDescriptor>();

    @Inject
    public FactoryGenerator(JCodeModel codeModel, SingletonCodeBuilder singletonCodeBuilder) {
        this.codeModel = codeModel;
        this.singletonCodeBuilder = singletonCodeBuilder;
    }

    public FactoryDescriptor buildFactory(InjectionNode injectionNode) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (!factoryMap.containsKey(injectionNode.getClassName())) {
            FactoryDescriptor descriptor = innerBuildFactory(injectionNode);
            factoryMap.put(injectionNode.getClassName(), descriptor);
        }
        return factoryMap.get(injectionNode.getClassName());
    }

    private FactoryDescriptor innerBuildFactory(InjectionNode injectionNode) throws ClassNotFoundException, JClassAlreadyExistsException {

        JDefinedClass factoryClass = codeModel._class(JMod.PUBLIC, injectionNode.getClassName() + "Factory", ClassType.CLASS);

        //singleton constructor
        SingletonDescriptor singletonDescriptor = singletonCodeBuilder.makeSingleton(factoryClass);

        JBlock constructorBody = singletonDescriptor.getConstructor().body();

        String name = injectionNode.getClassName().substring(injectionNode.getClassName().lastIndexOf('.') + 1);
        String packageName = injectionNode.getClassName().substring(0, injectionNode.getClassName().lastIndexOf('.'));

        JType returnType = codeModel.parseType(name);
        JMethod buildInstanceMethod = factoryClass.method(JMod.PUBLIC, returnType, BUILDER_METHOD);
        JBlock buildIntanceBody = buildInstanceMethod.body();


        JVar variable = buildIntanceBody.decl(codeModel.parseType(injectionNode.getClassName()), "activityDelegate");


        //constructor injection
        buildIntanceBody.assign(variable,
                buildConstructorCall(injectionNode, returnType, constructorBody, factoryClass));


        //parameter injection
        buildParameterInjection(injectionNode, variable, buildIntanceBody, packageName);

        //method injection
        buildMethodInjection(injectionNode);

        buildIntanceBody._return(variable);

        return new FactoryDescriptor(factoryClass, singletonDescriptor.getGetInstanceMethod().name(), returnType, buildInstanceMethod.name());
    }

    private void buildMethodInjection(InjectionNode injectionNode) {

    }

    private void buildParameterInjection(InjectionNode injectionNode, JVar variable, JBlock buildIntanceBody, String packageName) throws ClassNotFoundException {
        /*JTryBlock jTryBlock = buildIntanceBody._try();

        JBlock tryBuildInstancebody = jTryBlock.body();

        JVar fieldVariable = tryBuildInstancebody.decl(codeModel.parseType("java.lang.reflect.Field"), "field");
        tryBuildInstancebody.assign(fieldVariable, codeModel.ref(injectionNode.getClassName()).dotclass()
                .invoke("getDeclaredField").arg("controller"));


        tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.TRUE));
        tryBuildInstancebody.add(fieldVariable.invoke("set").arg(variable).arg(JExpr._new(codeModel.parseType((packageName + ".TestController")))));
        tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.FALSE));

        jTryBlock._catch(codeModel.directClass("java.lang.IllegalAccessException"));
        jTryBlock._catch(codeModel.directClass("java.lang.NoSuchFieldException"));*/
    }

    private JInvocation buildConstructorCall(InjectionNode injectionNode, JType returnType, JBlock constructorBody, JDefinedClass factoryClass) throws ClassNotFoundException, JClassAlreadyExistsException {
        ConstructorInjectionPoint injectionPoint = injectionNode.getConstructorInjectionPoint();

        JInvocation constructorInvocation = JExpr._new(returnType);

        for (InjectionNode node : injectionPoint.getInjectionNodes()) {

            FactoryDescriptor descriptor = buildFactory(node);

            String name = node.getClassName().substring(node.getClassName().lastIndexOf('.') + 1).toLowerCase();

            JFieldVar factoryField = factoryClass.field(JMod.PRIVATE, descriptor.getClassDefinition(), name + "Factory");

            constructorBody.assign(factoryField, descriptor.getClassDefinition().staticInvoke(descriptor.getInstanceMethodName()));

            constructorInvocation = constructorInvocation.arg(factoryField.invoke(descriptor.getBuilderMethodName()));
        }

        return constructorInvocation;
    }
}
