package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.ProviderDescriptor;
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
    private ProviderGenerator providerGenerator;
    private SingletonCodeBuilder singletonCodeBuilder;

    @Inject
    public InjectorGenerator(JCodeModel codeModel, ProviderGenerator providerGenerator, SingletonCodeBuilder singletonCodeBuilder) {
        this.codeModel = codeModel;
        this.providerGenerator = providerGenerator;
        this.singletonCodeBuilder = singletonCodeBuilder;
        this.injectorClass = null;

    }

    public ProviderDescriptor buildInjector(FieldInjectionPoint fieldInjectionPoint) throws JClassAlreadyExistsException, ClassNotFoundException {

        if (injectorClass == null) {
            injectorClass = codeModel._class(JMod.PUBLIC, fieldInjectionPoint.getName() + "Injector", ClassType.CLASS);
            //singleton constructor
            SingletonDescriptor singletonDescriptor = singletonCodeBuilder.makeSingleton(injectorClass);
            constructor = singletonDescriptor.getConstructor();
            getInstanceMethod = singletonDescriptor.getGetInstanceMethod();
        }

        return addFactoryMethod(fieldInjectionPoint);
    }

    private ProviderDescriptor addFactoryMethod(FieldInjectionPoint fieldInjectionPoint) throws ClassNotFoundException, JClassAlreadyExistsException {
        //build factory for delegate
        ProviderDescriptor providerDescriptor = providerGenerator.buildFactory(fieldInjectionPoint.getInjectionNode());

        String shortName = fieldInjectionPoint.getName().substring(fieldInjectionPoint.getName().lastIndexOf('.') + 1).toLowerCase();

        JFieldVar factoryField = injectorClass.field(JMod.PRIVATE, providerDescriptor.getClassDefinition(), shortName + "factory");

        constructor.body().assign(factoryField, providerDescriptor.getClassDefinition().staticInvoke(providerDescriptor.getInstanceMethodName()));

        //factoryField.assign(JExpr._new(factoryClass));
        JMethod builderMethod = injectorClass.method(JMod.PUBLIC, providerDescriptor.getReturnType(), "build" + shortName);
        JBlock buildDescriptorMethodBlock = builderMethod.body();

        buildDescriptorMethodBlock._return(factoryField.invoke(providerDescriptor.getBuilderMethodName()));

        return new ProviderDescriptor(injectorClass, getInstanceMethod.name(), providerDescriptor.getReturnType(), builderMethod.name());
    }
}
