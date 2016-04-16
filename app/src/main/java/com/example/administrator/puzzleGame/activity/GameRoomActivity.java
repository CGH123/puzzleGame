package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.net.UDPSocket;

/**
 * 用于创建服务器和客户端
 * Created by HUI on 2016-04-14.
 */
public class GameRoomActivity extends Activity implements View.OnClickListener{

    private Button create_room;
    private Button ready_room;
    private Button start_room;
    private TextView show_detail;
    private UDPSocket udpSocket;
    private String str;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameroom_test);
        udpSocket =UDPSocket.getInstance();
        initView();
        initEnvent();
    }

    private void initView(){
        create_room = (Button)findViewById(R.id.button1);
        ready_room = (Button)findViewById(R.id.button2);
        start_room = (Button)findViewById(R.id.button3);
        show_detail = (TextView)findViewById(R.id.label);
    }

    private void initEnvent(){
        create_room.setOnClickListener(this);
        ready_room.setOnClickListener(this);
        start_room.setOnClickListener(this);
        str = "";
    }


    protected void onRefresh(){
        DownTask task = new DownTask();
        task.execute();
    }

    class DownTask extends AsyncTask<Void,Void,Boolean>{


        @Override
        protected Boolean doInBackground(Void... params) {
            if(UDPSocket.STATE == true)
                return true;
            else return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean== true){
                str+=++count+"\n";
                show_detail.setText(str);
                UDPSocket.Thread_Flag = false;
            }
            else{
                str+="error"+"\n";
                show_detail.setText(str);
                UDPSocket.Thread_Flag = false;
            }
            UDPSocket.STATE = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                //创建UDP广播
                udpSocket.sendBroad();
                break;
            case R.id.button2:
                //接受UDP广播
                UDPSocket.Thread_Flag = true;
                new Thread(udpSocket).start();
                break;
            case R.id.button3:
                //// TODO
                onRefresh();
                break;
        }
    }

}
