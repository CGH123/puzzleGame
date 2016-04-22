package com.example;

import com.example.nioFrame.NIOService;
import com.example.nioFrame.NIOSocket;
import com.example.nioFrame.PacketRW.RawPacketReader;
import com.example.nioFrame.PacketRW.RawPacketWriter;
import com.example.nioFrame.SocketObserver;

import java.io.IOException;


/**
 * TCP连接的客户端
 * 用户必须自己实现OnMsglistener
 * Created by HUI on 2016-04-20.
 */
public class TCPClient implements Runnable {
    private static String TAG = "TCPClient";

    private static TCPClient instance;
    private NIOService mService;
    private NIOSocket mSocket;
    private int PORT = NetConstant.TCP_PORT;
    private OnMsgRecListener mlistener; //设置监听器的回调函数
    private boolean isRunning = true;



    TCPClient(){
        mlistener = null;
        init_connect();
    }

    TCPClient(OnMsgRecListener listener){
        mlistener = listener;
        init_connect();
    }


    /**
     *  设置OnMsgReclistener
     */
    public void setMlistener(OnMsgRecListener mlistener) {
        this.mlistener = mlistener;
    }



    /**
     * 单例模型
     */
    public static TCPClient getInstance(){
        if (instance == null) {
            instance = new TCPClient();
        }
        return instance;
    }

    /**
     * 需要序列化功能实现,建议在回调函数Listener中使用
     * 由于客户端只有一个管道，所以不用socketChannel的参数
     */
    public void write(byte[] content) {
        if(mSocket == null){
            LogUtil.d(TAG,"mSocket is null");
            return;
        }
        //往通道写入数据
        mSocket.write(content);
    }

    /**
     *待定，用观察者模式来处理
     */
    public byte[] read(){
        //暂时不用，由监听器来处理
        return null;
    }


    /**
     * 开启线程
     */
    public void start(){
        isRunning = true;
        new Thread(TCPClient.getInstance()).start();
    }

    /**
     * 关闭线程
     */
    public void stop(){
        mService.close();
        mSocket.close();
        isRunning =false;
    }

    @Override
    public void run(){
        while(isRunning){
            try {
                mService.selectBlocking();
            } catch (IOException e) {
                LogUtil.d(TAG,"mService wrong");
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化客户端的连接是要用的所有数据
     */
    public void init_connect() {

        try {
            mService = new NIOService();
            mSocket = mService.openSocket(PORT);

            mSocket.setPacketReader(new RawPacketReader());
            mSocket.setPacketWriter(new RawPacketWriter());

            mSocket.listen(new SocketObserver() {
                public void connectionOpened(NIOSocket nioSocket) {
                    System.out.println("login success");
                }

                public void packetSent(NIOSocket socket, Object tag) {
                    System.out.println("Packet sent success");
                }

                public void packetReceived(NIOSocket socket, byte[] packet) {
                    try {
                       //处理返回得到的数据,用观察者(监听器)模式来处理
                        if(mlistener!=null)
                            mlistener.processMSG(packet);
                        else
                            LogUtil.d(TAG,"Client listener is null");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void connectionBroken(NIOSocket nioSocket,
                                             Exception exception) {
                    System.out.println("Connection failed.");
                }

            });
        }catch (IOException e){
            e.getStackTrace();
        }
    }
}
