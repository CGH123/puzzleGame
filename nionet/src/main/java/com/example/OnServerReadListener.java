package com.example;

import com.example.nioFrame.NIOSocket;

public interface OnServerReadListener {
    void processMSG(byte[] packet, NIOSocket nioSocket);
}