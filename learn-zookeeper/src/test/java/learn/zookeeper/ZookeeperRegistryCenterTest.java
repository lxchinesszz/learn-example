package learn.zookeeper;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author liuxin
 * @version Id: ZookeeperRegistryCenterTest.java, v 0.1 2019-04-17 15:31
 */
public class ZookeeperRegistryCenterTest {

  @Test
  public void startTest() {
    ZookeeperConfiguration zk = new ZookeeperConfiguration("127.0.0.1:2181", "zk-demo");

    ZookeeperRegistryCenter reg = new ZookeeperRegistryCenter(zk);
    reg.init();

    //创建一种永久的
    reg.persist("/chijiuhua", "/chijiuhua1");

    reg.persist("/chijiuhua/chijiuhua-son", "/chijiuhua2");


    long registryCenterTime = reg.getRegistryCenterTime("/chijiuhua");
    String format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date(registryCenterTime));
    System.out.println(format);


    int numChildren = reg.getNumChildren("/chijiuhua");
    System.out.println("子节点数量:" + numChildren);

    List<String> childrenKeys = reg.getChildrenKeys("/chijiuhua");
    for (String s : childrenKeys) {
      System.out.println("子节点路径名:" + s);
    }

    //对当前节点所有的信息进行监控,节点有变动，就通知服务器，然后保证服务器获取节点信息时候数据是最新的
    reg.addCacheData("/chijiuhua");

    while (true);

  }

  @Test
  public void test2(){
    ZookeeperConfiguration zk = new ZookeeperConfiguration("127.0.0.1:2181", "zk-demo");
    ZookeeperRegistryCenter reg = new ZookeeperRegistryCenter(zk);
    reg.init();

    reg.addCacheData("/chijiuhua");
    String s = reg.get("/chijiuhua");
    System.out.println(s);
  }

}
