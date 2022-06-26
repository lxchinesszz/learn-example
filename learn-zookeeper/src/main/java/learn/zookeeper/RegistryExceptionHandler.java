package learn.zookeeper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;

/**
 * @author liuxin
 * @version Id: RegExceptionHandler.java, v 0.1 2019-04-17 13:47
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegistryExceptionHandler {

  /**
   * 处理异常.
   *
   * <p>处理掉中断和连接失效异常并继续抛注册中心.</p>
   *
   * @param cause 待处理异常.
   */
  public static void handleException(final Exception cause) {
    if (null == cause) {
      return;
    }
    if (isIgnoredException(cause) || null != cause.getCause() && isIgnoredException(cause.getCause())) {
      log.debug("Elastic job: ignored exception for: {}", cause.getMessage());
    } else if (cause instanceof InterruptedException) {
      Thread.currentThread().interrupt();
    } else {
      throw new RegistryException(cause);
    }
  }

  /**
   * 忽略的异常
   *
   * @param cause 异常
   * @return
   */
  private static boolean isIgnoredException(final Throwable cause) {
    return cause instanceof KeeperException.ConnectionLossException || cause instanceof KeeperException.NoNodeException || cause instanceof KeeperException.NodeExistsException;
  }
}

final class RegistryException extends RuntimeException {

  private static final long serialVersionUID = -6417179023552012152L;

  public RegistryException(final String errorMessage, final Object... args) {
    super(String.format(errorMessage, args));
  }

  public RegistryException(final Exception cause) {
    super(cause);
  }
}
