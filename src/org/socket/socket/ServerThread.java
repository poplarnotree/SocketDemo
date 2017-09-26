package org.socket.socket;

import org.socket.entity.TbFile;
import org.socket.entity.TbUser;
import org.socket.service.FileService;
import org.socket.service.UserService;
import org.socket.util.CommandTransfer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 服务器端线程处理类
 */
public class ServerThread extends  Thread {
    private Socket socket = null;
    private ObjectInputStream ois = null; //对象输入流
    private ObjectOutputStream oos = null; //对象输出流
    private UserService us = new UserService(); //用户业务对象
    private FileService fs = new FileService(); //文件业务对象

    // 通过构造方法，初始化socket
    public ServerThread(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            CommandTransfer transfer = (CommandTransfer) ois.readObject(); //读取输出流数据
            transfer = execute(transfer); //执行客户端发送到服务器的指令操作
            oos.writeObject(transfer); //响应客户端
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行客户端发送到服务器的指令操作
     */
    public CommandTransfer execute(CommandTransfer transfer){
        String cmd = transfer.getCmd(); // 获取当前操作的指令
        if (cmd.equals("login")){ //用户登录
            TbUser user = (TbUser)transfer.getData();
            boolean flag = us.login(user);
            transfer.setFlag(flag);
            if (flag){ //判断是否登录成功
                transfer.setResult("登录成功");
            }else {
                transfer.setResult("用户名或密码不正确，请重新登录！");
            }
        }else if (cmd.equals("register")){ //用户注册
            TbUser user = (TbUser) transfer.getData();
            int count = us.register(user);
            if (count == 1){
                transfer.setResult("注册成功,请登录！");
                transfer.setFlag(true);
            }else if (count == 0){
                transfer.setResult("注册失败，账号已存在");
                transfer.setFlag(false);
            }else {
                transfer.setResult("注册失败，系统错误");
                transfer.setFlag(false);
            }
        }else if(cmd.equals("unloadFile")){
            TbFile file = (TbFile) transfer.getData();
            int count = fs.save(file);
            if (count == 1){
                transfer.setResult("上传成功");
                transfer.setFlag(true);
            }else{
                transfer.setResult("上传失败");
                transfer.setFlag(false);
            }
        }
        return transfer;
    }
}
