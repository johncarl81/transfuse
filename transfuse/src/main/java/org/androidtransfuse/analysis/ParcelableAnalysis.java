package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.*;
import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.annotations.ParcelConverter;
import org.androidtransfuse.annotations.Transient;
import org.androidtransfuse.model.GetterSetterMethodPair;
import org.androidtransfuse.model.ParcelableDescriptor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ParcelableAnalysis implements Analysis<ParcelableDescriptor> {

    private static final String GET = "get";
    private static final String IS = "is";
    private static final String SET = "set";
    private static final String[] PREPENDS = {GET, IS, SET};
    private final Map<ASTType, ParcelableDescriptor> parcelableCache = new HashMap<ASTType, ParcelableDescriptor>();
    private final ASTClassFactory astClassFactory;

    @Inject
    public ParcelableAnalysis(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    public ParcelableDescriptor analyze(ASTType astType) {
        if (!parcelableCache.containsKey(astType)) {
            ParcelableDescriptor parcelableDescriptor = innerAnalyze(astType);
            parcelableCache.put(astType, parcelableDescriptor);

            //this needs to occur after adding to the cache to avoid infinite loops
            analyzeDepedencies(parcelableDescriptor.getGetterSetterPairs());
        }
        return parcelableCache.get(astType);
    }

    private void analyzeDepedencies(List<GetterSetterMethodPair> propertyPairs) {
        for (GetterSetterMethodPair propertyPair : propertyPairs) {
            ASTType type = propertyPair.getGetter().getReturnType();

            if (type.isAnnotated(Parcel.class)) {
                analyze(type);
            }
        }
    }

    private ParcelableDescriptor innerAnalyze(ASTType astType) {

        ParcelableDescriptor parcelableDescriptor;

        if (converterDefined(astType)) {
            parcelableDescriptor = new ParcelableDescriptor(getConverterType(astType));
        } else {
            Map<String, ASTMethod> methodNameMap = new HashMap<String, ASTMethod>();
            parcelableDescriptor = new ParcelableDescriptor();

            for (ASTMethod astMethod : astType.getMethods()) {
                methodNameMap.put(astMethod.getName(), astMethod);
            }

            //find all applicable getters
            for (ASTMethod astMethod : astType.getMethods()) {
                if (isGetter(astMethod) && !astMethod.isAnnotated(Transient.class)) {
                    String setterName = SET + astMethod.getName().substring(GET.length());
                    ASTMethod setterMethod = methodNameMap.get(setterName);

                    if (setterMethod != null && !setterMethod.isAnnotated(Transient.class)) {
                        if (setterMethod.getParameters().size() != 1 || !setterMethod.getParameters().get(0).getASTType().equals(astMethod.getReturnType())) {
                            throw new TransfuseAnalysisException("Setter " + setterName + " has incorrect parameters.");
                        }

                        parcelableDescriptor.getGetterSetterPairs().add(new GetterSetterMethodPair(getPropertyName(astMethod), astMethod, setterMethod));
                    }
                }
            }
        }

        return parcelableDescriptor;
    }

    private boolean converterDefined(ASTType astType) {

        ASTType converterType = getConverterType(astType);
        ASTType emptyConverterType = astClassFactory.getType(ParcelConverter.EmptyConverter.class);
        return converterType != null && !converterType.equals(emptyConverterType);
    }

    private ASTType getConverterType(ASTType astType) {
        ASTAnnotation astAnnotation = astType.getASTAnnotation(Parcel.class);
        return astAnnotation.getProperty("value", ASTType.class);
    }

    private boolean isGetter(ASTMethod astMethod) {
        boolean isGetter = astMethod.getParameters().size() == 0 &&
                (astMethod.getName().startsWith(GET) || astMethod.getName().startsWith(IS));
        if (isGetter && astMethod.getReturnType().equals(ASTVoidType.VOID)) {
            throw new TransfuseAnalysisException("Getter cannot return type void");
        }
        return isGetter;
    }

    private String getPropertyName(ASTMethod astMethod) {
        String methodName = astMethod.getName();

        for (String prepend : PREPENDS) {
            if (methodName.startsWith(prepend)) {
                String name = methodName.substring(prepend.length());
                return name.substring(0, 1).toLowerCase(Locale.getDefault()) + name.substring(1);
            }
        }
        throw new TransfuseAnalysisException("Unable to convert Method name " + methodName);
    }
}
