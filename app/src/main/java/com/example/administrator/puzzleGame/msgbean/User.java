package com.example.administrator.puzzleGame.msgbean;

import com.example.protocol.Entity;

/**
 * 玩家信息
 * Created by HUI on 2016-04-23.
 */
public class User extends Entity {
    public String name;
    User(){
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
