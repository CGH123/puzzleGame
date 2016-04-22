package com.example;

public interface OnClientReadListener {
    void processMSG(byte[] packet);
}