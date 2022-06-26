package learn.network;

import learn.common.print.ColorConsole;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author liuxin
 * 2022/5/8 20:56
 */
public class SocketClient {

    @SneakyThrows
    public String connect(String ip, Integer port) {
        ColorConsole.colorPrintln("开始连接远程服务端");
        Socket socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String message = dataInputStream.readUTF();
        socket.close();
        return message;
    }
}
