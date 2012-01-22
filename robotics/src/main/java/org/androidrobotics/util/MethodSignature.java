package org.androidrobotics.util;

import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTParameter;
import org.androidrobotics.analysis.adapter.ASTPrimitiveType;
import org.androidrobotics.analysis.adapter.ASTType;

import java.util.List;

/**
 * @author John Ericksen
 */
public class MethodSignature {

    private String methodSignature;
    private ASTMethod method;

    public MethodSignature(ASTMethod method) {
        methodSignature = makeDescriptor(method);
        this.method = method;
    }

    public ASTMethod getMethod() {
        return method;
    }

    /**
     * Makes a descriptor for a given method.
     */
    public static String makeDescriptor(ASTMethod method) {
        List<ASTParameter> params = method.getParameters();
        return method.getName() + ':' + makeDescriptor(params, method.getReturnType());
    }

    /**
     * Makes a descriptor for a given method.
     *
     * @param params  parameter types.
     * @param retType return type.
     */
    public static String makeDescriptor(List<ASTParameter> params, ASTType retType) {
        StringBuffer buff = new StringBuffer();
        buff.append('(');
        for (ASTParameter param : params) {
            makeDesc(buff, param.getASTType());
        }

        buff.append(')');
        makeDesc(buff, retType);
        return buff.toString();
    }

    private static void makeDesc(StringBuffer buff, ASTType type) {
        if (type.isArray()) {
            buff.append('[');
        }
        makeTypeDesc(buff, type);
    }

    private static void makeTypeDesc(StringBuffer buff, ASTType type) {
        if (type instanceof ASTPrimitiveType) {
            buff.append(type.getName());
        } else {
            buff.append('L').append(type.getName().replace('.', '/')).append(';');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodSignature)) {
            return false;
        }

        MethodSignature that = (MethodSignature) o;

        if (methodSignature != null ? !methodSignature.equals(that.methodSignature) : that.methodSignature != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return methodSignature != null ? methodSignature.hashCode() : 0;
    }
}
