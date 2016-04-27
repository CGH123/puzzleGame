package com.example.administrator.puzzleGame.msgbean;

import com.example.protocol.Entity;

/**
 * 玩家发送的信息
 * Created by HUI on 2016-04-23.
 */
public class Message extends Entity {
    private String message;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
