package com.example.nioFrame;

import com.example.NetConstant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 用于UDP进行泛洪寻找玩家
 * Created by HUI on 2016-04-16.
 */
public class UDPSocket implements Runnable {
    private static boolean isRunning;
    private final String TAG = "UDPSocket";
    private DatagramSocket DataSocket;
    private OnUdpReadListener udpReadListener;
    private UDPSocket() {
        isRunning = false;
    }

    public static UDPSocket getInstance() {
        return SingletonHolder.udpSocket;
    }

    public UDPSocket setUdpReadListener(OnUdpReadListener udpReadListener) {
        this.udpReadListener = udpReadListener;
        return getInstance();
    }

    public UDPSocket bind(int port) {
        try {
            DataSocket = new DatagramSocket(NetConstant.UDP_PORT);
        } catch (SocketException e) {
            System.out.print(TAG + "UDPSocket new failed");
        }
        return getInstance();
    }

    public UDPSocket start() {
        SingletonHolder.workThread.start();
        return getInstance();
    }

    public UDPSocket stop() {
        isRunning = false;
        return getInstance();

    }

    public UDPSocket close() {
        DataSocket.close();
        return getInstance();
    }

    public UDPSocket send(String host, byte[] data) {
        InetAddress address;
        try {
            address = InetAddress.getByName(host);
            DatagramPacket dataPacket = new DatagramPacket(data, data.length, address, NetConstant.UDP_PORT);
            DataSocket.send(dataPacket);
        } catch (UnknownHostException e) {
            stop();
            close();
            e.printStackTrace();
        } catch (IOException e) {
            stop();
            close();
            e.printStackTrace();
        }
        return getInstance();
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                byte data[] = new byte[1024];
                DatagramPacket dataPacket = new DatagramPacket(data, data.length);
                DataSocket.receive(dataPacket);
                String result = new String(dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());
                System.out.print(TAG + result);
                udpReadListener.processMsg(DataSocket.getInetAddress().getHostAddress(), data);
            } catch (Exception e) {
                stop();
                close();
                System.out.print(TAG + e.toString());
            }
        }
    }

    public interface OnUdpReadListener {
        void processMsg(String hostName, byte[] packet);
    }

    /**
     * 单例模型
     */
    private static class SingletonHolder {
        private static UDPSocket udpSocket = new UDPSocket();
        private static Thread workThread = new Thread(udpSocket);
    }

}
