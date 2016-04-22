package com.example;

/**
 * 回调函数接口，让用户定义处理返回的包
 * Created by HUI on 2016-04-22.
 */
public interface OnMsgRecListener {

    /**
     *处理通道返回的数据
     */
    void processMSG(byte[] packet);
}
