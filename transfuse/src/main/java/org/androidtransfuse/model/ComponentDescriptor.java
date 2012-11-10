package org.androidtransfuse.model;

import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;
import org.androidtransfuse.gen.componentBuilder.InjectionNodeFactory;
import org.androidtransfuse.gen.componentBuilder.MethodBuilder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ComponentDescriptor {

    private final PackageClass packageClass;
    private final String type;
    private MethodBuilder initMethodBuilder;
    private List<ExpressionVariableDependentGenerator> generators = new ArrayList<ExpressionVariableDependentGenerator>();
    private InjectionNodeFactory injectionNodeFactory;
    private Class<? extends Annotation> initMethodEventAnnotation;

    public ComponentDescriptor(String type, PackageClass packageClass) {
        this.type = type;
        this.packageClass = packageClass;
    }

    public PackageClass getPackageClass() {
        return packageClass;
    }

    public String getType() {
        return type;
    }

    public MethodBuilder getInitMethodBuilder() {
        return initMethodBuilder;
    }

    public List<ExpressionVariableDependentGenerator> getGenerators() {
        return generators;
    }

    public void addGenerators(Collection<ExpressionVariableDependentGenerator> generators) {
        this.generators.addAll(generators);
    }

    public void addGenerators(ExpressionVariableDependentGenerator... generators) {
        if (generators != null) {
            addGenerators(Arrays.asList(generators));
        }
    }

    public void setInitMethodBuilder(Class<? extends Annotation> initEventAnnotation, MethodBuilder initMethodBuilder) {
        this.initMethodBuilder = initMethodBuilder;
        this.initMethodEventAnnotation = initEventAnnotation;
    }

    public InjectionNodeFactory getInjectionNodeFactory() {
        return injectionNodeFactory;
    }

    public void setInjectionNodeFactory(InjectionNodeFactory injectionNodeFactory) {
        this.injectionNodeFactory = injectionNodeFactory;
    }

    public Class<? extends Annotation> getInitMethodEventAnnotation() {
        return initMethodEventAnnotation;
    }
}
