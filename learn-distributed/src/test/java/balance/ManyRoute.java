package balance;

import java.util.function.Consumer;

/**
 * @author liuxin
 * 2022/5/18 15:04
 */
public class ManyRoute {

    public static void manyRoute(Consumer<Integer> consumer) {
        manyRoute(3, consumer);
    }

    public static void manyRoute(Integer loopCount, Consumer<Integer> consumer) {
        for (int i = 0; i < loopCount; i++) {
            consumer.accept(i);
        }
    }

}
