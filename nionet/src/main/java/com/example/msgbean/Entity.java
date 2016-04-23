package com.example.msgbean;

import com.example.protocol.MSGProtocol;
import com.example.serialization.Serializer;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装类
 * Created by HUI on 2016-04-23.
 */
public class Entity {
    public enum ADD_TYPE {
        //待写，根据游戏中需要传递的数据来判断
        GAME_PROCESS, USER, MESSAGE
    }

    public static void registerBeanTypes() {
        MSGProtocol.register(GameProcess.class, ADD_TYPE.GAME_PROCESS);
        MSGProtocol.register(User.class, ADD_TYPE.USER);
        MSGProtocol.register(Message.class, ADD_TYPE.MESSAGE);
    }

    public static void main(String args[]) {
        Serializer serializer = Serializer.DEFAULT;
        Entity.registerBeanTypes();
        User user = new User("gj");
        List<User> users = new ArrayList<>();
        User user2 = new User("cgh");
        users.add(user);
        users.add(user2);
        MSGProtocol msgProtocol1 = new MSGProtocol("guojun", 1, user);
        MSGProtocol msgProtocol2 = new MSGProtocol("guojun", 2, users);
        String s1 = serializer.serialize(msgProtocol1);
        System.out.println(s1);
        String s2 = serializer.serialize(msgProtocol2);
        System.out.println(s2);
        msgProtocol1 = serializer.parseObject(s1, MSGProtocol.class);
        msgProtocol2 = serializer.parseObject(s2, MSGProtocol.class);

    }
}
