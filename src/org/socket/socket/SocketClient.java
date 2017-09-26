package org.socket.socket;

import org.socket.entity.TbFile;
import org.socket.entity.TbUser;
import org.socket.util.CommandTransfer;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {
    Scanner input = new Scanner(System.in);
    private Socket socket = null;

    // 主菜单
    public void showMainMenu(){
        System.out.println("*****欢迎使用文件上传器****");
        System.out.println("1.登录\n2.注册\n3.退出");
        System.out.println("***************************");
        System.out.println("请选择:");
        int choice = input.nextInt(); // 获取用户的选择
        switch (choice){
            case 1:
                showLogin(); //登录
                break;
            case 2:
                showRegister(); //注册
                break;
            case 3:
                System.out.println("再见，感谢使用！");
                System.exit(0);
            default:
                System.out.println("输入错误！请重新输入:");
                choice = input.nextInt(); // 获取用户的选择
        }
    }

    public void showLoginMenu(){
        System.out.println("1.上传文件\n2.退出");
        System.out.println("***************************");
        System.out.println("请选择:");
        int choice = input.nextInt(); // 获取用户的选择
        switch (choice){
            case 1:
                showUploadFile();
                break;
            case 2:
                System.out.println("再见，感谢使用！");
                System.exit(0);
            default:
                System.out.println("输入错误！请重新输入:");
                choice = input.nextInt(); // 获取用户的选择
        }
    }

    private void showLogin(){
        TbUser user = new TbUser();
        CommandTransfer transfer = new CommandTransfer();
        int count = 0; //登录次数
        while (true){
            count++;
            if (count > 3){
                System.out.println("您已连续三次登录失败，程序退出");
                System.out.println(0);
            }
            System.out.println("请输入用户名:");
            user.setUsername(input.next());
            System.out.println("请输入密码:");
            user.setPassword(input.next());
            transfer.setCmd("login");
            transfer.setData(user);

            try {
                socket = new Socket("127.0.0.1",8800);
                sendData(transfer);
                transfer = getData();
                System.out.println(transfer.getResult());
                System.out.println("**************");
                if (transfer.isFlag()) {
                    new SocketClient().showLoginMenu();
                    break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void showRegister(){
        TbUser user = new TbUser();
        CommandTransfer transfer = new CommandTransfer();
        while (true){
            System.out.println("请输入用户名:");
            user.setUsername(input.next());
            System.out.println("请输入密码:");
            user.setPassword(input.next());
            System.out.println("请再次输入密码:");
            String rePassword = input.next();
            if(!user.getPassword().equals(rePassword)){
                System.out.println("两次输入的密码不一致！");
                System.out.println("********************");
                continue;
            }
            transfer.setCmd("register");
            transfer.setData(user);

            try {
                socket = new Socket("127.0.0.1",8800);
                sendData(transfer);
                transfer = getData();
                System.out.println(transfer.getResult());
                System.out.println("***********");
                if (transfer.isFlag()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void showUploadFile(){
        System.out.println("请输入上传文件的绝对路径(如e:/file.xxx):");
        String path = input.next();
        TbFile file = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        String fname = path.substring(path.lastIndexOf("/")+1);
        try{
            fis = new FileInputStream(path);
            byte[] fcontent = new byte[fis.available()];
            bis = new BufferedInputStream(fis);
            bis.read(fcontent);
            file = new TbFile(fname, fcontent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        CommandTransfer transfer = new CommandTransfer();
        transfer.setCmd("unloadFile");
        transfer.setData(file);
        try {
            socket = new Socket("127.0.0.1",8800);
            sendData(transfer);
            transfer = getData();
            System.out.println(transfer.getResult());
        } catch (UnknownHostException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendData(CommandTransfer transfer){
        try {
            new ObjectOutputStream(socket.getOutputStream()).writeObject(transfer);
            new ObjectOutputStream(socket.getOutputStream()).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CommandTransfer getData(){
        CommandTransfer transfer = new CommandTransfer();

        try {
            transfer =(CommandTransfer) new ObjectInputStream(socket.getInputStream()).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return transfer;
    }
    public void closeAll(){
        try {
            new ObjectOutputStream(socket.getOutputStream()).close();
            new ObjectInputStream(socket.getInputStream()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
