package balance;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;
import learn.common.print.ColorConsole;

import java.util.Arrays;
import java.util.List;

/**
 * @author liuxin
 * 2022/5/18 15:00
 */
public class FeignBalanceTest {

    public static void main(String[] args) {
        ILoadBalancer iLoadBalancer = LoadBalancerBuilder.newBuilder().buildLoadBalancerFromConfigWithReflection();
        Server server1 = new Server("127.0.0.1", 8081);
        Server server2 = new Server("127.0.0.1", 8082);
        server1.setZone("user");
        server2.setZone("goods");
        List<Server> servers = Arrays.asList(server1, server2);
        System.out.println(iLoadBalancer.getClass());
        iLoadBalancer.addServers(servers);
        ManyRoute.manyRoute(10, i -> {
            // class com.netflix.loadbalancer.ZoneAwareLoadBalancer
            // com.netflix.loadbalancer.AvailabilityFilteringRule
            //   com.netflix.loadbalancer.DummyPing 检查服务器是否可用
            ColorConsole.colorPrintln("第{}次,{}", i, iLoadBalancer.chooseServer("user"));
        });
    }

}
