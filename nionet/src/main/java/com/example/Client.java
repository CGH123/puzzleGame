package com.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.net.InetAddress;

/**
 * TCP/IP的NIO非阻塞方式 
 * 客户端 
 * */
public class Client {

    Selector selector;
    SelectionKey sk;
    //创建缓冲区  
    private ByteBuffer buffer = ByteBuffer.allocate(512);
    //访问服务器  

    private void init(SocketChannel socketChannel) throws ClosedChannelException{
        sk = socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public void query(String host,int port) throws IOException{
        InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(host),port);
        InetSocketAddress address2 = new InetSocketAddress(InetAddress.getByName(host),9099);
        SocketChannel socket = null;
        SocketChannel socket2 = null;
        byte[] bytes = new byte[512];
        bytes = new String("hello").getBytes("UTF-8");
//        while(true){  
        try{
//            	System.out.println("i begin to run ");
//                System.in.read(bytes);  
            socket = SocketChannel.open();
            socket.connect(address);
            init(socket);

//                while(true){
            Thread.sleep(2000);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            socket.write(buffer);
            System.out.println(buffer.toString());
//                }


//                buffer.clear();  

//                System.out.println("i start to run ");
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(socket!=null){
                socket.close();
//                    socket2.close();  
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
//    }  

    public static void main(String[] args) throws IOException{
        Server server = new Server();
        Thread thread = new Thread(server);
        thread.start();

        new Client().query("localhost", 8099);

    }
}  