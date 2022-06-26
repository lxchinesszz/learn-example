package learn.zookeeper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author liuxin
 * @version Id: ZookeeperConfiguration.java, v 0.1 2019-04-17 11:30
 */
@Setter
@Getter
@RequiredArgsConstructor
public class ZookeeperConfiguration {
  /**
   * 连接Zookeeper服务器的列表.
   * 包括IP地址和端口号.
   * 多个地址用逗号分隔.
   * 如: host1:2181,host2:2181
   */
  private final String serverLists;

  /**
   * 命名空间.
   */
  private final String namespace;

  /**
   * 等待重试的间隔时间的初始值.
   * 单位毫秒.
   */
  private int baseSleepTimeMilliseconds = 1000;

  /**
   * 等待重试的间隔时间的最大值.
   * 单位毫秒.
   */
  private int maxSleepTimeMilliseconds = 3000;

  /**
   * 最大重试次数.
   */
  private int maxRetries = 3;

  /**
   * 会话超时时间.
   * 单位毫秒.
   */
  private int sessionTimeoutMilliseconds =1000;

  /**
   * 连接超时时间.
   * 单位毫秒.
   */
  private int connectionTimeoutMilliseconds =2000;

  /**
   * 连接Zookeeper的权限令牌.
   * 缺省为不需要权限验证.
   */
  private String digest;

}
