package com.example.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.msgbean.Entity;
import com.example.msgbean.Packer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络应用层协议
 * 处理怎么包装数据
 * Created by HUI on 2016-04-23.
 */
public class MSGProtocol {

    /*
    协议其实很简单啊，就一个name command option object
    可以用手机的IMEI号当name
    option是选项
    command是包的命令
    object是附加数据对象
     */

    public enum SET_TYPE {
        //待写，根据游戏中需要传递的数据来判断
        BEAN, LIST
    }

    @JSONField(serialize = false)
    private static Map<Class<?>, Entity.ADD_TYPE> typeMap = new HashMap<>();

    private String senderName;    //手机IMEI
    private int option;  //附加数据对象 选项内容待定
    private int command;    //包的命令
    private Entity.ADD_TYPE addType; //附加的对象的类型
    private SET_TYPE setType; //附加的对象的集合类型
    private Packer<? extends Entity> addObject;   //附加的对象
    private List<? extends Entity> addObjects = new ArrayList<>();   //附加的对象

    @JSONField(serialize = false)
    public static void register(Class<?> tClass, Entity.ADD_TYPE addType) {
        typeMap.put(tClass, addType);
    }

    public MSGProtocol() {
    }

    public MSGProtocol(String paramSenderName, int paramCommand) {
        this.senderName = paramSenderName;
        this.option = ProtocolConstant.OPTION_DEFAULT;
        this.command = paramCommand;
    }


    public MSGProtocol(String paramSenderName, int option, int paramCommand) {
        this.senderName = paramSenderName;
        this.option = option;
        this.command = paramCommand;
    }


    public MSGProtocol(String paramSenderName, int paramCommand, Entity paramObject) {
        this.senderName = paramSenderName;
        this.option = ProtocolConstant.OPTION_DEFAULT;
        this.command = paramCommand;
        this.addObject = new Packer<>(paramObject);
        this.setType = SET_TYPE.BEAN;
        this.addType = typeMap.get(paramObject.getClass());
    }


    public MSGProtocol(String paramSenderName, int option, int paramCommand, Entity paramObject) {
        this.senderName = paramSenderName;
        this.option = option;
        this.command = paramCommand;
        this.addObject = new Packer<>(paramObject);
        this.setType = SET_TYPE.BEAN;
        this.addType = typeMap.get(paramObject.getClass());
    }

    public MSGProtocol(String paramSenderName, int paramCommand, List<? extends Entity> paramObjects) {
        this.senderName = paramSenderName;
        this.option = ProtocolConstant.OPTION_DEFAULT;
        this.command = paramCommand;
        this.addObjects = paramObjects;
        this.setType = SET_TYPE.LIST;
        this.addType = typeMap.get(paramObjects.get(0).getClass());
    }


    public MSGProtocol(String paramSenderName, int option, int paramCommand, List<? extends Entity> paramObjects) {
        this.senderName = paramSenderName;
        this.option = option;
        this.command = paramCommand;
        this.addObjects = paramObjects;
        this.setType = SET_TYPE.LIST;
        this.addType = typeMap.get(paramObjects.get(0).getClass());
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public Entity.ADD_TYPE getAddType() {
        return addType;
    }

    public void setAddType(Entity.ADD_TYPE addType) {
        this.addType = addType;
    }

    public SET_TYPE getSetType() {
        return setType;
    }

    public void setSetType(SET_TYPE setType) {
        this.setType = setType;
    }


    public Packer<? extends Entity> getAddObject() {
        return addObject;
    }

    public void setAddObject(Packer<? extends Entity> addObject) {
        this.addObject = addObject;
    }

    public List<? extends Entity> getAddObjects() {
        return addObjects;
    }

    public void setAddObjects(List<? extends Entity> addObjects) {
        this.addObjects = addObjects;
    }


}
