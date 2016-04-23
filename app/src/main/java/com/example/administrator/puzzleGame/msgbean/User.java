package com.example.administrator.puzzleGame.msgbean;

import com.example.protocol.Entity;
import com.example.protocol.MSGProtocol;
import com.example.serialization.Serializer;
import com.example.serialization.SerializerFastJson;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家信息
 * Created by HUI on 2016-04-23.
 */
public class User extends Entity {
    public String name;

    User() {
    }

    public User(String name) {
        this.name = name;
    }

    public static void main(String args[]) {
        Serializer serializer = SerializerFastJson.getInstance();
        User user = new User("gj");
        List<User> users = new ArrayList<>();
        User user2 = new User("cgh");
        users.add(user);
        users.add(user2);
        MSGProtocol<User> msgProtocol1 = new MSGProtocol<>("guojun", 1, user);
        MSGProtocol<User> msgProtocol2 = new MSGProtocol<>("guojun", 2, users);
        String s1 = serializer.serialize(msgProtocol1);
        System.out.println(s1);
        String s2 = serializer.serialize(msgProtocol2);
        System.out.println(s2);
        msgProtocol1 = serializer.parse(s1, User.class);
        msgProtocol2 = serializer.parse(s2, User.class);
        if (msgProtocol1.getSetType() == MSGProtocol.SET_TYPE.BEAN)
            user = msgProtocol1.getAddObject();
        if (msgProtocol2.getSetType() == MSGProtocol.SET_TYPE.LIST)
            users = msgProtocol2.getAddObjects();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
