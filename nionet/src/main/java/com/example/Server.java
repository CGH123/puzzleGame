package com.example;

import com.example.nioFrame.NIOSocket;
import com.example.nioFrame.ServerSocketObserver;


public interface Server {
    /**
     * 绑定线程
     * @param port 端口
     * @return 服务器instance
     */
    Server bind(int port);


    /**
     * 开启线程
     * @return 服务器instance
     */
    Server start();

    /**
     * 停止线程
     * @return 服务器instance
     */
    Server stop();

    /**
     * 开启线程
     * @return 服务器instance
     */
    Server startUdp(int port);

    /**
     * 停止线程
     * @return 服务器instance
     */
    Server stopUdp();

    /**
     * 关闭线程
     * @return 服务器instance
     */
    Server close();

    /**
     * 发送数据给服务器
     * @param content 数据
     * @param nioSocket 客户端socket
     * @return 服务器instance
     */
    Server sendToClient(byte[] content, NIOSocket nioSocket);

    /**
     * 发送数据给服务器
     * @param content 数据
     * @param nioSocket 客户端socket
     * @return 服务器instance
     */
    Server sendExceptClient(byte[] content, NIOSocket nioSocket);

    /**
     * 发送数据给服务器
     * @param content 数据
     * @return 服务器instance
     */
    Server sendAllClient(byte[] content);

    /**
     * 设置socket事件监听
     * @param serverSocketObserver 监听接口
     * @return 服务器instance
     */
    Server setServerSocketObserver(ServerSocketObserver serverSocketObserver);

    /**
     * 添加服务器回调读数据监听
     * @param mlistener 监听接口
     * @return 服务器instance
     */
    Server addServerReadListener(OnServerReadListener mlistener);

    /**
     * 删除服务器回调写数据监听
     * @param mlistener 监听接口
     * @return 服务器instance
     */
    Server removeServerReadListener(OnServerReadListener mlistener);

    /**
     * 删除服务器回调写数据监听
     * @param key key
     * @param o 数据
     */
    void putData(String key, Object o);

    /**
     * 删除服务器回调写数据监听
     * @param key key
     * @return 数据
     */
    Object putData(String key);


}
