package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.Client;
import com.example.ClientLAN;
import com.example.NetConstant;
import com.example.Server;
import com.example.ServerLAN;
import com.example.administrator.puzzleGame.R;

/**
 * 未在.xml中登记
 * 暂时用来测试数据用的
 * 用于搜索游戏房间的信息
 * Created by HUI on 2016-04-24.
 */
public class GameRoomActivity extends Activity implements View.OnClickListener {

    Server server;
    Client client;


    Button game_build;
    Button game_start;
    Button game_ready;
    Button game_refresh;
    TextView detail_show; //用来测试网络连接中的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gameroom);
        initViews();
        initEvent();
        initAction();
    }

    private void initViews() {
        game_build = (Button) findViewById(R.id.button_build);
        game_start = (Button) findViewById(R.id.button_start);
        game_ready = (Button) findViewById(R.id.button_ready);
        detail_show = (TextView) findViewById(R.id.test_gameroom);
        game_refresh = (Button) findViewById(R.id.button_refresh);
    }

    private void initEvent() {
        game_build.setOnClickListener(this);
        game_ready.setOnClickListener(this);
        game_start.setOnClickListener(this);
        game_refresh.setOnClickListener(this);
    }

    private void initAction() {
        //待定吧
        detail_show.setText("fuck");
    }

    @Override
    protected void onStart() {
        super.onStart();
        server = ServerLAN.getInstance();
        client = ClientLAN.getInstance();
        client.startUdp(NetConstant.UDP_PORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        client.stopUdp();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_build:
                //TODO 创建UDP服务端
                server.startUdp(9788);
                break;
            case R.id.button_refresh:
                //TODO 用来接收房主信息
                detail_show.setText("test");
                new DownTask().execute();
                break;
            case R.id.button_ready:
                //TODO 建立TCP链接准备,暂时用来测试关闭UDP
                server.stopUdp();
                break;
            case R.id.button_start:
                //TODO 开始进行TCP连接游戏对战
                //待写，用来判断已经进入房间，用来测试UDP的
                //目前用于跳转到游戏界面
                Intent intent = new Intent(GameRoomActivity.this, GameActivity.class);
                startActivity(intent);
        }
    }

    private class DownTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String temp = client.findServerIP(2000);
            String ip = "ip为";
            if (temp.equals("")) ip += ":找不到";
            else ip += temp;
            return ip;
        }

        @Override
        protected void onPostExecute(String ip) {
            detail_show.setText("内容如下" + ip);
        }

    }

}
