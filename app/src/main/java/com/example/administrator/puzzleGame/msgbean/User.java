package com.example.administrator.puzzleGame.msgbean;

import android.content.Context;
import android.telephony.TelephonyManager;

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
        User user2 = new User("cgh");
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        MSGProtocol<User> msgProtocol1 = new MSGProtocol<>("guojun", 1, user);
        MSGProtocol<User> msgProtocol2 = new MSGProtocol<>("guojun", 2, users);
        String s1 = serializer.serialize(msgProtocol1);
        System.out.println(s1);
        String s2 = serializer.serialize(msgProtocol2);
        System.out.println(s2);


        MSGProtocol msgProtocol3 = serializer.parse(s1, MSGProtocol.class);
        MSGProtocol msgProtocol4 = serializer.parse(s2, MSGProtocol.class);
        user =  (User) msgProtocol3.getAddObject();
        List userList = msgProtocol4.getAddObjects();
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name != null ? name.equals(user.name) : user.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
