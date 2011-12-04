package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InjectionNode {

    private ASTType astType;
    private boolean proxyRequired;
    private Set<ASTType> proxyInterfaces = new HashSet<ASTType>();

    private Set<ConstructorInjectionPoint> constructorInjectionPoints = new HashSet<ConstructorInjectionPoint>();
    private Set<MethodInjectionPoint> methodInjectionPoints = new HashSet<MethodInjectionPoint>();
    private Set<FieldInjectionPoint> fieldInjectionPoints = new HashSet<FieldInjectionPoint>();
    private Map<String, InjectionNode> builderResources = new HashMap<String, InjectionNode>();

    public InjectionNode(ASTType astType) {
        this.astType = astType;
    }

    public ConstructorInjectionPoint getConstructorInjectionPoint() {
        return constructorInjectionPoints.iterator().next();
    }

    public Set<ConstructorInjectionPoint> getConstructorInjectionPoints() {
        return constructorInjectionPoints;
    }

    public Set<MethodInjectionPoint> getMethodInjectionPoints() {
        return methodInjectionPoints;
    }

    public Set<FieldInjectionPoint> getFieldInjectionPoints() {
        return fieldInjectionPoints;
    }

    public void addInjectionPoint(ConstructorInjectionPoint constructorInjectionPoint) {
        constructorInjectionPoints.add(constructorInjectionPoint);
    }

    public void addInjectionPoint(MethodInjectionPoint methodInjectionPoint) {
        methodInjectionPoints.add(methodInjectionPoint);
    }

    public void addInjectionPoint(FieldInjectionPoint fieldInjectionPoint) {
        fieldInjectionPoints.add(fieldInjectionPoint);
    }

    public String getClassName() {
        return astType.getName();
    }

    public ASTType getAstType() {
        return astType;
    }

    public void setProxyRequired(boolean proxyRequired) {
        this.proxyRequired = proxyRequired;
    }

    public boolean isProxyRequired() {
        return proxyRequired;
    }

    public void putBuilderResource(String name, InjectionNode injectionNode) {
        builderResources.put(name, injectionNode);
    }

    public InjectionNode getBuilderResource(String name) {
        return builderResources.get(name);
    }

    public void addProxyInterface(ASTType astType) {
        proxyInterfaces.add(astType);
    }

    public Set<ASTType> getProxyInterfaces() {
        return proxyInterfaces;
    }
}
