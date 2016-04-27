package com.example.protocol;


import com.alibaba.fastjson.annotation.JSONType;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络应用层协议
 * 处理怎么包装数据
 * <p/>
 * 协议其实很简单啊，就一个name command option object
 * 可以用手机的IMEI号当name
 * option是选项
 * command是包的命令
 * object是附加数据对象
 * Created by HUI on 2016-04-23.
 */
public class MSGProtocol<T extends Entity> {
    private String senderName;    //手机IMEI
    private int option;  //附加数据对象 选项内容待定
    private int command;    //包的命令
    private SET_TYPE setType; //附加的对象的集合类型

    private T addObject;   //附加的对象
    private List<T> addObjects = new ArrayList<>();   //附加的对象
    public MSGProtocol() {
    }

    public MSGProtocol(String paramSenderName, int paramCommand) {
        this.senderName = paramSenderName;
        this.option = ProtocolConstant.OPTION_DEFAULT;
        this.command = paramCommand;
        this.setType = SET_TYPE.NULL;
    }

    public MSGProtocol(String paramSenderName, int option, int paramCommand) {
        this.senderName = paramSenderName;
        this.option = option;
        this.command = paramCommand;
        this.setType = SET_TYPE.NULL;
    }


    public MSGProtocol(String paramSenderName, int paramCommand, T paramObject) {
        this.senderName = paramSenderName;
        this.option = ProtocolConstant.OPTION_DEFAULT;
        this.command = paramCommand;
        this.addObject = paramObject;
        this.setType = SET_TYPE.BEAN;
    }


    public MSGProtocol(String paramSenderName, int option, int paramCommand, T paramObject) {
        this.senderName = paramSenderName;
        this.option = option;
        this.command = paramCommand;
        this.addObject = paramObject;
        this.setType = SET_TYPE.BEAN;
    }


    public MSGProtocol(String paramSenderName, int paramCommand, List<T> paramObjects) {
        this.senderName = paramSenderName;
        this.option = ProtocolConstant.OPTION_DEFAULT;
        this.command = paramCommand;
        this.addObjects = paramObjects;
        this.setType = SET_TYPE.LIST;
    }

    public MSGProtocol(String paramSenderName, int option, int paramCommand, List<T> paramObjects) {
        this.senderName = paramSenderName;
        this.option = option;
        this.command = paramCommand;
        this.addObjects = paramObjects;
        this.setType = SET_TYPE.LIST;
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

    public SET_TYPE getSetType() {
        return setType;
    }

    public void setSetType(SET_TYPE setType) {
        this.setType = setType;
    }

    public T getAddObject() {
        return addObject;
    }

    public void setAddObject(T addObject) {
        this.addObject = addObject;
    }

    public List getAddObjects() {
        return addObjects;
    }

    public void setAddObjects(List<T> addObjects) {
        this.addObjects = addObjects;
    }

    public enum SET_TYPE {
        NULL, BEAN, LIST
    }


}
