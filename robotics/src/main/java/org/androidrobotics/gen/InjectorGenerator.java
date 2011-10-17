package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.model.FactoryDescriptor;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.SingletonDescriptor;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectorGenerator {

    private JCodeModel codeModel;
    private JDefinedClass injectorClass;
    private JMethod constructor;
    private JMethod getInstanceMethod;
    private FactoryGenerator factoryGenerator;
    private SingletonCodeBuilder singletonCodeBuilder;

    @Inject
    public InjectorGenerator(JCodeModel codeModel, FactoryGenerator factoryGenerator, SingletonCodeBuilder singletonCodeBuilder) {
        this.codeModel = codeModel;
        this.factoryGenerator = factoryGenerator;
        this.singletonCodeBuilder = singletonCodeBuilder;
        this.injectorClass = null;

    }

    public FactoryDescriptor buildInjector(FieldInjectionPoint fieldInjectionPoint) throws JClassAlreadyExistsException, ClassNotFoundException {

        if (injectorClass == null) {
            injectorClass = codeModel._class(JMod.PUBLIC, fieldInjectionPoint.getName() + "Injector", ClassType.CLASS);
            //singleton constructor
            SingletonDescriptor singletonDescriptor = singletonCodeBuilder.makeSingleton(injectorClass);
            constructor = singletonDescriptor.getConstructor();
            getInstanceMethod = singletonDescriptor.getGetInstanceMethod();
        }

        return addFactoryMethod(fieldInjectionPoint);
    }

    private FactoryDescriptor addFactoryMethod(FieldInjectionPoint fieldInjectionPoint) throws ClassNotFoundException, JClassAlreadyExistsException {
        //build factory for delegate
        FactoryDescriptor factoryDescriptor = factoryGenerator.buildFactory(fieldInjectionPoint.getInjectionNode());

        String shortName = fieldInjectionPoint.getName().substring(fieldInjectionPoint.getName().lastIndexOf('.') + 1).toLowerCase();

        JFieldVar factoryField = injectorClass.field(JMod.PRIVATE, factoryDescriptor.getClassDefinition(), shortName + "factory");

        constructor.body().assign(factoryField, factoryDescriptor.getClassDefinition().staticInvoke(factoryDescriptor.getInstanceMethodName()));

        //factoryField.assign(JExpr._new(factoryClass));
        JMethod builderMethod = injectorClass.method(JMod.PUBLIC, factoryDescriptor.getReturnType(), "build" + shortName);
        JBlock buildDescriptorMethodBlock = builderMethod.body();

        buildDescriptorMethodBlock._return(factoryField.invoke(factoryDescriptor.getBuilderMethodName()));

        return new FactoryDescriptor(injectorClass, getInstanceMethod.name(), factoryDescriptor.getReturnType(), builderMethod.name());
    }
}
