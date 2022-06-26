package learn.common.print;

import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liuxin
 * 2022/5/8 21:35
 */
class ColorConsoleTest {

    class A {
    }

    class B {
    }

    class School<T, K> {
    }

    class X {
        public School<A, B> getSchool() {
            return null;
        }
    }

    @Test
    public void test() {
        List<A> list = new ArrayList<A>();
        // Spring的提供工具类,用于字段的泛型信息,Person<String>
        ResolvableType resolvableType = ResolvableType.forInstance(list);
        System.out.println(resolvableType);
        Class<?> resolve1 = resolvableType.getRawClass();
        // class learn.common.print.ColorConsoleTest$A
        System.out.println(resolve1);
        Type genericSuperclass = Class.class.getGenericSuperclass();
    }

    class Y {
        private Map<A, B> map;

        private List<A>[] list;
    }


    /**
     * 数组
     */
    @Test
    public void GenericArrayTypeTest() {
        Field list = ReflectionUtils.findField(Y.class, "list");
        Type genericType = list.getGenericType();
        if (genericType instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) genericType).getGenericComponentType();
            System.out.println(genericComponentType);
        }
    }

    /**
     * 参数
     */
    @Test
    public void ParameterizedTypeTest() {
        Field list = ReflectionUtils.findField(Y.class, "map");
        Type genericType = list.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
//                learn.common.print.ColorConsoleTest$A
//                learn.common.print.ColorConsoleTest$B
                System.out.println(actualTypeArgument.getTypeName());
            }
            System.out.println(parameterizedType.getOwnerType());
            // interface java.util.Map
            System.out.println(parameterizedType.getRawType());
            // java.util.Map<learn.common.print.ColorConsoleTest$A, learn.common.print.ColorConsoleTest$B>
            System.out.println(parameterizedType.getTypeName());
        }
    }

    class Z<K extends String, V extends Integer> {
        Z<? extends String, ? extends Integer> z;
    }

    @Test
    public void testTypeVariableTest() {
        Field z = ReflectionUtils.findField(Z.class, "z");
        Type genericType = z.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                if (actualTypeArgument instanceof WildcardType) {
                    for (Type lowerBound : ((WildcardType) actualTypeArgument).getLowerBounds()) {
                        System.out.println(lowerBound);
                    }
                    for (Type upperBound : ((WildcardType) actualTypeArgument).getUpperBounds()) {
                        System.out.println(upperBound);
                    }
                    System.out.println(((WildcardType) actualTypeArgument).getLowerBounds());
                    System.out.println(((WildcardType) actualTypeArgument).getUpperBounds());
                    System.out.println(((WildcardType) actualTypeArgument).getTypeName());
                }
            }
        }

    }
}