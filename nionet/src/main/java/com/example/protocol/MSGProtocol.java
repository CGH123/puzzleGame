package com.example.protocol;

import com.example.msgbean.Entity;
import com.example.msgbean.GameProcess;
import com.example.msgbean.Message;
import com.example.msgbean.User;
import com.example.serialization.SerializerFastJson;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 网络应用层协议
 * 处理怎么包装数据
 * Created by HUI on 2016-04-23.
 */
public class MSGProtocol {

    /*
    协议其实很简单啊，就一个name command option object
    可以用手机的IMEI号当name
    command是包的命令
    option是选项
    object是附加数据对象
     */

    public enum ADDTION_TYPE {
        //待写，根据游戏中需要传递的数据来判断
        GAMEPROCESS,USER,MESSAGE
    }

    private final String NAME = "senderName";
    private final String COMMAND = "command";
    private final String OPTION = "option";
    private final String ADDTYPE = "addType";
    private final String ADDOBJECT = "addObject";

    private String senderName;    //手机IMEI
    private int command;    //包的命令
    private String option;  //附加数据对象 选项内容待定
    private ADDTION_TYPE addType; //附加的对象的类型
    private Entity addObject;   //附加的对象

    public  MSGProtocol(){
    }


    public MSGProtocol(byte[] paramProtocolJSON) throws JSONException{
        JSONObject protocolJSON = new JSONObject(paramProtocolJSON);
        senderName = protocolJSON.getString(NAME);
        command = protocolJSON.getInt(COMMAND);

        //判断是否有附加选项
        if(protocolJSON.has(OPTION)){
            option = protocolJSON.getString(OPTION);
        }else{
            option = null;
        }

        //判断是否有附加的对象
        if(protocolJSON.has(ADDTYPE))
        {
            //获取返回对象的类型
            addType = ADDTION_TYPE.valueOf(protocolJSON.getString(ADDTYPE));
            //获取附加的对象
            byte[] objectData = protocolJSON.getString(ADDOBJECT).getBytes();
            switch (addType){
                case GAMEPROCESS: //游戏通关完成度
                    addObject = new SerializerFastJson().deserialize(objectData, GameProcess.class);
                    break;
                case MESSAGE:
                    addObject = new SerializerFastJson().deserialize(objectData, Message.class);
                    break;
                case USER:
                    addObject = new SerializerFastJson().deserialize(objectData, User.class);
                    break;
            }
        }
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

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public ADDTION_TYPE getAddType() {
        return addType;
    }

    public void setAddType(ADDTION_TYPE addType) {
        this.addType = addType;
    }

    public Entity getAddObject() {
        return addObject;
    }

    public void setAddObject(Entity addObject) {
        this.addObject = addObject;
    }


}
