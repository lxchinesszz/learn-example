package learn.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxin
 * 2022/6/5 18:49
 */
public class HeapOverflowTest {

    /**
     * 设置堆大小 -Xms10m -Xmx10m
     * 10 * 1024 * 1024
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     *
     * @param args
     */
    public static void main(String[] args) {
        // 10m = (10 * 1024)kb = (10 * 1024 * 1024)b
        byte[] b = new byte[10 * 1024 * 1024];
    }
}
