package learn.common.print;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;

/**
 * @author liuxin
 * 2022/5/9 15:34
 */
public class Test {

    class Girl implements Person {
    }

    class Boy implements Person {
    }

    interface Person {
    }

    class School<A extends Boy & Person> {
    }

    Boy boy;

    School<Boy> boySchool;

    School<Boy>[] schools;

    @org.junit.jupiter.api.Test
    public void test() {
        // class java.lang.Class
        System.out.println(ReflectionUtils.findField(Test.class, "boy").getGenericType().getClass());
        // class sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
        System.out.println(ReflectionUtils.findField(Test.class, "boySchool").getGenericType().getClass());
        // class sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl
        System.out.println(ReflectionUtils.findField(Test.class, "schools").getGenericType().getClass());
    }

    @org.junit.jupiter.api.Test
    public void test2() {
        GenericArrayType schoolsArrayType = (GenericArrayType) ReflectionUtils.findField(Test.class, "schools").getGenericType();
        Type genericComponentType = schoolsArrayType.getGenericComponentType();
        // learn.common.print.Test$School<learn.common.print.Test$Boy>
        System.out.println(genericComponentType.getTypeName());
        // class sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
        System.out.println(genericComponentType.getClass());
    }


    class TypeVariableObj<A extends Number> {

        A a;

        TypeVariableObj<A> as;
    }

    @org.junit.jupiter.api.Test
    public void test3() {
        Type a = ReflectionUtils.findField(TypeVariableObj.class, "a").getGenericType();
        // class sun.reflect.generics.reflectiveObjects.TypeVariableImpl
        System.out.println(a.getClass());

        Type as = ReflectionUtils.findField(TypeVariableObj.class, "as").getGenericType();
        // class sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
        System.out.println(as.getClass());
        Type[] actualTypeArguments = ((ParameterizedType) as).getActualTypeArguments();
        // A 因为只有1个泛型,所以直接去下标0
        TypeVariable actualTypeArgument = (TypeVariable) actualTypeArguments[0];
        System.out.println(actualTypeArgument.getTypeName());
    }
}
