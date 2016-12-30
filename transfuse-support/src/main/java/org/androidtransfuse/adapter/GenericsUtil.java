package org.androidtransfuse.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public final class GenericsUtil {

    private static class TypeArgument{
        ASTType type;
        ASTGenericArgument argument;

        public TypeArgument(ASTType type, ASTGenericArgument argument) {
            this.type = type;
            this.argument = argument;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof TypeArgument)) return false;

            TypeArgument that = (TypeArgument) o;

            return new EqualsBuilder()
                    .append(type, that.type)
                    .append(argument, that.argument)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(type)
                    .append(argument)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return "(" + type.getPackageClass().getClassName() + " " + argument + ")";
        }
    }

    private static final GenericsUtil INSTANCE = new GenericsUtil();

    private GenericsUtil() {}

    public static GenericsUtil getInstance() {
        return INSTANCE;
    }

    public ASTType getType(ASTType rootType, ASTType containingType, ASTType targetGenericType) {
        if (targetGenericType instanceof ASTGenericParameterType) {
            return getType(rootType, containingType, ((ASTGenericParameterType)targetGenericType).getArgument());
        }
        return targetGenericType;
    }

    public ASTType getType(ASTType rootType, ASTType containingType, ASTGenericArgument targetGenericType) {
        Map<TypeArgument, TypeArgument> implementations = new HashMap<TypeArgument, TypeArgument>();
        Map<TypeArgument, ASTType> concretes = new HashMap<TypeArgument, ASTType>();

        recursivePopulate(implementations, concretes, null, rootType);

        return recursiveFindType(implementations, concretes, new TypeArgument(containingType, targetGenericType));
    }

    private void recursivePopulate(Map<TypeArgument, TypeArgument> implementations, Map<TypeArgument, ASTType> concretes, ASTType implementationType, ASTType declaredType) {

        if(implementationType != null) {
            for (int i = 0; i < declaredType.getGenericArgumentTypes().size(); i++) {
                ASTType genericArgumentType = declaredType.getGenericArgumentTypes().get(i);
                TypeArgument genericArgument = new TypeArgument(declaredType, declaredType.getGenericArguments().get(i));
                if (genericArgumentType instanceof ASTGenericParameterType) {
                    implementations.put(genericArgument, new TypeArgument(implementationType, ((ASTGenericParameterType) genericArgumentType).getArgument()));
                } else {
                    concretes.put(genericArgument, genericArgumentType);
                }
            }
        }

        if (declaredType.getSuperClass() != null) {
            recursivePopulate(implementations, concretes, declaredType, declaredType.getSuperClass());

            Set<ASTType> genericInterfaces = declaredType.getInterfaces();
            for (ASTType genericInterface : genericInterfaces) {
                recursivePopulate(implementations, concretes, declaredType, genericInterface);
            }
        }

    }

    private ASTType recursiveFindType(Map<TypeArgument, TypeArgument> implementations, Map<TypeArgument, ASTType> concretes, TypeArgument targetArgument) {
        if (concretes.containsKey(targetArgument)) {
            return concretes.get(targetArgument);
        }

        if (implementations.containsKey(targetArgument)) {
            return recursiveFindType(implementations, concretes, implementations.get(targetArgument));
        }
        return null;
    }
}
