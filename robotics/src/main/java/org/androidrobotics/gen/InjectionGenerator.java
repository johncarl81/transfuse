package org.androidrobotics.gen;

import com.sun.codemodel.*;

/**
 * @author John Ericksen
 */
public class InjectionGenerator {

    private JCodeModel codeModel;
    private JDefinedClass injectorClass;

    public InjectionGenerator(JCodeModel codeModel) {
        this.codeModel = codeModel;
        this.injectorClass = null;

    }

    public JDefinedClass buildInjector(ActivityDescriptor descriptor) throws JClassAlreadyExistsException, ClassNotFoundException {

        if (injectorClass == null) {
            injectorClass = codeModel._class(JMod.PUBLIC, "org.androidrobotics.example.simple.Injector", ClassType.CLASS);
            //singleton constructor
            injectorClass.constructor(JMod.PRIVATE);
            JFieldVar instance = injectorClass.field(JMod.PRIVATE | JMod.STATIC, injectorClass, "INSTANCE");
            JMethod getInstance = injectorClass.method(JMod.PUBLIC | JMod.STATIC, injectorClass, "getInstance");
            JBlock getInstanceBody = getInstance.body();

            getInstanceBody._if(instance.eq(JExpr._null()))
                    ._then().block().assign(instance, JExpr._new(injectorClass));

            getInstanceBody._return(instance);
        }

        JDefinedClass factoryClass = buildFactory(descriptor.getDelegateClass());

        JFieldVar factoryField = injectorClass.field(JMod.PRIVATE, factoryClass, descriptor.getShorDelegateClassName() + "_factory");
        factoryField.assign(JExpr._new(factoryClass));

        JBlock buildDescriptorMethodBlock = injectorClass.method(JMod.PUBLIC, codeModel.parseType(descriptor.getDelegateClass()), "build_" + descriptor.getShorDelegateClassName()).body();

        buildDescriptorMethodBlock._return(factoryField.invoke("buildInstance"));

        return injectorClass;

    }

    private JDefinedClass buildFactory(String name) throws JClassAlreadyExistsException, ClassNotFoundException {

        JDefinedClass factoryClass = codeModel._class(JMod.PUBLIC, "org.androidrobotics.example.simple." + name + "Factory", ClassType.CLASS);

        JBlock buildIntanceBody = factoryClass.method(JMod.PUBLIC | JMod.STATIC, codeModel.parseType(name), "buildInstance").body();

        buildIntanceBody._return(JExpr._new(codeModel.parseType(name)));

        return factoryClass;
    }
}
