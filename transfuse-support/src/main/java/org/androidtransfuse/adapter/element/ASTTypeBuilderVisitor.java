/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.adapter.element;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.androidtransfuse.TransfuseAdapterException;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.transaction.TransactionRuntimeException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor6;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Builder of an ASTType from a TypeMirror input
 *
 * @author John Ericksen
 */
public class ASTTypeBuilderVisitor extends SimpleTypeVisitor6<ASTType, Void> implements Function<TypeMirror, ASTType> {

    private final Provider<ASTElementFactory> astElementFactoryProvider;

    @Inject
    public ASTTypeBuilderVisitor(Provider<ASTElementFactory> astElementFactoryProvider) {
        this.astElementFactoryProvider = astElementFactoryProvider;
    }

    @Override
    public ASTType visitPrimitive(PrimitiveType primitiveType, Void v) {
        return ASTPrimitiveType.valueOf(primitiveType.getKind().name());
    }

    @Override
    public ASTType visitNull(NullType nullType, Void v) {
        throw new TransfuseAdapterException("Encountered NullType, unable to recover");
    }

    @Override
    public ASTType visitArray(ArrayType arrayType, Void v) {
        return new ASTArrayType(arrayType.getComponentType().accept(this, null));
    }

    @Override
    public ASTType visitDeclared(DeclaredType declaredType, Void v) {
        return astElementFactoryProvider.get().buildASTElementType(declaredType);
    }

    @Override
    public ASTType visitError(ErrorType errorType, Void v) {
        throw new TransactionRuntimeException("Encountered ErrorType " + errorType.asElement().toString() + ", unable to recover");
    }

    @Override
    public ASTType visitTypeVariable(TypeVariable typeVariable, Void v) {
        ASTType upperBound = typeVariable.getUpperBound().accept(this, null);
        return new ASTGenericParameterType(new ASTGenericArgument(typeVariable.toString()), upperBound);
    }

    @Override
    public ASTType visitWildcard(WildcardType wildcardType, Void v) {
        ASTType extendsBound = null;
        ASTType superBound = null;
        if(wildcardType.getSuperBound() != null){
            superBound = wildcardType.getSuperBound().accept(this, null);
        }
        if(wildcardType.getExtendsBound() != null){
            extendsBound = wildcardType.getExtendsBound().accept(this, null);
        }
        return new ASTWildcardType(superBound, extendsBound);
    }

    @Override
    public ASTType visitExecutable(ExecutableType executableType, Void v) {
        if (executableType instanceof TypeElement) {
            return astElementFactoryProvider.get().getType((TypeElement) executableType);
        } else {
            throw new TransfuseAdapterException("Encountered non-TypeElement");
        }
    }

    @Override
    public ASTType visitNoType(NoType noType, Void v) {
        if (noType.getKind().equals(TypeKind.VOID)) {
            return ASTVoidType.VOID;
        }
        return new ASTEmptyType("<NOTYPE>");
    }

    @Override
    public ASTType visitUnknown(TypeMirror typeMirror, Void v) {
        try {
            // optionally handle the IntersectionType (introduced in Java 8) by reflection.
            Class<?> intersectionTypeClass = Class.forName("javax.lang.model.type.IntersectionType");
            Method getBoundsMethod = intersectionTypeClass.getMethod("getBounds");
            if(intersectionTypeClass.isInstance(typeMirror)){
                return new ASTIntersectionType(
                        FluentIterable
                                .from((List<TypeMirror>)getBoundsMethod.invoke(typeMirror))
                                .transform(this)
                                .toList()
                );
            }
        } catch (ClassNotFoundException e) {
            //not found, continue
        } catch (InvocationTargetException e) {
            //not found, continue
        } catch (NoSuchMethodException e) {
            //not found, continue
        } catch (IllegalAccessException e) {
            //not found, continue
        }

        throw new TransfuseAdapterException("Encountered unknown TypeMirror (" + typeMirror + ") kind: " + typeMirror.getKind() + ", unable to recover");
    }



    @Override
    public ASTType apply(TypeMirror input) {
        return input.accept(this, null);
    }
}
