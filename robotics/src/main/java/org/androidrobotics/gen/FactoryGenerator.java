package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.model.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class FactoryGenerator {

    public static final String BUILDER_METHOD = "get";

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

        JClass providerInterface = codeModel.ref(Provider.class).narrow(codeModel.ref(injectionNode.getClassName()));

        factoryClass._implements(providerInterface);

        //singleton constructor
        SingletonDescriptor singletonDescriptor = singletonCodeBuilder.makeSingleton(factoryClass);

        JBlock constructorBody = singletonDescriptor.getConstructor().body();

        String name = injectionNode.getClassName().substring(injectionNode.getClassName().lastIndexOf('.') + 1);

        JType returnType = codeModel.parseType(name);
        JMethod buildInstanceMethod = factoryClass.method(JMod.PUBLIC, returnType, BUILDER_METHOD);
        JBlock buildInstanceBody = buildInstanceMethod.body();

        JVar variable = buildInstanceBody.decl(codeModel.parseType(injectionNode.getClassName()), "activityDelegate");

        //constructor injection
        buildInstanceBody.assign(variable,
                buildConstructorCall(injectionNode.getConstructorInjectionPoint(), returnType, constructorBody, factoryClass));

        //field injection
        for (FieldInjectionPoint fieldInjectionPoint : injectionNode.getFieldInjectionPoints()) {
            buildParameterInjection(injectionNode, fieldInjectionPoint, variable, buildInstanceBody, constructorBody, factoryClass);
        }

        //method injection
        for (MethodInjectionPoint methodInjectionPoint : injectionNode.getMethodInjectionPoints()) {
            buildMethodInjection(methodInjectionPoint, variable, buildInstanceBody, constructorBody, factoryClass);
        }

        buildInstanceBody._return(variable);

        return new FactoryDescriptor(factoryClass, singletonDescriptor.getGetInstanceMethod().name(), returnType, buildInstanceMethod.name());
    }

    private void buildMethodInjection(MethodInjectionPoint methodInjectionPoint, JVar variable, JBlock buildInstancebody, JBlock constructorBody, JDefinedClass factoryClass) throws ClassNotFoundException, JClassAlreadyExistsException {

        JInvocation methodInvocation = variable.invoke(methodInjectionPoint.getName());
        for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
            FactoryDescriptor descriptor = buildFactory(injectionNode);

            String name = injectionNode.getClassName().substring(injectionNode.getClassName().lastIndexOf('.') + 1).toLowerCase();

            JFieldVar factoryField = factoryClass.field(JMod.PRIVATE, descriptor.getClassDefinition(), name + "Factory");

            constructorBody.assign(factoryField, descriptor.getClassDefinition().staticInvoke(descriptor.getInstanceMethodName()));

            methodInvocation.arg(factoryField.invoke(descriptor.getBuilderMethodName()));
        }

        buildInstancebody.add(methodInvocation);

    }

    private void buildParameterInjection(InjectionNode injectionNode, FieldInjectionPoint fieldInjectionPoint, JVar variable, JBlock buildIntanceBody, JBlock constructorBody, JDefinedClass factoryClass) throws ClassNotFoundException, JClassAlreadyExistsException {
        JTryBlock jTryBlock = buildIntanceBody._try();

        JBlock tryBuildInstancebody = jTryBlock.body();

        FactoryDescriptor descriptor = buildFactory(fieldInjectionPoint.getInjectionNode());
        InjectionNode node = fieldInjectionPoint.getInjectionNode();

        String name = node.getClassName().substring(node.getClassName().lastIndexOf('.') + 1).toLowerCase();

        JFieldVar factoryField = factoryClass.field(JMod.PRIVATE, descriptor.getClassDefinition(), name + "Factory");

        constructorBody.assign(factoryField, descriptor.getClassDefinition().staticInvoke(descriptor.getInstanceMethodName()));


        JVar fieldVariable = tryBuildInstancebody.decl(codeModel.parseType("java.lang.reflect.Field"), "field");
        tryBuildInstancebody.assign(fieldVariable, codeModel.ref(injectionNode.getClassName()).dotclass()
                .invoke("getDeclaredField").arg(fieldInjectionPoint.getName()));


        tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.TRUE));
        tryBuildInstancebody.add(fieldVariable.invoke("set").arg(variable).arg(factoryField.invoke(descriptor.getBuilderMethodName())));
        tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.FALSE));

        JCatchBlock illegalAccessExceptionBlock = jTryBlock._catch(codeModel.directClass("java.lang.IllegalAccessException"));
        JVar e1 = illegalAccessExceptionBlock.param("e");
        illegalAccessExceptionBlock.body().add(e1.invoke("printStackTrace"));
        JCatchBlock noSuchFieldExceptionBlock = jTryBlock._catch(codeModel.directClass("java.lang.NoSuchFieldException"));
        JVar e2 = noSuchFieldExceptionBlock.param("e");
        noSuchFieldExceptionBlock.body().add(e2.invoke("printStackTrace"));
    }

    private JInvocation buildConstructorCall(ConstructorInjectionPoint injectionNode, JType returnType, JBlock constructorBody, JDefinedClass factoryClass) throws ClassNotFoundException, JClassAlreadyExistsException {
        JInvocation constructorInvocation = JExpr._new(returnType);

        for (InjectionNode node : injectionNode.getInjectionNodes()) {

            FactoryDescriptor descriptor = buildFactory(node);

            String name = node.getClassName().substring(node.getClassName().lastIndexOf('.') + 1).toLowerCase();

            JFieldVar factoryField = factoryClass.field(JMod.PRIVATE, descriptor.getClassDefinition(), name + "Factory");

            constructorBody.assign(factoryField, descriptor.getClassDefinition().staticInvoke(descriptor.getInstanceMethodName()));

            constructorInvocation = constructorInvocation.arg(factoryField.invoke(descriptor.getBuilderMethodName()));
        }

        return constructorInvocation;
    }
}
