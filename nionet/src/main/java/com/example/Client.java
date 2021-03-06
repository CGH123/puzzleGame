package com.example;

import com.example.nioFrame.SocketObserver;

public interface Client {

    /**
     * 绑定线程
     *
     * @param port 端口
     * @return 客户端instance
     */
    Client bind(int port);

    /**
     * 绑定线程
     *
     * @param host 地址
     * @param port 端口
     * @return 客户端instance
     */
    Client bind(String host, int port);


    /**
     * 开启线程
     *
     * @return 客户端instance
     */
    Client start();

    /**
     * 绑定线程
     *
     * @param timeout 寻找时间
     * @return 服务器IP
     */
    String findServerIP(int timeout);

    /**
     * 开启线程
     *
     * @return 服务器instance
     */
    Client startUdp(int port);

    /**
     * 停止线程
     *
     * @return 服务器instance
     */
    Client stopUdp();

    /**
     * 停止线程
     *
     * @return 客户端instance
     */
    Client stop();

    /**
     * 关闭线程
     *
     * @return 客户端instance
     */
    Client close();

    /**
     * 发送数据给服务器
     *
     * @param content 数据
     * @return 客户端instance
     */
    Client sendToServer(byte[] content);

    /**
     * 设置socket事件监听
     *
     * @param socketObserver 监听接口
     * @return 客户端instance
     */
    Client setSocketObserver(SocketObserver socketObserver);

    /**
     * 添加客户端回调读数据监听
     *
     * @param mlistener 监听接口
     * @return 客户端instance
     */
    Client setClientReadListener(OnClientReadListener mlistener);

    /**
     * 删除服务器回调写数据监听
     *
     * @param key key
     * @param o   数据
     */
    <T> void putData(String key, T o);

    /**
     * 删除服务器回调写数据监听
     *
     * @param key key
     * @return 数据
     */
    <T> T getData(String key);
}
