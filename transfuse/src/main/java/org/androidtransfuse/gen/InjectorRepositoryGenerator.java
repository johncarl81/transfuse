package org.androidtransfuse.gen;

import com.sun.codemodel.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class InjectorRepositoryGenerator {

    private static final String REPOSITORY_NAME = "org.androidtransfuse.InjectorRepository";
    private static final String GET_METHOD = "get";
    private static final String MAP_NAME = "injectors";

    private final JCodeModel codeModel;

    private JBlock injectorRegistrationBlock = null;
    private JVar registrationMap = null;

    @Inject
    public InjectorRepositoryGenerator(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public void generateInjectorRepository(JClass interfaceClass, JDefinedClass implClass) throws JClassAlreadyExistsException {
        if(injectorRegistrationBlock == null){
            JDefinedClass injectorRepsoitoryClass = codeModel._class(JMod.PUBLIC, REPOSITORY_NAME, ClassType.CLASS);

            //map definition
            JClass mapType = codeModel.ref(Map.class).narrow(Class.class, Object.class);
            JClass hashMapType = codeModel.ref(HashMap.class).narrow(Class.class, Object.class);
            registrationMap = injectorRepsoitoryClass.field(JMod.PRIVATE | JMod.STATIC, mapType , MAP_NAME);

            //getter
            JMethod getMethod = injectorRepsoitoryClass.method(JMod.PUBLIC | JMod.STATIC, Object.class, GET_METHOD);
            JTypeVar t = getMethod.generify("T");
            getMethod.type(t);
            JVar typeParam = getMethod.param(codeModel.ref(Class.class).narrow(t), "type");

            getMethod.body()._return(JExpr.cast(t, registrationMap.invoke("get").arg(typeParam)));

            //static registration block
            injectorRegistrationBlock = injectorRepsoitoryClass.init();
            injectorRegistrationBlock.assign(registrationMap, JExpr._new(hashMapType));
        }

        //register injector implementations
        injectorRegistrationBlock.add(registrationMap.invoke("put").arg(interfaceClass.dotclass()).arg(JExpr._new(implClass)));
    }
}
