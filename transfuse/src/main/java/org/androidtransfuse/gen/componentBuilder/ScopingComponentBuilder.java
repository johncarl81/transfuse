package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.gen.AndroidComponentDescriptor;
import org.androidtransfuse.gen.ComponentBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.SingletonScope;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ScopingComponentBuilder implements ComponentBuilder {

    private JCodeModel codeModel;

    @Inject
    public ScopingComponentBuilder(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    @Override
    public void build(JDefinedClass definedClass, AndroidComponentDescriptor descriptor, RResource rResource) {
        definedClass._implements(Scope.class);

        JClass singletonScopeClassRef = codeModel.ref(SingletonScope.class);
        JFieldVar singletonScopeField = definedClass.field(JMod.PRIVATE, singletonScopeClassRef, "singletonScope");
        singletonScopeField.init(JExpr._new(singletonScopeClassRef));

        //<T> T getScopedObject(Class<T> clazz, Provider<T> provider);
        JClass clazzRef = codeModel.ref(Class.class);
        JClass providerRef = codeModel.ref(Provider.class);

        JMethod getScopedObjectMethod = definedClass.method(JMod.PUBLIC, Object.class, "getScopedObject");
        JTypeVar t = getScopedObjectMethod.generify("T");
        JVar clazzParam = getScopedObjectMethod.param(clazzRef.narrow(t), "clazz");
        JVar providerParam = getScopedObjectMethod.param(providerRef.narrow(t), "provider");

        getScopedObjectMethod.type(t);

        JBlock scopedObjectBody = getScopedObjectMethod.body();

        scopedObjectBody._return(singletonScopeField.invoke("getScopedObject").arg(clazzParam).arg(providerParam));
    }

}
