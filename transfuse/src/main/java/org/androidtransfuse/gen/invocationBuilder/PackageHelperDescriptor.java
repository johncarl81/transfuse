package org.androidtransfuse.gen.invocationBuilder;

import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.PackageClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class PackageHelperDescriptor {

    private final PackageClass name;
    private final Map<ConstructorInjectionPoint, String> constructorMapping = new HashMap<ConstructorInjectionPoint, String>();
    private final Map<FieldInjectionPoint, String> fieldSetMapping = new HashMap<FieldInjectionPoint, String>();
    private final Map<FieldGetter, String> fieldGetMapping = new HashMap<FieldGetter, String>();
    private final Map<MethodCall, String> methodCallMapping = new HashMap<MethodCall, String>();

    public PackageHelperDescriptor(PackageClass name) {
        this.name = name;
    }

    public PackageClass getName() {
        return name;
    }

    public Map<ConstructorInjectionPoint, String> getConstructorMapping() {
        return constructorMapping;
    }

    public Map<FieldInjectionPoint, String> getFieldSetMapping() {
        return fieldSetMapping;
    }

    public Map<FieldGetter, String> getFieldGetMapping() {
        return fieldGetMapping;
    }

    public Map<MethodCall, String> getMethodCallMapping() {
        return methodCallMapping;
    }
}
