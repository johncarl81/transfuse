package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.model.SingletonDescriptor;

/**
 * @author John Ericksen
 */
public class SingletonCodeBuilder {
    public SingletonDescriptor makeSingleton(JDefinedClass factoryClass) {
        JMethod constructor = factoryClass.constructor(JMod.PRIVATE);
        JFieldVar instance = factoryClass.field(JMod.PRIVATE | JMod.STATIC, factoryClass, "INSTANCE");
        JMethod getInstanceMethod = factoryClass.method(JMod.PUBLIC | JMod.STATIC, factoryClass, "getInstance");
        JBlock getInstanceBody = getInstanceMethod.body();

        getInstanceBody._if(instance.eq(JExpr._null()))
                ._then().block().assign(instance, JExpr._new(factoryClass));

        getInstanceBody._return(instance);

        return new SingletonDescriptor(constructor, getInstanceMethod);
    }
}
