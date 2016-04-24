package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.Client;
import com.example.ClientLAN;
import com.example.NetConstant;
import com.example.OnClientReadListener;
import com.example.OnServerReadListener;
import com.example.Server;
import com.example.ServerLAN;
import com.example.administrator.puzzleGame.R;
import com.example.nioFrame.NIOSocket;
import com.example.protocol.MSGProtocol;
import com.example.serialization.SerializerFastJson;

/**
 * Created by HUI on 2016-04-24.
 */
public class GameActivity extends Activity implements View.OnClickListener{

    Button sent;
    Button server_init;
    Button client_init;
    TextView content;
    MyHandler myHandler;

    Server server;
    Client client;

    boolean isServer = false;
    boolean isClient = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gameplay);

        initView();
        initEvent();
    }

    private void initView(){
        sent = (Button)findViewById(R.id.test_send);
        server_init = (Button)findViewById(R.id.server_init);
        client_init = (Button)findViewById(R.id.client_init);
        content = (TextView)findViewById(R.id.text_show);
        myHandler = new MyHandler();
    }

    private void initEvent(){
        sent.setOnClickListener(this);
        server_init.setOnClickListener(this);
        client_init.setOnClickListener(this);
        server = ServerLAN.getInstance();
        client = ClientLAN.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_send:
                if(!isClient) break;
                MSGProtocol msgProtocol = new MSGProtocol("Hi",1);
                String temp = SerializerFastJson.getInstance().serialize(msgProtocol);
                client.sendToServer(temp.getBytes());

                Message msg = new Message();
                msg.what = NetConstant.Message_Sen;
                Bundle bundle = new Bundle();
                bundle.putString("name",msgProtocol.getSenderName());
                msg.setData(bundle);
                myHandler.sendMessage(msg);

                break;
            case R.id.server_init:
                if(isServer) break;
                isServer = true;
                OnServerReadListener serverReadListener = new OnServerReadListener() {
                    @Override
                    public void processMsg(byte[] packet, NIOSocket nioSocket) {
                        MSGProtocol msgProtocol = SerializerFastJson.getInstance().parseNull(new String(packet), MSGProtocol.class);

                        Message msg = new Message();
                        msg.what = NetConstant.Message_Rec;
                        Bundle bundle = new Bundle();
                        bundle.putString("name",msgProtocol.getSenderName());
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);

                        MSGProtocol temp = new MSGProtocol("hello", msgProtocol.getCommand());
                        String msgString = SerializerFastJson.getInstance().serialize(temp);
                        //server.sendToClient(msgString.getBytes(),nioSocket);
                        server.sendAllClient(msgString.getBytes());
                    }
                };

                server.addServerReadListener(serverReadListener).bind(NetConstant.TCP_PORT).start();
                break;
            case R.id.client_init:
                if(isClient) break;
                isClient = true;
                OnClientReadListener clientReadListener = new OnClientReadListener() {
                    @Override
                    public void processMsg(byte[] packet) {
                        MSGProtocol msgProtocol = SerializerFastJson.getInstance().parseNull(new String(packet), MSGProtocol.class);

                        Message msg = new Message();
                        msg.what = NetConstant.Message_Rec;
                        Bundle bundle = new Bundle();
                        bundle.putString("name",msgProtocol.getSenderName());
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);

                    }
                };

                client.addClientReadListener(clientReadListener).bind(NetConstant.TCP_PORT).start();
                break;
        }
    }

    private class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            String name;
            switch(msg.what){
                case NetConstant.Message_Rec:
                    //TODO 处理收到的消息
                    bundle = msg.getData();
                    name = "我收到消息"+bundle.getString("name")+"\n";
                    content.setText(content.getText()+name);
                    break;
                case NetConstant.Message_Sen:
                    //TODO 处理发送的消息

                    bundle = msg.getData();
                    name = "我发送消息"+bundle.getString("name")+"\n";
                    content.setText(content.getText()+name);
                    break;
            }
        }
    }


}
