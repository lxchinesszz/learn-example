package learn.jvm;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author liuxin
 * 2022/6/2 23:42
 */
public class Heap {
    /**
     * -XX:+PrintClassHistogramBeforeFullGC
     * -XX:+PrintGCDetails
     * JVM初始内存 -Xms50m
     * 最大堆大小 -Xmx50m
     * 年轻代大小 -Xmn10m
     * 年轻代和年老代的比值 -XX:NewRatio=2
     * Eden区与两个Survivor区的比值 -XX:SurvivorRatio=4
     * -XX:MetaspaceSize=230m
     * -XX:MaxMetaspaceSize=230m
     * -XX:+PrintGCDateStamps
     * -Xloggc:/Users/liuxin/AccessGroup/gitlab/purchase-center/app-gc.log
     * -verbose:class
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        List<Heap> result = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            Heap heap = new Heap();
            result.add(heap);
        }
        while (true) ;
    }
}
