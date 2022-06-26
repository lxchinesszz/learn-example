package balance;

import learn.common.print.ColorConsole;
import learn.common.print.Loops;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 权重负载
 *
 * @author liuxin
 * 2022/5/18 11:08
 */
public class WeightBalanceTest2 {

    @Data
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    private static class Server {
        private String host;
        private Integer port;
        // 初始化权重
        private Integer weight;
        // 计算后的当前权重
        private Integer currentWeight;
    }


    public static Server selectServer(List<Server> servers) {
        // 获取总权重
        Integer totalWeight = 0;
        for (Server server : servers) {
            totalWeight += server.getWeight();
        }
        // 根据权重从小到大排序
        List<Server> sortByCurrentWeight = servers.stream().sorted(Comparator.comparing(Server::getCurrentWeight))
                .collect(Collectors.toList());
        // 集合反转,从大到小排序
        Collections.reverse(sortByCurrentWeight);
        // 当前最大权重
        Server maxWeightServer = sortByCurrentWeight.get(0);
        for (Server server : servers) {
            if (server.equals(maxWeightServer)) {
                server.setCurrentWeight(server.getCurrentWeight() - totalWeight);
            }
            server.setCurrentWeight(server.getCurrentWeight() + server.getWeight());
        }
        // 重新计算权重
        return maxWeightServer;
    }

    public static void main(String[] args) {
        Server server1 = new Server("127,0,0,1", 8080, 4, 4);
        Server server2 = new Server("127,0,0,1", 8081, 2, 2);
        Server server3 = new Server("127,0,0,1", 8082, 1, 1);
        List<Server> servers = Arrays.asList(server2, server1, server3);
        Loops.loop(7, i -> {
            ColorConsole.colorPrintln("第{}次,平滑权重轮训{}", i, selectServer(servers));
        });
    }

}
