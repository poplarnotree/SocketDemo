package org.socket.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 启动服务器
 */
public class StartServer {
    public static void main(String[] args) {
        // 创建服务器Socket,绑定指定端口
        try {
            ServerSocket serverSocket = new ServerSocket(8800);
            Socket socket = null;
            int count = 0;
            System.out.println("服务器即将启动，等待客户端连接*******");
            while (true){ //服务器循环监听客户端的连接请求
                // 开始监听，等待客户端连接
                socket = serverSocket.accept();
                //多线程通信
                ServerThread thread = new ServerThread(socket);
                thread.start();
                count++;//统计客户端的数量
                System.out.println("客户端的数量:"+count);
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP："+address.getHostAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
