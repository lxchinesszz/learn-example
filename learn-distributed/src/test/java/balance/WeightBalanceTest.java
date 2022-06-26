package balance;

import learn.common.print.ColorConsole;
import learn.common.print.Loops;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 权重负载
 *
 * @author liuxin
 * 2022/5/18 11:08
 */
public class WeightBalanceTest {

    private static class Server {

        private String host;

        private Integer port;

        public Server(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public String toString() {
            return "Server{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

    private static final AtomicInteger atomicInteger = new AtomicInteger();

    public static Server round(List<Server> services) {
        int count = atomicInteger.get();
        if (count >= Integer.MAX_VALUE) {
            atomicInteger.set(0);
        }
        atomicInteger.incrementAndGet();
        Server[] toArray = services.toArray(new Server[0]);
        return toArray[count % toArray.length];
    }

    public static void main(String[] args) {
        Map<Server, Integer> confWeight = new HashMap<>();
        confWeight.put(new Server("127.0.0.1", 80), 2);
        confWeight.put(new Server("127.0.0.1", 81), 3);
        confWeight.put(new Server("127.0.0.1", 82), 5);

        List<Server> servers = new ArrayList<>();
        for (Map.Entry<Server, Integer> entity : confWeight.entrySet()) {
            Server server = entity.getKey();
            Integer weight = entity.getValue();
            for (int i = 0; i < weight; i++) {
                servers.add(server);
            }
        }
        /**
         * 但这样还是不均匀的, 相同的ip可能被连续的访问到其实就没有做到负载均衡。
         * 第0次,权重轮训Server{host='127.0.0.1', port=80}
         * 第1次,权重轮训Server{host='127.0.0.1', port=80}
         * 第2次,权重轮训Server{host='127.0.0.1', port=82}
         * 第3次,权重轮训Server{host='127.0.0.1', port=82}
         * 第4次,权重轮训Server{host='127.0.0.1', port=82}
         * 第5次,权重轮训Server{host='127.0.0.1', port=82}
         * 第6次,权重轮训Server{host='127.0.0.1', port=82}
         * 第7次,权重轮训Server{host='127.0.0.1', port=81}
         * 第8次,权重轮训Server{host='127.0.0.1', port=81}
         * 第9次,权重轮训Server{host='127.0.0.1', port=81}
         */
        Loops.loop(10, i -> {
            ColorConsole.colorPrintln("第{}次,权重轮训{}", i, round(servers));
        });
    }

}
