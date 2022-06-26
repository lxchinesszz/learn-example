package learn.network;

import learn.common.print.ColorConsole;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liuxin
 * 2022/5/8 20:54
 */
class SocketServerTest {

    @Test
    public void startServer(){
        SocketServer socketServer = new SocketServer(10086);
        socketServer.start();
    }

    @Test
    @SneakyThrows
    void start() {
        String message = new SocketClient().connect("localhost", 10086);
        ColorConsole.colorPrintln("收到服务端响应:{}", message);
    }
}