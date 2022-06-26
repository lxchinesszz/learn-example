package learn.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;

/**
 * 监听器:
 * 1. 当一个节点在zookeeper更新,就像所有的客户端发起一个事件
 *
 * 如何使用该原理:
 * 在服务端接口注册生成URL将数据绑定节点上。
 *
 *
 * 客户端在接口实例化时候,到Zookeeper上去查询节点的数据。拿到服务的地址等信息。 生成代理,代理中当方法执行就
 *
 * 节点:
 * /root/{服务全路径名}/provider
 * dada:
 * {协议名}://192.168.0.80:12345/learn.service.Person?anyhost=true&application=person&dubbo=2.5.3&interface=learn.service.Person&methods=eat,say&pid=39641&revision=1.0.0&side=provider&threads=200&timestamp=1536047734960&version=1.0.0
 *
 *  @author liuxin
 * @version Id: ListenerTest.java, v 0.1 2019-02-22 11:04
 */
public class ListenerTest {
  private static final String ROOT_PATH = "/dubboz/cache";

  private static final String connect_info = "127.0.0.1:2181";

  /**
   * 能监听某一个子节点下面的所有节点的操作
   * 这里只能监听: /dubboz/cache的子节点,不能监听子节点的子节点
   *
   * @throws Exception
   */
  @Test
  public void pathChildrenCacheTest() throws Exception {
    ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory.newClient(connect_info,
      exponentialBackoffRetry);
    client.start();
    client.usingNamespace("dubboz");


    PathChildrenCache cache = new PathChildrenCache(client, ROOT_PATH, true);
    cache.start();

    PathChildrenCacheListener cacheListener = new PathChildrenCacheListener() {
      public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
        System.out.println("事件类型：" + event.getType());
        if (null != event.getData()) {
          System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
        }
      }
    };

    cache.getListenable().addListener(cacheListener);
    client.create().creatingParentsIfNeeded().forPath("/dubboz/cache/test01", "01".getBytes());
    Thread.sleep(10);
    client.create().creatingParentsIfNeeded().forPath("/dubboz/cache/test02", "02".getBytes());
    Thread.sleep(10);
    client.setData().forPath("/dubboz/cache/test01", "03".getBytes());
    Thread.sleep(10);
    client.setData().forPath("/dubboz/cache/test02", "04".getBytes());
    Thread.sleep(10);
    client.delete().deletingChildrenIfNeeded().forPath("/dubboz/cache/test01");
    Thread.sleep(10);
    client.delete().deletingChildrenIfNeeded().forPath("/dubboz/cache/test02");
    Thread.sleep(10);
    cache.close();
    client.close();
  }

  /**
   * 只能监听某一个节点的变化
   *
   * @throws Exception
   */
  @Test
  public void nodeCacheListenerTest() throws Exception {
    ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory.newClient(connect_info,
      exponentialBackoffRetry);
    client.start();
    client.usingNamespace("dubboz");

    final NodeCache cache = new NodeCache(client, ROOT_PATH);

    NodeCacheListener listener = new NodeCacheListener() {
      public void nodeChanged() throws Exception {
        ChildData data = cache.getCurrentData();
        if (null != data) {
          System.out.println("节点数据：" + new String(cache.getCurrentData().getData()));
        } else {
          System.out.println("节点被删除!");
        }
      }
    };
    cache.getListenable().addListener(listener);
    cache.start();

    client.create().creatingParentsIfNeeded().forPath(ROOT_PATH);
    client.setData().forPath(ROOT_PATH, "01".getBytes());
    Thread.sleep(100);
    client.setData().forPath(ROOT_PATH, "02".getBytes());
    Thread.sleep(100);
    client.delete().deletingChildrenIfNeeded().forPath(ROOT_PATH);
    Thread.sleep(1000 * 2);
    cache.close();
    client.close();
    System.out.println("OK!");
  }
}

