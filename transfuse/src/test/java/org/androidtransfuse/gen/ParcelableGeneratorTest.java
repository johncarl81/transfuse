package org.androidtransfuse.gen;

import android.os.Parcel;
import android.os.Parcelable;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.ParcelableAnalysis;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ParcelableDescriptor;
import org.androidtransfuse.util.ParcelWrapper;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
@PrepareForTest(Parcel.class)
public class ParcelableGeneratorTest {

    private static final String TEST_VALUE = "test value";

    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private ParcelableGenerator parcelableGenerator;
    @Inject
    private ParcelsGenerator parcelsGenerator;
    @Inject
    private ParcelableAnalysis parcelableAnalysis;

    private ParcelTarget parcelTarget;
    private Parcel mockParcel;
    private Parcelable mockSecondParcel;
    private Class<Parcelable> parcelableClass;
    private ParcelSecondTarget parcelSecondTarget;

    @Before
    public void setup() throws ClassNotFoundException, IOException {
        TransfuseTestInjector.inject(this);

        ASTType mockParcelASTType = astClassFactory.buildASTClassType(ParcelTarget.class);
        ASTType mockParcelTwoASTType = astClassFactory.buildASTClassType(ParcelSecondTarget.class);

        ParcelableDescriptor parcelableDescriptor = parcelableAnalysis.analyze(mockParcelASTType);
        ParcelableDescriptor parcelableTwoDescriptor = parcelableAnalysis.analyze(mockParcelTwoASTType);

        JDefinedClass parcelableDefinedClass = parcelableGenerator.generateParcelable(mockParcelASTType, parcelableDescriptor);
        JDefinedClass parcelableTwoDefinedClass = parcelableGenerator.generateParcelable(mockParcelTwoASTType, parcelableTwoDescriptor);

        Map<ASTType, JDefinedClass> generated = new HashMap<ASTType, JDefinedClass>();
        generated.put(mockParcelASTType, parcelableDefinedClass);
        generated.put(mockParcelTwoASTType, parcelableTwoDefinedClass);

        parcelsGenerator.generate(generated);

        ClassLoader classLoader = codeGenerationUtil.build();
        parcelableClass = (Class<Parcelable>) classLoader.loadClass(parcelableGenerator.getParcelable(mockParcelASTType).fullName());
        Class parcelableTwoClass = classLoader.loadClass(parcelableGenerator.getParcelable(mockParcelTwoASTType).fullName());

        parcelTarget = new ParcelTarget();
        parcelSecondTarget = new ParcelSecondTarget();

        parcelTarget.setDoubleValue(Math.PI);
        parcelTarget.setStringValue(TEST_VALUE);
        parcelTarget.setSecondTarget(parcelSecondTarget);

        parcelSecondTarget.setValue(TEST_VALUE);

        mockParcel = PowerMockito.mock(Parcel.class);
        mockSecondParcel = (Parcelable) PowerMockito.mock(parcelableTwoClass);
    }

    @Test
    public void testGeneratedParcelable() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        when(mockParcel.readString()).thenReturn(TEST_VALUE);
        when(mockParcel.readDouble()).thenReturn(Math.PI);
        when(mockParcel.readParcelable(any(ClassLoader.class))).thenReturn(mockSecondParcel);
        when(((ParcelWrapper) mockSecondParcel).getParcel()).thenReturn(parcelSecondTarget);

        Parcelable outputParcelable = parcelableClass.getConstructor(ParcelTarget.class).newInstance(parcelTarget);

        outputParcelable.writeToParcel(mockParcel, 0);

        Parcelable inputParcelable = parcelableClass.getConstructor(Parcel.class).newInstance(mockParcel);

        ParcelTarget wrapped = ((ParcelWrapper<ParcelTarget>) inputParcelable).getParcel();

        assertEquals(parcelTarget, wrapped);

        verify(mockParcel).writeString(TEST_VALUE);
        verify(mockParcel).writeDouble(Math.PI);
        verify(mockParcel).writeParcelable(any(Parcelable.class), eq(0));
    }
}
