package com.example;

import com.example.nioFrame.ConnectionAcceptor;
import com.example.nioFrame.NIOServerSocket;
import com.example.nioFrame.NIOService;
import com.example.nioFrame.NIOSocket;
import com.example.nioFrame.PacketRW.RawPacketReader;
import com.example.nioFrame.PacketRW.RawPacketWriter;
import com.example.nioFrame.ServerSocketObserver;
import com.example.nioFrame.SocketObserver;


import java.io.IOException;
import java.util.Vector;

/**
 * TCP连接的服务端
 * 用户必须自定义OnMsgRecListener
 * Created by HUI on 2016-04-22.
 */
public class TCPServer implements Runnable{

    private static String TAG = "TCPServer";

    private static TCPServer instance;
    private NIOService mService;
    private NIOServerSocket mServerSocket;
    private int PORT = NetConstant.TCP_PORT;
    private OnMsgRecListener mlistener;

    private boolean isRunning = true;

    /**
     * 所有客户端的NIOSocket的集合
     */
    private Vector<NIOSocket> mSocketList;

    TCPServer(){
        isRunning = true;
        mSocketList = new Vector<NIOSocket>();
        mlistener = null;
        init_connect();
    }

    TCPServer(OnMsgRecListener listener){
        isRunning = true;
        mSocketList = new Vector<NIOSocket>();
        mlistener = listener;
        init_connect();
    }

    /**
     * 单例模式
     * 返回TCPServer
     */
    public static TCPServer getIntance(){
        if(instance == null){
            instance = new TCPServer();
        }
        return instance;
    }


    /**
     * 设置Listener
     */
    public void setMlistener(OnMsgRecListener mlistener) {
        this.mlistener = mlistener;
    }

    /**
     * 获取所有客户端的socket
     */
    public Vector<NIOSocket> getmSocketList() {
        return mSocketList;
    }

    /**
     * 需要序列化功能实现,建议在回调函数Listener中使用
     * 向指定的客户端管道写信息
     */
    public void write(byte[] content,NIOSocket socket) {
        if(socket == null){
            LogUtil.d(TAG,"mSocket is null");
            return;
        }
        //往通道写入数据
        socket.write(content);
    }


    /**
     * 初始化ServerSocketChannel，监听和回调的内容
     */
    private void init_connect() {
        try
        {
            mService = new NIOService();
            mServerSocket= mService.openServerSocket(PORT);
            mServerSocket.listen(new ServerSocketObserver()
            {
                @Override
                public void acceptFailed(IOException exception) {
                    //待定
                }

                @Override
                public void serverSocketDied(Exception exception) {
                    //待定
                }

                public void newConnection(NIOSocket nioSocket)
                {
                    LogUtil.d(TAG, "Received connection: " + nioSocket);
                    //利用Naga框架的监听器回调，保留通信的NIOSocket
                    if(!mSocketList.contains(nioSocket))
                        mSocketList.add(nioSocket);


                    nioSocket.setPacketReader(new RawPacketReader());
                    nioSocket.setPacketWriter(new RawPacketWriter());

                    nioSocket.listen(new SocketObserver()
                    {
                        @Override
                        public void connectionOpened(NIOSocket nioSocket) {
                            //待定
                        }

                        @Override
                        public void connectionBroken(NIOSocket nioSocket, Exception exception) {
                            //待定
                        }

                        public void packetReceived(NIOSocket socket, byte[] packet) {
                            //回应一下当前发送过来的客户端
                            //TO-DO

                            //把其中一个客户端发送的信息，在服务端更新了之后，传递给其他每一个的客户端
                            for(NIOSocket e:mSocketList){
                                if(e!=socket){
                                    if(mlistener!=null)
                                        mlistener.processMSG(packet);
                                    else
                                        LogUtil.d(TAG,"Server listener is null");
                                }
                            }
                        }

                        @Override
                        public void packetSent(NIOSocket socket, Object tag) {
                            //待定
                        }
                    });
                }
            });

            mServerSocket.setConnectionAcceptor(ConnectionAcceptor.ALLOW);
        } catch (IOException e){
            LogUtil.d(TAG,"ServerSocket is wrong");
            e.getStackTrace();
        }
    }

    /**
     * 开启线程
     */
    public void start(){
        isRunning = true;
        new Thread(getIntance()).start();
    }

    /**
     * 关闭线程
     */
    public void stop(){
        isRunning = false;
        mService.close();
        mServerSocket.close();
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                mService.selectBlocking();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
