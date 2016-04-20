package com.example.nionet_test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *用于生成申请的NIOClient
 *
 * 由于NIO适合连接数多且为短连接，轻数据量的服务器，比如通讯服务器，所以client实现像Http一样的短连接
 * （初步设计为短连接,用每隔一段时间访问一次服务器）
 * Created by HUI on 2016-04-19.
 */
public class NIOClient implements Runnable{
    private final String TAG = "NIOClient";
    private ByteBuffer buffer;
    private SocketChannel socketChannel;
    final int PORT;                   //端口链接

    private boolean isConnect = true; //是否需要保持和服务器长久的链接


    NIOClient(int port) throws IOException{
        PORT = port;
        buffer = ByteBuffer.allocate(1024);
    }

    /**
     * 向服务器发送数据
     */
    private void sentPacket(byte[] content){
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(PORT));
            buffer.clear();
            buffer.put(content);
            buffer.flip();
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * 服务器的连接请求
     */
    public int NIO_post(){
        try {
            //发送给一个试探包
            byte[] probe = BufferUtils.encodeByte("updata");
            sentPacket(probe);

        } catch (IOException e) {
            //LogUtil.d(TAG, "短连接申请问题异常");
            e.printStackTrace();
        }finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                //LogUtil.d(TAG, "channel关闭异常");
                e.printStackTrace();
            }
        }

        return 0;
    }

    /**
     * 有消息需要传送到服务区
     */
    public void packetSend(byte[] packet){
        if(packet == null) return;

    }

    public void packetReceive(byte[] packet){
        if(packet == null) return;

    }

    /**
     * 计时器的轮询，因为NIO实现为短连接，所以保持每次一段时间进行访问
     */
    @Override
    public void run() {
        while(isConnect){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //LogUtil.d(TAG, "短连接心跳问题异常");
                e.printStackTrace();
            }

            //获取服务器的更新信息

        }
    }
}
