package learn.jvm.oom;

import java.nio.ByteBuffer;

/**
 * @author liuxin
 * 2022/6/5 18:59
 */
public class DirectBufferOverflowTest {

    /**
     * -XX:MaxDirectMemorySize=100M
     * 设置最大可分配内存
     *
     * @param args Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory
     */
    public static void main(String[] args) {
        //分配128MB直接内存
        ByteBuffer bb = ByteBuffer.allocateDirect(1024 * 1024 * 128);
    }
}
