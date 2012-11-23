package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.model.PackageClass;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;


/**
 * @author John Ericksen
 */
//todo: move away from singleton
@Singleton
public class InjectorsGenerator {

    private static final PackageClass REPOSITORY_NAME = new PackageClass("org.androidtransfuse", "Injectors");
    private static final String GET_METHOD = "get";
    private static final String MAP_NAME = "injectors";

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;

    private JBlock injectorRegistrationBlock = null;
    private JVar registrationMap = null;

    @Inject
    public InjectorsGenerator(JCodeModel codeModel, ClassGenerationUtil generationUtil) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
    }

    public void generateInjectors(JClass interfaceClass, JDefinedClass implClass) throws JClassAlreadyExistsException {
        if (injectorRegistrationBlock == null) {
            JDefinedClass injectorRepositoryClass = generationUtil.defineClass(REPOSITORY_NAME);

            //map definition
            JClass mapType = codeModel.ref(Map.class).narrow(Class.class, Object.class);
            JClass hashMapType = codeModel.ref(HashMap.class).narrow(Class.class, Object.class);
            registrationMap = injectorRepositoryClass.field(JMod.PRIVATE | JMod.STATIC, mapType, MAP_NAME);

            //getter
            JMethod getMethod = injectorRepositoryClass.method(JMod.PUBLIC | JMod.STATIC, Object.class, GET_METHOD);
            JTypeVar t = getMethod.generify("T");
            getMethod.type(t);
            JVar typeParam = getMethod.param(codeModel.ref(Class.class).narrow(t), "type");

            getMethod.body()._return(JExpr.cast(t, registrationMap.invoke("get").arg(typeParam)));

            //static registration block
            injectorRegistrationBlock = injectorRepositoryClass.init();
            injectorRegistrationBlock.assign(registrationMap, JExpr._new(hashMapType));
        }

        //register injector implementations
        injectorRegistrationBlock.add(registrationMap.invoke("put").arg(interfaceClass.dotclass()).arg(JExpr._new(implClass)));
    }
}
