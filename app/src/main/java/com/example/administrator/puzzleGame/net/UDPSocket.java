package com.example.administrator.puzzleGame.net;

import com.example.administrator.puzzleGame.util.LogUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 * 用于UDP进行泛洪寻找玩家
 * Created by HUI on 2016-04-16.
 */
public class UDPSocket implements Runnable {
    private final String BROADCASTIP = "255.255.255.255";
    private static UDPSocket instance;
    private String TAG = "UDPSocket";
    private DatagramSocket UDPSocket;
    public static boolean STATE = false;
    public static boolean Thread_Flag = true;

    UDPSocket() {
        try {
            UDPSocket = new DatagramSocket(NetConstant.UDP_PORT);
        } catch (SocketException e) {
            LogUtil.d(TAG, "UDPSocket new failed");
        }

    }

    public static UDPSocket getInstance() {
        if (instance == null) {
            instance = new UDPSocket();
        }
        return instance;
    }

    @Override
    public void run() {
        while (Thread_Flag) {
            try {
                byte data[] = new byte[1024];
                DatagramPacket dataPacket = new DatagramPacket(data, data.length);
                UDPSocket.receive(dataPacket);
                String result = new String(dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());
                LogUtil.d(TAG, result);
                if (result.equals("hello"))
                    STATE = true;
                Thread.sleep(500);
            } catch (Exception e) {
                LogUtil.d(TAG, e.toString());
            }
        }
    }

    public void sendBroad() {
        new Thread(new sendBroadcast()).start();
    }

    protected class sendBroadcast implements Runnable {

        private boolean SEND_Flag = true;

        @Override
        public void run() {
            if (SEND_Flag) {
                try {
                    InetAddress broadcastAddr = InetAddress.getByName(BROADCASTIP);
                    byte[] data = new String("hello").getBytes();
                    DatagramPacket dataPacket = new DatagramPacket(data, data.length, broadcastAddr, NetConstant.UDP_PORT);
                    UDPSocket.send(dataPacket);
                } catch (IOException e) {
                    LogUtil.d(TAG, e.toString());
                }
            }
        }
    }
}
