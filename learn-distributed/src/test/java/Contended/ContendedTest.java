package Contended;

import org.junit.jupiter.api.Test;
import sun.misc.Contended;

/**
 * @author liuxin
 * 2022/5/20 11:02
 */
public class ContendedTest {

    @Contended
    volatile int a;

    @Contended
    volatile int b;

    @Test
    public void test() throws Exception {
        ContendedTest c = new ContendedTest();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000_0000L; i++) {
                c.a = i;
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000_0000L; i++) {
                c.b = i;
            }
        });
        final long start = System.nanoTime();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println((System.nanoTime() - start) / 100_0000);
    }
}

