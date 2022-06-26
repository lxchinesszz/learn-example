package learn.common.print;

import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * @author liuxin
 * 2022/5/9 11:43
 */
public class GenericTest<Z extends GenericTest.X & GenericTest.Y> {

    // list是ParameterizedType,<? extends Number> 是WildcardType
    List<? extends Number> list;

    // map是ParameterizedType,Z是TypeVariable
    Map<Z, Z> map;

    // colorConsole 是class
    ColorConsole colorConsole;

    // genericTest 是
    GenericTest<U> genericTest;

    // map2是ParameterizedType,<? extends String>是WildcardType
    Map<? extends String, ? super String> map2;

    List<?>[] lists;

    Z z;

    Z[] zs;

    class U extends X implements Y{}

    class X {
    }

    interface Y {
    }

    @Test
    public void test2(){
        Field c = ReflectionUtils.findField(GenericTest.class, "genericTest");
        Type genericType = c.getGenericType();
        System.out.println(genericType);
    }

    @Test
    public void test() {
        Field c = ReflectionUtils.findField(GenericTest.class, "list");
        ParameterizedType parameterizedType = (ParameterizedType) c.getGenericType();
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        WildcardType wildcardType = (WildcardType) actualTypeArgument;
        // ? extends java.lang.Number
        System.out.println(wildcardType.getTypeName());
        // java.lang.Number
        System.out.println(wildcardType.getUpperBounds()[0].getTypeName());
        // class sun.reflect.generics.reflectiveObjects.WildcardTypeImpl
        System.out.println(wildcardType.getClass());

        Field a = ReflectionUtils.findField(GenericTest.class, "z");
        Type genericType = a.getGenericType();
        TypeVariable typeVariable = (TypeVariable) genericType;
        System.out.println(typeVariable.getName());
        Type[] bounds = typeVariable.getBounds();


        Field map = ReflectionUtils.findField(GenericTest.class, "map");
        Type genericType1 = map.getGenericType();
        Type[] actualTypeArguments = ((ParameterizedType) genericType1).getActualTypeArguments();
        System.out.println(((TypeVariable) actualTypeArguments[0]).getName());
        System.out.println(((TypeVariable) actualTypeArguments[1]).getName());


        Field map2 = ReflectionUtils.findField(GenericTest.class, "map2");
        Type genericType2 = map2.getGenericType();
        Type[] actualTypeArguments2 = ((ParameterizedType) genericType2).getActualTypeArguments();
        System.out.println(((WildcardType) actualTypeArguments2[0]).getTypeName());
        System.out.println(((WildcardType) actualTypeArguments2[1]).getTypeName());

        Field lists = ReflectionUtils.findField(GenericTest.class, "lists");
        Type genericType3 = lists.getGenericType();
        System.out.println(genericType3);

        Field zs = ReflectionUtils.findField(GenericTest.class, "zs");
        Type genericType4 =
                zs.getGenericType();
        System.out.println(genericType4);
    }

}
