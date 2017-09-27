package org.androidtransfuse.adapter;

import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by john on 12/24/16.
 */
public class GenericsUtilTest {

    static class SuperType<SuperT, SuperR, SuperS, IntermediateR> {
        public SuperT value;
        public Map<List<SuperT>, SuperS> collection;


        public SuperR getValue(SuperS parameter){return null;}
    }

    static class Intermediate<IntermediateV, IntermediateR, IntermediateT, IntermediateS> extends SuperType<IntermediateR, IntermediateT, IntermediateS, Void> {}

    static class SubType extends Intermediate<String, TargetType1, TargetType2, TargetType3> {}

    static class TargetType1 {}
    static class TargetType2 {}
    static class TargetType3 {}

    private ASTClassFactory classFactory;
    private ASTType superType;
    private ASTType subType;
    private ASTType targetType1;
    private ASTType targetType2;
    private ASTType targetType3;
    private ASTType superS;

    @Before
    public void setup() {
        classFactory = new ASTClassFactory();
        superType = classFactory.getType(SuperType.class);
        subType = classFactory.getType(SubType.class);
        targetType1 = classFactory.getType(TargetType1.class);
        targetType2 = classFactory.getType(TargetType2.class);
        targetType3 = classFactory.getType(TargetType3.class);
        superS = new ASTGenericParameterType(new ASTGenericArgument("SuperS"), new ASTStringType(Object.class.getName()));
    }

    @Test
    public void testFieldType() throws NoSuchFieldException {
        ASTField field = ASTUtils.getInstance().findField(superType, "value");

        ASTType value = GenericsUtil.getInstance().getType(subType, superType, field.getASTType());

        assertEquals(targetType1, value);
    }

    @Test
    public void testMethodReturnType() {
        ASTMethod method = ASTUtils.getInstance().findMethod(superType, "getValue", superS);

        ASTType returnType = GenericsUtil.getInstance().getType(subType, superType, method.getReturnType());

        assertEquals(targetType2, returnType);
    }

    @Test
    public void testMethodParameterType() {
        ASTMethod method = ASTUtils.getInstance().findMethod(superType, "getValue", superS);

        ASTType returnType = GenericsUtil.getInstance().getType(subType, superType, method.getParameters().get(0).getASTType());

        assertEquals(targetType3, returnType);
    }

    @Test
    public void testGenericTypes() {
        ASTField field = ASTUtils.getInstance().findField(superType, "collection");

        ASTType value = GenericsUtil.getInstance().getType(subType, superType, field.getASTType());

        List<ASTType> arguments = ((ASTGenericTypeWrapper)value).getGenericArgumentTypes();

        ASTType subArgument1 = arguments.get(0).getGenericArgumentTypes().get(0);
        ASTType subArgument2 = arguments.get(1);

        assertEquals(targetType1, subArgument1);
        assertEquals(targetType3, subArgument2);
    }
}