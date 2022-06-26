package learn.common.print;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author liuxin
 * 2022/5/9 10:18
 */
public class TypeVariableTest<A extends TypeVariableTest.X & TypeVariableTest.Y & TypeVariableTest.Z> {

    private A a;

    @Data
    static class X {
    }

    interface Y {
    }

    interface Z{}


    /**
     * 处理泛型是变量
     * eg: 字段A 是变量。
     */
    @Test
    public void typeVariableTest() {
        Field a = ReflectionUtils.findField(TypeVariableTest.class, "a");
        Type genericType = a.getGenericType();
        if (genericType instanceof TypeVariable) {
            TypeVariable typeVariable = ((TypeVariable<?>) genericType);
            Type[] bounds = typeVariable.getBounds();
            for (Type bound : bounds) {
                // 1. class learn.common.print.TypeVariableTest$X
                // 2. interface learn.common.print.TypeVariableTest$Y
                // 3. interface learn.common.print.TypeVariableTest$Z
                System.out.println(bound);
            }
            // A
            System.out.println(typeVariable.getName());
            // A
            System.out.println(typeVariable.getTypeName());
            // class learn.common.print.TypeVariableTest
            System.out.println(typeVariable.getGenericDeclaration());
            System.out.println(typeVariable.getAnnotatedBounds());
        }
    }
}
