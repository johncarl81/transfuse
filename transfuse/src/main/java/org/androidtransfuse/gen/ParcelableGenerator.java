package org.androidtransfuse.gen;

import android.os.Parcel;
import android.os.Parcelable;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.util.ParcelableWrapper;

import javax.inject.Inject;
import javax.inject.Singleton;
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

    @Inject
    public ParcelableGenerator(JCodeModel codeModel, UniqueVariableNamer namer, ASTClassFactory astClassFactory) {
        this.codeModel = codeModel;
        this.namer = namer;
        this.astClassFactory = astClassFactory;
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
                        parcelParam.invoke(getMethodForType(propertyMutator.getGetter().getReturnType())));
            }

            //@Parcel input
            JMethod inputConstructor = parcelableClass.constructor(JMod.PUBLIC);
            JVar inputParam = inputConstructor.param(inputType, namer.generateName(type.getName()));
            inputConstructor.body().assign(wrapped, inputParam);

            //writeToParcel(android.os.Parcel,int)
            JMethod writeToParcelMethod = parcelableClass.method(JMod.PUBLIC, codeModel.VOID, WRITE_TO_PARCEL);
            JVar wtParcelParam = writeToParcelMethod.param(Parcel.class, namer.generateName(Parcel.class));
            JVar flagsParam = writeToParcelMethod.param(codeModel.INT, "flags");

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
        if (returnType.equals(astClassFactory.buildASTClassType(String.class))) {
            return "writeString";
        }
        if (returnType.equals(astClassFactory.buildASTClassType(Double.class))) {
            return "writeDouble";
        }
        return null;
    }

    private String getMethodForType(ASTType returnType) {
        if (returnType.equals(astClassFactory.buildASTClassType(String.class))) {
            return "readString";
        }
        if (returnType.equals(astClassFactory.buildASTClassType(Double.class))) {
            return "readDouble";
        }
        return null;
    }


    public String getParcelable(ASTType type) {
        if (parceableMap.containsKey(type)) {
            return parceableMap.get(type).fullName();
        }
        //todo: throw exception?
        return null;
    }
}
