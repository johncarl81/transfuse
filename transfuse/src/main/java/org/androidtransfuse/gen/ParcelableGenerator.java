package org.androidtransfuse.gen;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTArrayType;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTPrimitiveType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.util.ParcelableWrapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class ParcelableGenerator {

    private static final String CREATOR_CLASS_NAME = "CREATOR";
    private static final String CREATE_FROM_PARCEL = "createFromParcel";
    private static final String NEW_ARRAY = "newArray";
    private static final String WRITE_TO_PARCEL = "writeToParcel";
    private static final String DESCRIBE_CONTENTS = "describeContents";
    private static final String GET_WRAPPED = "getWrapped";

    private JCodeModel codeModel;
    private UniqueVariableNamer namer;
    private ASTClassFactory astClassFactory;
    private Map<ASTType, JDefinedClass> parceableMap = new HashMap<ASTType, JDefinedClass>();
    private Map<ASTType, ReadWritePair> parceableModifier = new HashMap<ASTType, ReadWritePair>();

    @Inject
    public ParcelableGenerator(JCodeModel codeModel, UniqueVariableNamer namer, ASTClassFactory astClassFactory) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.astClassFactory = astClassFactory;

        addPrimitivePair(ASTPrimitiveType.BYTE, "readByte", "writeByte");
        addPrimitivePair(ASTPrimitiveType.DOUBLE, "readDouble", "writeDouble");
        addPrimitivePair(ASTPrimitiveType.FLOAT, "readFloat", "writeFloat");
        addPrimitivePair(ASTPrimitiveType.INT, "readInt", "writeInt");
        addPrimitivePair(ASTPrimitiveType.LONG, "readLong", "writeLong");
        /*addPrimitiveArrayPair(ASTPrimitiveType.BYTE, "readByteArray", "writeByteArray");
        addPrimitiveArrayPair(ASTPrimitiveType.CHAR, "readCharArray", "writeCharArray");
        addPrimitiveArrayPair(ASTPrimitiveType.BOOLEAN, "readIntArray", "writeBooleanArray");
        addPrimitiveArrayPair(ASTPrimitiveType.INT, "readByteArray", "writeIntArray");
        addPrimitiveArrayPair(ASTPrimitiveType.LONG, "readLongArray", "writeLongArray");
        addPrimitiveArrayPair(ASTPrimitiveType.FLOAT, "readFloatArray", "writeFloatArray");
        addPrimitiveArrayPair(ASTPrimitiveType.DOUBLE, "readDoubleArray", "writeDoubleArray");*/
        //addPair(String[].class, "readStringArray", "writeStringArray");
        addPair(String.class, "readString", "writeString");
        addPair(IBinder.class, "readStrongBinder", "writeStrongBinder");
        addPair(Bundle.class, "readBundle", "writeBundle");
        //addPair(Object[].class, "readArray", "writeArray");
        //addPair(SparseArray.class, "readSparseArray", "writeSparseArray");
        addPair(SparseBooleanArray.class, "readSparseBooleanArray", "writeSparseBooleanArray");
        //addPair(Parcelable.class, "readParcelable", "writeParcelable");
        addPair(Serializable.class, "readSerializable", "writeSerializable");
        addPair(Exception.class, "readException", "writeException");
    }

    private void addPair(Class clazz, String readMethod, String writeMethod) {
        addPair(astClassFactory.buildASTClassType(clazz), readMethod, writeMethod);
    }

    private void addPrimitiveArrayPair(ASTPrimitiveType primitiveType, String readMethod, String writeMethod) {
        addPair(new ASTArrayType(primitiveType), readMethod, writeMethod);
        addPair(new ASTArrayType(astClassFactory.buildASTClassType(primitiveType.getObjectClass())), readMethod, writeMethod);
    }

    private void addPrimitivePair(ASTPrimitiveType primitiveType, String readMethod, String writeMethod) {
        addPair(primitiveType, readMethod, writeMethod);
        addPair(astClassFactory.buildASTClassType(primitiveType.getObjectClass()), readMethod, writeMethod);
    }

    private void addPair(ASTType astType, String readMethod, String writeMethod) {
        parceableModifier.put(astType, new ReadWritePair(readMethod, writeMethod));
    }

    public void generateParcelable(ASTType type, List<GetterSetterMethodPair> propertyMutators) {
        if (!parceableMap.containsKey(type)) {
            JDefinedClass definedClass = generateParcelableDefinedClass(type, propertyMutators);
            if (definedClass != null) {
                parceableMap.put(type, definedClass);
            }
        }
    }

    private JDefinedClass generateParcelableDefinedClass(ASTType type, List<GetterSetterMethodPair> propertyMutators) {
        try {
            JType inputType = codeModel.ref(type.getName());

            JDefinedClass parcelableClass = codeModel._class(JMod.PUBLIC, type.getName() + "_Parcelable", ClassType.CLASS);
            parcelableClass._implements(Parcelable.class)
                    ._implements(codeModel.ref(ParcelableWrapper.class).narrow(inputType));

            //wrapped @Parcel
            JFieldVar wrapped = parcelableClass.field(JMod.PRIVATE, inputType, namer.generateName(type.getName()));

            //Parcel constructor
            JMethod parcelConstructor = parcelableClass.constructor(JMod.PUBLIC);
            JVar parcelParam = parcelConstructor.param(codeModel.ref(Parcel.class), namer.generateName(Parcel.class));
            JBlock parcelConstructorBody = parcelConstructor.body();
            parcelConstructorBody.assign(wrapped, JExpr._new(inputType));

            //read from parcel
            for (GetterSetterMethodPair propertyMutator : propertyMutators) {
                parcelConstructorBody.invoke(wrapped, propertyMutator.getSetter().getName()).arg(
                        parcelParam.invoke(getGetter(propertyMutator.getGetter().getReturnType())));
            }

            //@Parcel input
            JMethod inputConstructor = parcelableClass.constructor(JMod.PUBLIC);
            JVar inputParam = inputConstructor.param(inputType, namer.generateName(type.getName()));
            inputConstructor.body().assign(wrapped, inputParam);

            //writeToParcel(android.os.Parcel,int)
            JMethod writeToParcelMethod = parcelableClass.method(JMod.PUBLIC, codeModel.VOID, WRITE_TO_PARCEL);
            JVar wtParcelParam = writeToParcelMethod.param(Parcel.class, namer.generateName(Parcel.class));
            writeToParcelMethod.param(codeModel.INT, "flags");

            for (GetterSetterMethodPair propertyMutator : propertyMutators) {

                writeToParcelMethod.body().invoke(wtParcelParam, getSetter(propertyMutator.getGetter().getReturnType())).arg(wrapped.invoke(propertyMutator.getGetter().getName()));
            }


            //describeContents()
            JMethod describeContentsMethod = parcelableClass.method(JMod.PUBLIC, codeModel.INT, DESCRIBE_CONTENTS);
            describeContentsMethod.body()._return(JExpr.lit(0));

            //ParcelableWrapper.getWrapped()
            JMethod getWrappedMethod = parcelableClass.method(JMod.PUBLIC, inputType, GET_WRAPPED);
            getWrappedMethod.body()._return(wrapped);

            /*
            @SuppressWarnings("UnusedDeclaration")
            public static final Creator<ParcelExample_Parcel> CREATOR = new Creator<ParcelExample_Parcel>() {
                @Override
                public ParcelExample_Parcel createFromParcel(Parcel source) {
                    return new ParcelExample_Parcel(source);
                }

                @Override
                public ParcelExample_Parcel[] newArray(int size) {
                    return new ParcelExample_Parcel[0];
                }
            };
            */
            JDefinedClass creatorClass = codeModel.anonymousClass(codeModel.ref(Parcelable.Creator.class).narrow(parcelableClass));

            //createFromParcel method
            JMethod createFromParcelMethod = creatorClass.method(JMod.PUBLIC, parcelableClass, CREATE_FROM_PARCEL);
            JVar cfpParcelParam = createFromParcelMethod.param(Parcel.class, namer.generateName(Parcel.class));
            createFromParcelMethod.annotate(Override.class);

            createFromParcelMethod.body()._return(JExpr._new(parcelableClass).arg(cfpParcelParam));

            //newArray method
            JMethod newArrayMethod = creatorClass.method(JMod.PUBLIC, parcelableClass.array(), NEW_ARRAY);
            JVar sizeParam = newArrayMethod.param(codeModel.INT, "size");
            newArrayMethod.annotate(Override.class);

            newArrayMethod.body()._return(JExpr.newArray(parcelableClass, sizeParam));

            //public static final Creator<type> CREATOR
            JFieldVar creatorField = parcelableClass.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, creatorClass, CREATOR_CLASS_NAME, JExpr._new(creatorClass));
            creatorField.annotate(SuppressWarnings.class).param("value", "UnusedDeclaration");

            return parcelableClass;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class Already Exists: " + type.getName() + "_Parelable", e);
        }
    }

    private String getSetter(ASTType returnType) {
        if (parceableModifier.containsKey(returnType)) {
            return parceableModifier.get(returnType).getWriteMethod();
        }
        throw new TransfuseAnalysisException("Unable to find appropriate Parcel method to write " + returnType.getName());
    }

    private String getGetter(ASTType returnType) {
        if (parceableModifier.containsKey(returnType)) {
            return parceableModifier.get(returnType).getReadMethod();
        }
        throw new TransfuseAnalysisException("Unable to find appropriate Parcel method to read " + returnType.getName());
    }


    public String getParcelable(ASTType type) {
        if (parceableMap.containsKey(type)) {
            return parceableMap.get(type).fullName();
        }
        //todo: throw exception?
        return null;
    }

    public class ReadWritePair {
        String readMethod;
        String writeMethod;

        public ReadWritePair(String readMethod, String writeMethod) {
            this.readMethod = readMethod;
            this.writeMethod = writeMethod;
        }

        public String getReadMethod() {
            return readMethod;
        }

        public String getWriteMethod() {
            return writeMethod;
        }
    }
}
