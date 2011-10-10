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
            injectorClass = codeModel._class(JMod.PUBLIC, descriptor.getPackage() + "." + descriptor.getName() + "Injector", ClassType.CLASS);
            //singleton constructor
            injectorClass.constructor(JMod.PRIVATE);
            JFieldVar instance = injectorClass.field(JMod.PRIVATE | JMod.STATIC, injectorClass, "INSTANCE");
            JMethod getInstance = injectorClass.method(JMod.PUBLIC | JMod.STATIC, injectorClass, "getInstance");
            JBlock getInstanceBody = getInstance.body();

            getInstanceBody._if(instance.eq(JExpr._null()))
                    ._then().block().assign(instance, JExpr._new(injectorClass));

            getInstanceBody._return(instance);
        }

        JDefinedClass factoryClass = buildFactory(descriptor);

        JFieldVar factoryField = injectorClass.field(JMod.PRIVATE, factoryClass, descriptor.getShorDelegateClassName() + "_factory");
        factoryField.assign(JExpr._new(factoryClass));

        JBlock buildDescriptorMethodBlock = injectorClass.method(JMod.PUBLIC, codeModel.parseType(descriptor.getDelegateClass()), "build_" + descriptor.getShorDelegateClassName()).body();

        buildDescriptorMethodBlock._return(factoryField.invoke("buildInstance"));

        return injectorClass;

    }

    private JDefinedClass buildFactory(ActivityDescriptor descriptor) throws JClassAlreadyExistsException, ClassNotFoundException {

        String name = descriptor.getDelegateClass();

        JDefinedClass factoryClass = codeModel._class(JMod.PUBLIC, descriptor.getPackage() + "." + name + "Factory", ClassType.CLASS);

        JBlock buildIntanceBody = factoryClass.method(JMod.PUBLIC | JMod.STATIC, codeModel.parseType(name), "buildInstance").body();


        JVar variable = buildIntanceBody.decl(codeModel.parseType(descriptor.getPackage() + ".TestActivityDelegate"), "activityDelegate");
        buildIntanceBody.assign(variable, JExpr._new(codeModel.parseType(name)));

        for (InjectionPoint injectionPoint : descriptor.getInjectionPoints()) {
            JTryBlock jTryBlock = buildIntanceBody._try();

            JBlock tryBuildInstancebody = jTryBlock.body();

            JVar fieldVariable = tryBuildInstancebody.decl(codeModel.parseType("java.lang.reflect.Field"), "field");
            tryBuildInstancebody.assign(fieldVariable, codeModel.ref(descriptor.getPackage() + ".TestActivityDelegate").dotclass()
                    .invoke("getDeclaredField").arg("controller"));


            tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.TRUE));
            tryBuildInstancebody.add(fieldVariable.invoke("set").arg(variable).arg(JExpr._new(codeModel.parseType((descriptor.getPackage() + ".TestController")))));
            tryBuildInstancebody.add(fieldVariable.invoke("setAccessible").arg(JExpr.FALSE));

            jTryBlock._catch(codeModel.directClass("java.lang.IllegalAccessException"));
            jTryBlock._catch(codeModel.directClass("java.lang.NoSuchFieldException"));
        }


        buildIntanceBody._return(variable);

        return factoryClass;
    }
}
