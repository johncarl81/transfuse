package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.TypeAnalysisBridge;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.FactoryDescriptor;
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

    public FactoryDescriptor buildInjector(ActivityDescriptor descriptor) throws JClassAlreadyExistsException, ClassNotFoundException {

        if (injectorClass == null) {
            injectorClass = codeModel._class(JMod.PUBLIC, descriptor.getPackage() + "." + descriptor.getShortDelegateClassName() + "Injector", ClassType.CLASS);
            //singleton constructor
            SingletonDescriptor singletonDescriptor = singletonCodeBuilder.makeSingleton(injectorClass);
            constructor = singletonDescriptor.getConstructor();
            getInstanceMethod = singletonDescriptor.getGetInstanceMethod();
        }

        return addFactoryMethod(descriptor.getDelegateAnalysis());
    }

    private FactoryDescriptor addFactoryMethod(TypeAnalysisBridge delegateAnalysis) throws ClassNotFoundException, JClassAlreadyExistsException {
        //build factory for delegate
        FactoryDescriptor factoryDescriptor = factoryGenerator.buildFactory(delegateAnalysis);

        String shortName = delegateAnalysis.getName().substring(delegateAnalysis.getName().lastIndexOf('.') + 1).toLowerCase();

        JFieldVar factoryField = injectorClass.field(JMod.PRIVATE, factoryDescriptor.getClassDefinition(), shortName + "factory");

        constructor.body().assign(factoryField, factoryDescriptor.getClassDefinition().staticInvoke(factoryDescriptor.getInstanceMethodName()));

        //factoryField.assign(JExpr._new(factoryClass));
        JMethod builderMethod = injectorClass.method(JMod.PUBLIC, factoryDescriptor.getReturnType(), "build" + shortName);
        JBlock buildDescriptorMethodBlock = builderMethod.body();

        buildDescriptorMethodBlock._return(factoryField.invoke(factoryDescriptor.getBuilderMethodName()));

        return new FactoryDescriptor(injectorClass, getInstanceMethod.name(), factoryDescriptor.getReturnType(), builderMethod.name());
    }
}
