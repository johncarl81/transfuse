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
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ParcelableAnalysis implements Analysis<ParcelableDescriptor> {

    private static final String GET = "get";
    private static final String SET = "set";
    private Map<ASTType, ParcelableDescriptor> parcelableCache = new HashMap<ASTType, ParcelableDescriptor>();
    private ASTClassFactory astClassFactory;

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

    public ParcelableDescriptor innerAnalyze(ASTType astType) {

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
                    if (!methodNameMap.containsKey(setterName)) {
                        throw new TransfuseAnalysisException("Unable to find setter " + setterName + " to match getter " + astMethod.getName());
                    }

                    ASTMethod setterMethod = methodNameMap.get(setterName);

                    if (setterMethod.getParameters().size() != 1 || !setterMethod.getParameters().get(0).getASTType().equals(astMethod.getReturnType())) {
                        throw new TransfuseAnalysisException("Setter " + setterName + " has incorrect parameters.");
                    }

                    parcelableDescriptor.getGetterSetterPairs().add(new GetterSetterMethodPair(astMethod, setterMethod));
                }
            }
        }

        return parcelableDescriptor;
    }

    private boolean converterDefined(ASTType astType) {

        ASTType converterType = getConverterType(astType);
        ASTType emptyConverterType = astClassFactory.buildASTClassType(ParcelConverter.EmptyConverter.class);
        return converterType != null && !converterType.equals(emptyConverterType);
    }

    private ASTType getConverterType(ASTType astType) {
        ASTAnnotation astAnnotation = astType.getASTAnnotation(Parcel.class);
        return astAnnotation.getProperty("value", ASTType.class);
    }

    private boolean isGetter(ASTMethod astMethod) {
        boolean isGetter = astMethod.getParameters().size() == 0 && astMethod.getName().startsWith(GET);
        if (isGetter && astMethod.getReturnType().equals(ASTVoidType.VOID)) {
            throw new TransfuseAnalysisException("Getter cannot return type void");
        }
        return isGetter;
    }
}
