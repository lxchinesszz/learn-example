package learn.jvm.oom;

import learn.common.print.Sleeps;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 超过98%的时间用来做GC并且回收了不到2%的堆内存时会抛出此异常。
 *
 * @author liuxin
 * 2022/6/5 19:00
 */
public class GCoverheadLimitExceededTest {

    /**
     * @param args Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
     **/
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executor.execute(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    //do nothing
                }
            });
        }
    }
}
