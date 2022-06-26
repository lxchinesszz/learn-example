package balance;

import com.xxl.rpc.remoting.invoker.route.impl.XxlRpcLoadBalanceConsistentHashStrategy;
import com.xxl.rpc.remoting.invoker.route.impl.XxlRpcLoadBalanceLFUStrategy;
import com.xxl.rpc.remoting.invoker.route.impl.XxlRpcLoadBalanceLRUStrategy;
import learn.common.print.ColorConsole;
import learn.common.print.Loops;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author liuxin
 * 2022/5/18 10:09
 */
public class XxlBalanceTest {

    public static void manyRoute(Consumer<Integer> consumer) {
        for (int i = 0; i < 3; i++) {
            consumer.accept(i);
        }
    }

    public static void main(String[] args) {
        List<String> services = Arrays.asList("service1", "service2", "service3");
        manyRoute(i -> {
            ColorConsole.colorPrintln("随机负载({}):{}", i, random(services));
        });
        System.out.println("----------------------------");
        manyRoute(i -> {
            // 请求次数,取模。serviceKey 可以更细粒度的控制轮训
            ColorConsole.colorPrintln("轮训负载({}):{}", i, round("123", services));
        });
        XxlRpcLoadBalanceLRUStrategy lruStrategy = new XxlRpcLoadBalanceLRUStrategy();
        manyRoute(i -> {
            // LRU，即：最近最少使用淘汰算法（Least Recently Used）。LRU是淘汰最长时间没有被使用的页面。
            // 利用迭代器进行轮训: lruItem.entrySet().iterator().next().getKey().并且最长时间没有被用到的会被删除。
            TreeSet<String> stringTreeSet = new TreeSet<>(services);
            ColorConsole.colorPrintln("LRU负载({}):{}", i, lruStrategy.route("123", stringTreeSet));
        });
        // LFU，即：最不经常使用淘汰算法（Least Frequently Used）。LFU是淘汰一段时间内，使用次数最少的页面。
        // 最少用的，优先被轮训到
        TreeSet<String> stringTreeSet = new TreeSet<>(services);
        XxlRpcLoadBalanceLFUStrategy lfuStrategy = new XxlRpcLoadBalanceLFUStrategy();
        // 先调用两次预热一下。
        System.out.println(lfuStrategy.route("123", stringTreeSet));
        System.out.println(lfuStrategy.route("123", stringTreeSet));
        // 根据最少使用会被先用的原则,第一次打印的一定是新的
        manyRoute(i -> {
            // LRU，即：最近最少使用淘汰算法（Least Recently Used）。LRU是淘汰最长时间没有被使用的页面。
            // 利用迭代器进行轮训: lruItem.entrySet().iterator().next().getKey().并且最长时间没有被用到的会被删除。
            ColorConsole.colorPrintln("LFU负载({}):{}", i, lfuStrategy.route("123", stringTreeSet));
        });

        XxlRpcLoadBalanceConsistentHashStrategy hashStrategy = new XxlRpcLoadBalanceConsistentHashStrategy();
        Loops.loop(10,i->{
            ColorConsole.colorPrintln("一致性Hash负载({}):{}", i, hashStrategy.route("123", stringTreeSet));
        });
    }

    // XxlRpcLoadBalanceRandomStrategy
    public static String random(List<String> services) {
        Random random = new Random();
        String[] addressArr = services.toArray(new String[0]);
        // random
        return addressArr[random.nextInt(services.size())];
    }

    // XxlRpcLoadBalanceRoundStrategy

    public static String round(String serviceKey, List<String> services) {
        // arr
        String[] addressArr = services.toArray(new String[services.size()]);
        // round 请求次数,取模。可以更细粒度的控制轮训
        return addressArr[count(serviceKey) % addressArr.length];
    }

    private static ConcurrentHashMap<String, Integer> routeCountEachJob = new ConcurrentHashMap<String, Integer>();
    private static long CACHE_VALID_TIME = 0;

    private static int count(String serviceKey) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachJob.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        }

        // count++
        Integer count = routeCountEachJob.get(serviceKey);
        count = (count == null || count > 1000000) ? (new Random().nextInt(100)) : ++count;  // 初始化时主动Random一次，缓解首次压力
        routeCountEachJob.put(serviceKey, count);
        return count;
    }
}
