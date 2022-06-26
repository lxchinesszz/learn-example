package learn.common.print;

import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Map;

/**
 * @author liuxin
 * 2022/5/9 10:48
 */
public class WildcardTypeTest {
    Map<? extends String, ? super Number> map;
    @Test
    public void wildcardTypeTest() {
        Field map = ReflectionUtils.findField(WildcardTypeTest.class, "map");
        Type[] actualTypeArguments = ((ParameterizedType) map.getGenericType()).getActualTypeArguments();
        // ? extends java.lang.String
        System.out.println(((WildcardType) actualTypeArguments[0]).getTypeName());
        // class java.lang.String
        System.out.println(((WildcardType) actualTypeArguments[0]).getLowerBounds()[0]);
    }
}
