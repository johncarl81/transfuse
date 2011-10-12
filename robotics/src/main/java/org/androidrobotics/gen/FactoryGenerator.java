package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.AnalysisBridge;
import org.androidrobotics.analysis.ElementAnalysisBridge;
import org.androidrobotics.analysis.TypeAnalysisBridge;
import org.androidrobotics.analysis.VariableElementAnalysisBridge;
import org.androidrobotics.util.RoboticsGenerationException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
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

    public FactoryDescriptor buildFactory(TypeAnalysisBridge analysisBridge) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (!factoryMap.containsKey(analysisBridge.getName())) {
            FactoryDescriptor descriptor = innerBuildFactory(analysisBridge);
            factoryMap.put(analysisBridge.getName(), descriptor);
        }
        return factoryMap.get(analysisBridge.getName());
    }

    private FactoryDescriptor innerBuildFactory(TypeAnalysisBridge analysisBridge) throws ClassNotFoundException, JClassAlreadyExistsException {

        JDefinedClass factoryClass = codeModel._class(JMod.PUBLIC, analysisBridge.getName() + "Factory", ClassType.CLASS);

        //singleton constructor
        SingletonDescriptor singletonDescriptor = singletonCodeBuilder.makeSingleton(factoryClass);

        JBlock constructorBody = singletonDescriptor.getConstructor().body();

        String name = analysisBridge.getName().substring(analysisBridge.getName().lastIndexOf('.') + 1);
        String packageName = analysisBridge.getName().substring(0, analysisBridge.getName().lastIndexOf('.'));

        JType returnType = codeModel.parseType(name);
        JMethod buildInstanceMethod = factoryClass.method(JMod.PUBLIC, returnType, BUILDER_METHOD);
        JBlock buildIntanceBody = buildInstanceMethod.body();


        JVar variable = buildIntanceBody.decl(codeModel.parseType(analysisBridge.getName()), "activityDelegate");

        //constructor injection
        buildIntanceBody.assign(variable,
                buildConstructorCall(analysisBridge, returnType, constructorBody, factoryClass));


        //parameter injection
        buildParameterInjection(analysisBridge, variable, buildIntanceBody, packageName);

        //method injection
        buildMethodInjection(analysisBridge);

        buildIntanceBody._return(variable);

        return new FactoryDescriptor(factoryClass, singletonDescriptor.getGetInstanceMethod().name(), returnType, buildInstanceMethod.name());
    }

    private void buildMethodInjection(TypeAnalysisBridge analysisBridge) {

    }

    private void buildParameterInjection(TypeAnalysisBridge analysisBridge, JVar variable, JBlock buildIntanceBody, String packageName) throws ClassNotFoundException {
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

    private JInvocation buildConstructorCall(TypeAnalysisBridge analysisBridge, JType returnType, JBlock constructorBody, JDefinedClass factoryClass) throws ClassNotFoundException, JClassAlreadyExistsException {
        AnalysisBridge applicableConstructor = findApplicableConstructor(analysisBridge);

        JInvocation constructorInvocation = JExpr._new(returnType);


        if (applicableConstructor != null) {
            ElementAnalysisBridge elemntConstructor = (ElementAnalysisBridge) applicableConstructor;
            System.out.println("constructor elemnt count: " + ((ExecutableElement) elemntConstructor.getElement()).getParameters().size());
            for (VariableElement variable : ((ExecutableElement) elemntConstructor.getElement()).getParameters()) {
                System.out.println("** constructor element: " + variable.asType().toString());

                FactoryDescriptor descriptor = buildFactory(new VariableElementAnalysisBridge(variable));

                JFieldVar factoryField = factoryClass.field(JMod.PRIVATE, descriptor.getClassDefinition(), variable.getSimpleName() + "Factory");

                constructorBody.assign(factoryField, descriptor.getClassDefinition().staticInvoke(descriptor.getInstanceMethodName()));

                constructorInvocation = constructorInvocation.arg(factoryField.invoke(descriptor.getBuilderMethodName()));
            }
        }

        return constructorInvocation;
    }

    private AnalysisBridge findApplicableConstructor(TypeAnalysisBridge analysisBridge) {

        AnalysisBridge applicableConstructor = null;
        AnalysisBridge noArgConstructor = null;
        boolean constructorDefined = false;

        for (AnalysisBridge childAnalysisBridge : analysisBridge.getEnclosedElements()) {
            if (childAnalysisBridge.getType() == ElementKind.CONSTRUCTOR) {
                constructorDefined = true;
                if (childAnalysisBridge.getEnclosedElements().isEmpty()) {
                    noArgConstructor = childAnalysisBridge;
                }
                if (childAnalysisBridge.isAnnotated(Inject.class)) {
                    if (applicableConstructor != null) {
                        //already found
                        throw new RoboticsGenerationException("Multiple constructors defined");//todo: complete statement
                    }
                    applicableConstructor = childAnalysisBridge;
                }
            }
        }

        if (applicableConstructor == null && constructorDefined) {
            if (noArgConstructor == null) {
                throw new RoboticsGenerationException("No no-arg constructor defined, with no @Inject constructor defined.");//todo:complete statement
            } else {
                applicableConstructor = noArgConstructor;
            }
        }

        return applicableConstructor;
    }
}
