/**
 * Copyright 2012 John Ericksen
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
import org.androidtransfuse.util.Providers;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.inject.Inject;
import javax.inject.Provider;
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

        ASTType mockParcelASTType = astClassFactory.getType(ParcelTarget.class);
        ASTType mockParcelTwoASTType = astClassFactory.getType(ParcelSecondTarget.class);

        ParcelableDescriptor parcelableDescriptor = parcelableAnalysis.analyze(mockParcelASTType);
        ParcelableDescriptor parcelableTwoDescriptor = parcelableAnalysis.analyze(mockParcelTwoASTType);

        JDefinedClass parcelableDefinedClass = parcelableGenerator.generateParcelable(mockParcelASTType, parcelableDescriptor);
        JDefinedClass parcelableTwoDefinedClass = parcelableGenerator.generateParcelable(mockParcelTwoASTType, parcelableTwoDescriptor);

        Map<Provider<ASTType>, JDefinedClass> generated = new HashMap<Provider<ASTType>, JDefinedClass>();
        generated.put(Providers.of(mockParcelASTType), parcelableDefinedClass);
        generated.put(Providers.of(mockParcelTwoASTType), parcelableTwoDefinedClass);

        parcelsGenerator.generate(generated);

        ClassLoader classLoader = codeGenerationUtil.build();
        parcelableClass = (Class<Parcelable>) classLoader.loadClass(parcelableDefinedClass.fullName());
        Class parcelableTwoClass = classLoader.loadClass(parcelableTwoDefinedClass.fullName());

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
