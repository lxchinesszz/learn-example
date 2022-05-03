package learn.pool2.connect.factory;

import lombok.SneakyThrows;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * CommonsPool2TargetSource spring中aop时候使用池化技术
 *
 * @author liuxin
 * 2022/4/30 21:05
 */
public class PooledConnectFactory implements PooledObjectFactory<Connection> {


    /**
     * 数据库连接
     */
    private final String url;

    /**
     * 用户名
     */
    private final String userName;

    /**
     * 数据密码
     */
    private final String password;

    public PooledConnectFactory(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 对象被激活后，会进行调用
     *
     * @param pooledObject a {@code PooledObject} wrapping the instance to be activated
     */
    @Override
    public void activateObject(PooledObject<Connection> pooledObject) throws Exception {
    }

    /**
     * 销毁数据库连接
     *
     * @param pooledObject a {@code PooledObject} wrapping the instance to be destroyed
     * @throws Exception 异常
     */
    @Override
    public void destroyObject(PooledObject<Connection> pooledObject) throws Exception {
        Connection connection = pooledObject.getObject();
        connection.close();
    }

    /**
     * 创建一个数据库连接
     *
     * @return 数据库连接的池对象包装
     * @throws Exception 异常
     */
    @Override
    public PooledObject<Connection> makeObject() throws Exception {
        Connection connection = DriverManager.getConnection(this.url, this.userName, this.password);
        return new DefaultPooledObject<>(connection);
    }

    /**
     * 回收资源时候进行调用
     * @param pooledObject a {@code PooledObject} wrapping the instance to be passivated
     *
     * @throws Exception
     */
    @Override
    public void passivateObject(PooledObject<Connection> pooledObject) throws Exception {

    }

    @Override
    @SneakyThrows
    public boolean validateObject(PooledObject<Connection> pooledObject) {
        Connection connection = pooledObject.getObject();
        // 如果连接关闭说明已经失效就返回false告诉池子,已经失效,会自动移除
        return !connection.isClosed();
    }
}
