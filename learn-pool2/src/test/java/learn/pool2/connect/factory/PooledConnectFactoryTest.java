package learn.pool2.connect.factory;


import learn.common.print.ColorConsole;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxin
 * 2022/4/30 21:19
 */
public class PooledConnectFactoryTest {

    @Test
    @DisplayName("验证回收对象")
    public void testReturn()throws Exception{
        // 1. 构建一个数据连接池化工厂
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/test";
        String user = "root";
        String pass = "123456";
        PooledConnectFactory pooledConnectFactory = new PooledConnectFactory(dbUrl, user, pass);

        // 2. 给池子添加支持的配置信息
        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<Connection>();
        // 2.1 最大池化对象数量
        config.setMaxTotal(5);
        // 2.2 最大空闲池化对象数量
        config.setMaxIdle(2);
        // 2.3 最小空闲池化对象数量
        config.setMinIdle(2);
        // 2.4 间隔多久检查一次池化对象状态,驱逐空闲对象,检查最小空闲数量小于就创建
        config.setTimeBetweenEvictionRuns(Duration.ofSeconds(5));
        // 2.5 阻塞就报错
        config.setBlockWhenExhausted(true);
        // 2.6 最大等待时长超过5秒就报错,如果不配置一直进行等待
        config.setMaxWait(Duration.ofSeconds(5));
        // 2.7 是否开启jmx监控,默认开启
        config.setJmxEnabled(true);
        // 2.8 一定要符合命名规则,否则无效
        config.setJmxNameBase("org.apache.commons.pool2:type=MysqlConnObjectPool,name=ConnectJmxNameBase");
        // 生成数据库连接池
        // 连接池配置最大5个连接setMaxTotal(5),但是获取6次,那么有一次获取不到就会阻塞setBlockWhenExhausted(true),
        // 当等待了10秒setMaxWait(Duration.ofSeconds(10))还是获取不到。就直接报错
        try (GenericObjectPool<Connection> connPool = new GenericObjectPool<>(pooledConnectFactory, config)) {
            for (int i = 1; i <= 7; i++) {
                Connection connection = connPool.borrowObject();
                Statement statement = connection.createStatement();
                ResultSet show_tables = statement.executeQuery("show tables");
                printRows("Connect-" + i + ">", show_tables);
                connPool.returnObject(connection);
            }
        }
    }


    @Test
    @DisplayName("验证开始jmx管理")
    public void testPoolJmx() throws Exception {
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/test";
        String user = "root";
        String pass = "123456";
        PooledConnectFactory pooledConnectFactory = new PooledConnectFactory(dbUrl, user, pass);

        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<Connection>();
        config.setMaxTotal(5);
        config.setMaxIdle(2);
        config.setMinIdle(2);
        // 5秒钟没有使用,就移除掉
        config.setTimeBetweenEvictionRuns(Duration.ofSeconds(5));
        config.setMinEvictableIdleTime(Duration.ofSeconds(5));
        // 阻塞后异常
        config.setBlockWhenExhausted(true);
        config.setMaxWait(Duration.ofSeconds(5));
        config.setJmxEnabled(true);
        config.setJmxNameBase("ConnectJmxNameBase");
        // 生成数据库连接池
        // 连接池配置最大5个连接setMaxTotal(5),但是获取6次,那么有一次获取不到就会阻塞setBlockWhenExhausted(true),
        // 当等待了10秒setMaxWait(Duration.ofSeconds(10))还是获取不到。就直接报错
        try (GenericObjectPool<Connection> connPool = new GenericObjectPool<>(pooledConnectFactory, config)) {
            for (int i = 1; i <= 7; i++) {
                Connection connection = connPool.borrowObject();
                Statement statement = connection.createStatement();
                ResultSet show_tables = statement.executeQuery("show tables");
                printRows("Connect-" + i + ">", show_tables);
            }
        }
    }

    /**
     * 连接池配置最大5个连接setMaxTotal(5),但是获取6次,那么有一次获取不到就会阻塞setBlockWhenExhausted(true),
     * 当等待了10秒setMaxWait(Duration.ofSeconds(10))还是获取不到。就直接报错
     *
     * @throws Exception 链接异常
     */
    @Test
    @DisplayName("验证不进行回收,阻塞并报错的场景配置")
    public void test() throws Exception {
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/test";
        String user = "root";
        String pass = "123456";
        PooledConnectFactory pooledConnectFactory = new PooledConnectFactory(dbUrl, user, pass);

        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<Connection>();
        config.setMaxTotal(5);
        config.setMaxIdle(2);
        config.setMinIdle(2);
        // 5秒钟没有使用,就移除掉
        config.setTimeBetweenEvictionRuns(Duration.ofSeconds(5));
        config.setMinEvictableIdleTime(Duration.ofSeconds(5));
        config.setBlockWhenExhausted(true);
        // 最大等待10秒
        config.setMaxWait(Duration.ofSeconds(10));
        // 生成数据库连接池
        // 连接池配置最大5个连接setMaxTotal(5),但是获取6次,那么有一次获取不到就会阻塞setBlockWhenExhausted(true),
        // 当等待了10秒setMaxWait(Duration.ofSeconds(10))还是获取不到。就直接报错
        try (GenericObjectPool<Connection> connPool = new GenericObjectPool<>(pooledConnectFactory, config)) {
            for (int i = 1; i <= 6; i++) {
                Connection connection = connPool.borrowObject();
                Statement statement = connection.createStatement();
                ResultSet show_tables = statement.executeQuery("show tables");
                printRows("Connect-" + i + ">", show_tables);
            }
        }
    }


    private void printRows(String conn, ResultSet resultSet) throws Exception {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        // 所有的列名
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            // 这里使用别名,如果没有别名的情况,别名跟字段名是一样的。
            columnNames.add(metaData.getColumnLabel(i));
        }
        while (resultSet.next()) {
            for (String columnName : columnNames) {
                System.out.println(ColorConsole.color(conn + "列:" + columnName + ":value:" + resultSet.getObject(columnName)));
            }
        }
        resultSet.close();
    }
}