package learn.network;

import learn.common.print.ColorConsole;
import lombok.SneakyThrows;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author liuxin
 * 2022/5/8 20:47
 */
public class SocketServer {

    private Integer port;

    public SocketServer(Integer port) {
        this.port = port;
    }

    @SneakyThrows
    public void start() {
        ServerSocket serverSocket = new ServerSocket(this.port);
        int acceptCount = 1;
        while (true) {
            ColorConsole.colorPrintln("等待远程客户端连接");
            Socket clientSocket = serverSocket.accept();
            ColorConsole.colorPrintln("第:{}个客户端ip:{},连接成功", acceptCount, clientSocket.getRemoteSocketAddress());
            // 获取输出流，给客户端发送信息
            OutputStream outputStream = clientSocket.getOutputStream();
            DataOutputStream os = new DataOutputStream(outputStream);
            os.writeUTF("连接成功");
            // 关闭客户端连接
            clientSocket.close();
            ColorConsole.colorPrintln("远程客户端连接关闭");
            acceptCount++;
        }
    }
}
