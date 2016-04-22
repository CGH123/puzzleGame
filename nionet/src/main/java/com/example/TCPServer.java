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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * TCP连接的服务端
 * 用户必须自定义OnMsgRecListener
 * Created by HUI on 2016-04-22.
 */
public class TCPServer implements Runnable, Server, ServerSocketObserver {
    private final static String TAG = "TCPServer";
    private NIOService service;
    private NIOServerSocket serverSocket;
    private ServerSocketObserver serverSocketObserver;
    private List<OnServerReadListener> serverReadListeners; //设置监听器的回调函数
    private Map<String, Object> serverDataMap;
    private List<NIOSocket> socketList;
    private boolean isRunning;
    private int port;

    private TCPServer() {
        serverReadListeners = new LinkedList<>();
        serverDataMap = new TreeMap<>();
        socketList = new Vector<>();
        serverSocketObserver = this;
        isRunning = false;
    }

    /**
     * 单例模型
     */
    private static class SingletonHolder {
        private static TCPServer server = new TCPServer();
        private static Thread workThread = new Thread(server);
    }

    public static Server getInstance() {
        return SingletonHolder.server;
    }

    public Server setServerSocketObserver(ServerSocketObserver serverSocketObserver) {
        this.serverSocketObserver = serverSocketObserver;
        return getInstance();
    }


    public Server addServerReadListener(OnServerReadListener mlistener) {
        this.serverReadListeners.add(mlistener);
        return getInstance();
    }

    public Server removeServerReadListener(OnServerReadListener mlistener) {
        this.serverReadListeners.remove(mlistener);
        return getInstance();
    }

    public synchronized void putData(String key, Object o) {
        if (key.isEmpty())
            throw new NullPointerException();
        serverDataMap.put(key, o);
    }

    public synchronized Object putData(String key) {
        if (key.isEmpty())
            throw new NullPointerException();
        return serverDataMap.get(key);
    }

    /**
     * 绑定线程
     */
    public Server bind(int port) {
        this.port = port;
        initServerObserver();
        return getInstance();
    }


    /**
     * 开启线程
     */
    public Server start() {
        isRunning = true;
        SingletonHolder.workThread.start();
        return getInstance();
    }

    /**
     * 停止线程
     */
    public Server stop() {
        isRunning = false;
        return getInstance();
    }

    /**
     * 关闭线程
     */
    public Server close() {
        service.close();
        serverSocket.close();
        return getInstance();
    }

    /**
     * 关闭线程
     */
    public Server sendToClient(byte[] content, NIOSocket socket) {
        write(content, socket);
        return getInstance();
    }

    /**
     * 关闭线程
     */
    public Server sendExceptClient(byte[] content, NIOSocket socket) {
        for(NIOSocket nioSocket : socketList)
            if(nioSocket != socket)
                write(content, nioSocket);
        return getInstance();
    }

    /**
     * 关闭线程
     */
    public Server sendAllClient(byte[] content) {
        for(NIOSocket nioSocket : socketList)
            write(content, nioSocket);
        return getInstance();
    }


    /**
     * 需要序列化功能实现,建议在回调函数Listener中使用
     * 向指定的客户端管道写信息
     */
    private void write(byte[] content, NIOSocket socket) {
        if (socket == null || socket.isOpen() || service.isOpen() || !isRunning) {
            System.out.print(TAG + "mSocket is null");
            stop();
            close();
            throw new NullPointerException();
        }
        //往通道写入数据
        socket.write(content);
    }


    /**
     * 初始化ServerSocketChannel，监听和回调的内容
     */
    private void initServerObserver() {
        try {
            service = new NIOService();
            serverSocket = service.openServerSocket(port);
            serverSocket.listen(serverSocketObserver);
            serverSocket.setConnectionAcceptor(ConnectionAcceptor.ALLOW);
        } catch (IOException e) {
            stop();
            close();
            System.out.print(TAG + "ServerSocket is wrong");
            e.getStackTrace();
        }
    }


    @Override
    public void run() {
        while (isRunning) {
            try {
                service.selectBlocking();
            } catch (IOException e) {
                this.stop();
                this.close();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acceptFailed(IOException exception) {
        //待定
    }

    @Override
    public void serverSocketDied(Exception exception) {
        //待定
    }

    @Override
    public void newConnection(NIOSocket nioSocket) {
        System.out.print(TAG + "Received connection: " + nioSocket);
        socketList.add(nioSocket);

        nioSocket.setPacketReader(new RawPacketReader());
        nioSocket.setPacketWriter(new RawPacketWriter());

        nioSocket.listen(new SocketObserver() {
            @Override
            public void connectionOpened(NIOSocket nioSocket) {
                //待定
            }

            @Override
            public void connectionBroken(NIOSocket nioSocket, Exception exception) {
                //待定
            }

            public void packetReceived(NIOSocket socket, byte[] packet) {
                //把其中一个客户端发送的信息，在服务端更新了之后，传递给其他每一个的客户端
                for (NIOSocket e : socketList) {
                    if (e != socket) {
                        if (!serverReadListeners.isEmpty())
                            for (OnServerReadListener serverReadListener : serverReadListeners)
                                serverReadListener.processMSG(packet, socket);
                        else
                            System.out.print(TAG + "Server listener is null");
                    }
                }
            }

            @Override
            public void packetSent(NIOSocket socket, Object tag) {
                //待定
            }
        });
    }
}
