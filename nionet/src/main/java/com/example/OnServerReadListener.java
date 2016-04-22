package com.example;

import com.example.nioFrame.NIOSocket;

public interface OnServerReadListener {
    void processMsg(byte[] packet, NIOSocket nioSocket);
}