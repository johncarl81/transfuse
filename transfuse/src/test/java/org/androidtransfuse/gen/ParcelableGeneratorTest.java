package org.androidtransfuse.gen;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidtransfuse.analysis.ParcelableAnalysis;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.util.JavaUtilLogger;
import org.androidtransfuse.util.ParcelableWrapper;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

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
    private ParcelableAnalysis parcelableAnalysis;

    private ParcelTarget parcelTarget;
    private Parcel mockParcel;
    private Parcelable mockSecondParcel;
    private Class<Parcelable> parcelableClass;
    private Class parcelableTwoClass;
    private ParcelSecondTarget parcelSecondTarget;

    @Before
    public void setup() throws ClassNotFoundException, IOException {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new TransfuseGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        ASTType mockParcelASTType = astClassFactory.buildASTClassType(ParcelTarget.class);
        ASTType mockParcelTwoASTType = astClassFactory.buildASTClassType(ParcelSecondTarget.class);

        List<GetterSetterMethodPair> methodPair = parcelableAnalysis.analyze(mockParcelASTType);

        parcelableGenerator.generateParcelable(mockParcelASTType, methodPair);

        ClassLoader classLoader = codeGenerationUtil.build();
        parcelableClass = (Class<Parcelable>) classLoader.loadClass(parcelableGenerator.getParcelable(mockParcelASTType).fullName());
        parcelableTwoClass = classLoader.loadClass(parcelableGenerator.getParcelable(mockParcelTwoASTType).fullName());

        parcelTarget = new ParcelTarget();
        parcelSecondTarget = new ParcelSecondTarget();

        parcelTarget.setDoubleValue(Math.PI);
        parcelTarget.setStringValue(TEST_VALUE);
        parcelTarget.setSecondTarget(parcelSecondTarget);

        parcelSecondTarget.setValue(TEST_VALUE);

        mockParcel = PowerMock.createMock(Parcel.class);
        mockSecondParcel = (Parcelable) PowerMock.createMock(parcelableTwoClass);
    }

    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        PowerMock.reset(mockParcel, mockSecondParcel);

        mockParcel.writeString(TEST_VALUE);
        mockParcel.writeDouble(Math.PI);
        mockParcel.writeParcelable(EasyMock.anyObject(Parcelable.class), EasyMock.eq(0));

        EasyMock.expect(mockParcel.readString()).andReturn(TEST_VALUE);
        EasyMock.expect(mockParcel.readDouble()).andReturn(Math.PI);
        EasyMock.expect(mockParcel.readParcelable(null)).andReturn(mockSecondParcel);
        EasyMock.expect(((ParcelableWrapper) mockSecondParcel).getWrapped()).andReturn(parcelSecondTarget);

        PowerMock.replay(mockParcel, mockSecondParcel);

        Parcelable outputParcelable = parcelableClass.getConstructor(ParcelTarget.class).newInstance(parcelTarget);

        outputParcelable.writeToParcel(mockParcel, 0);

        Parcelable inputParcelable = parcelableClass.getConstructor(Parcel.class).newInstance(mockParcel);

        ParcelTarget wrapped = ((ParcelableWrapper<ParcelTarget>) inputParcelable).getWrapped();

        assertEquals(parcelTarget, wrapped);

        PowerMock.verify(mockParcel, mockSecondParcel);
    }
}
