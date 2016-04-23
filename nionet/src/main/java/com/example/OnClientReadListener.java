package com.example;

public interface OnClientReadListener {
    void processMsg(byte[] packet);
}