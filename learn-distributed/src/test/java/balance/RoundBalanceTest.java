package balance;

import learn.common.print.ColorConsole;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuxin
 * 2022/5/18 11:25
 */
public class RoundBalanceTest {

    public static void main(String[] args) {
        List<String> services = Arrays.asList("service1", "service2", "service3");
        XxlBalanceTest.manyRoute(i -> {
            // 请求次数,取模。serviceKey 可以更细粒度的控制轮训
            ColorConsole.colorPrintln("轮训负载({}):{}", i, round(services));
        });
    }

    private static final AtomicInteger atomicInteger = new AtomicInteger();

    public static String round(List<String> services) {
        int count = atomicInteger.get();
        if (count >= Integer.MAX_VALUE) {
            atomicInteger.set(0);
        }
        atomicInteger.incrementAndGet();
        String[] toArray = services.toArray(new String[0]);
        return toArray[count % toArray.length];
    }
}
