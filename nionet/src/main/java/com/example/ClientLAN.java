package com.example;


import com.example.nioFrame.NIOService;
import com.example.nioFrame.NIOSocket;
import com.example.nioFrame.SocketObserver;
import com.example.nioFrame.UDPSocket;
import com.example.protocol.MSGProtocol;
import com.example.serialization.SerializerFastJson;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * TCP连接的客户端
 * Created by HUI on 2016-04-20.
 */
public class ClientLAN implements Runnable, Client, SocketObserver {
    private final static String TAG = "mClient";
    private NIOService service;
    private NIOSocket socket;
    private SocketObserver socketObserver;
    private UDPSocket udpSocket;
    private String host;
    private int port;
    private List<OnClientReadListener> clientReadListeners; //设置监听器的回调函数
    private Map<String, Object> clientDataMap;
    private boolean isRunning;
    private volatile boolean isFindServer;
    private String serverIp;

    private ClientLAN() {
        udpSocket = UDPSocket.getInstance();
        clientReadListeners = new LinkedList<>();
        clientDataMap = new TreeMap<>();
        socketObserver = this;
        isRunning = false;
        isFindServer = false;
        serverIp = "";
    }

    public static Client getInstance() {
        return SingletonHolder.client;
    }

    public static void main(String args[]) {
        final Client client = ClientLAN.getInstance();
        OnClientReadListener clientReadListener = new OnClientReadListener() {
            @Override
            public void processMsg(byte[] packet) {
                System.out.println(new String(packet));
            }
        };

        client.addClientReadListener(clientReadListener)
                .bind("localhost", NetConstant.TCP_PORT)
                .start();

        for (int i = 0; i < 100; i++) {
            MSGProtocol msgProtocol = new MSGProtocol("hello server", i);
            String msgString = SerializerFastJson.getInstance().serialize(msgProtocol);
            client.sendToServer(msgString.getBytes());
        }

    }


    public Client setSocketObserver(SocketObserver mSocketObserver) {
        this.socketObserver = mSocketObserver;
        return getInstance();
    }

    public Client addClientReadListener(OnClientReadListener mlistener) {
        this.clientReadListeners.add(mlistener);
        return getInstance();
    }

    public Client removeClientReadListener(OnClientReadListener mlistener) {
        this.clientReadListeners.remove(mlistener);
        return getInstance();
    }

    /**
     * 绑定线程
     */
    public Client bind(int port) {
        return bind(null, port);
    }

    /**
     * 绑定线程
     */
    public Client bind(String host, int port) {
        this.host = host;
        this.port = port;
        initClientObserver();
        return getInstance();
    }


    /**
     * 开启线程
     */
    public Client start() {
        isRunning = true;
        SingletonHolder.workThread.start();
        return getInstance();
    }

    @Override
    public Client startUdp(int port) {
        udpSocket.bind(port).start();
        return getInstance();
    }

    @Override
    public Client stopUdp() {
        udpSocket.stop();
        udpSocket.close();
        return getInstance();
    }

    @Override
    public String findServerIP(int timeout) {
        udpSocket.setUdpReadListener(new UDPSocket.OnUdpReadListener() {
            @Override
            public void processMsg(String hostName, byte[] packet) {
                if (new String(packet).equals(NetConstant.RETURN_HOST)) {
                    isFindServer = true;
                    serverIp = hostName;
                    udpSocket.stop();
                    udpSocket.close();
                }
            }
        });
        int time = 0;
        int sleepTime = 300;
        while (time < timeout && !isFindServer) {
            try {
                byte[] data = "find_server".getBytes();
                udpSocket.send(NetConstant.BROADCAST_HOST, data);
                Thread.sleep(sleepTime);
                time += sleepTime;
            } catch (InterruptedException e) {
                udpSocket.stop();
                udpSocket.close();
                System.out.print(TAG + e.toString());
            }
        }
        return serverIp;
    }

    /**
     * 停止线程
     */
    public Client stop() {
        isRunning = false;
        return getInstance();
    }

    /**
     * 关闭线程
     */
    public Client close() {
        service.close();
        socket.close();
        return getInstance();
    }

    /**
     * 发送数据给服务器
     */
    public Client sendToServer(byte[] content) {
        write(content);
        return getInstance();
    }
  
    /**
     * 需要序列化功能实现,建议在回调函数Listener中使用
     * 由于客户端只有一个管道，所以不用socketChannel的参数
     */
    private void write(byte[] content) {
        if (socket == null || !isRunning) {
            System.out.println(TAG + "write socket wrong");
            stop();
            close();
            throw new NullPointerException();
        }
        //往通道写入数据
        socket.write(content);
    }


    public void putData(String key, Object o) {
        if (key.isEmpty()) {
            stop();
            close();
            throw new NullPointerException();
        }
        clientDataMap.put(key, o);
    }

    public Object putData(String key) {
        if (key.isEmpty()) {
            stop();
            close();
            throw new NullPointerException();
        }
        return clientDataMap.get(key);
    }


    @Override
    public void run() {
        while (isRunning) {
            try {
                service.selectBlocking();
            } catch (IOException e) {
                System.out.println(TAG + "mService wrong");
                this.stop();
                this.close();
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化客户端的连接要用的所有数据
     */
    private void initClientObserver() {
        try {
            service = new NIOService();
            if (host == null)
                socket = service.openSocket(port);
            else
                socket = service.openSocket(host, port);
            socket.listen(socketObserver);
        } catch (IOException e) {
            this.stop();
            this.close();
            e.getStackTrace();
        }
    }

    public void connectionOpened(NIOSocket nioSocket) {
        System.out.println("login success");
    }

    public void packetSent(NIOSocket socket, Object tag) {
        System.out.println("Packet sent success");
    }

    public void packetReceived(NIOSocket socket, byte[] packet) {
        //处理返回得到的数据,用观察者(监听器)模式来处理
        if (!clientReadListeners.isEmpty())
            for (OnClientReadListener clientReadListener : clientReadListeners)
                clientReadListener.processMsg(packet);
        else
            System.out.println(TAG + "Client listener is empty");
    }

    public void connectionBroken(NIOSocket nioSocket, Exception exception) {
        System.out.println("Connection failed.");
    }

    /**
     * 单例模型
     */
    private static class SingletonHolder {
        private static ClientLAN client = new ClientLAN();
        private static Thread workThread = new Thread(client);
    }


}
