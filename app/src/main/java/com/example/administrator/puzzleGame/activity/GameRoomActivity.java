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
    AsyncTask findServerTask;

    Button game_build;
    Button game_start;
    Button game_refresh;
    TextView detail_show; //用来测试网络连接中的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gameroom);
        initViews();
        initEvent();
        initAction();
        client = ClientLAN.getInstance();
        server = ServerLAN.getInstance();
        client.startUdp(NetConstant.UDP_PORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.stopUdp();
        if (findServerTask != null && !findServerTask.isCancelled()) {
            findServerTask.cancel(true);
        }
    }

    private void initViews() {
        game_build = (Button) findViewById(R.id.button_build);
        game_start = (Button) findViewById(R.id.button_start);
        detail_show = (TextView) findViewById(R.id.test_gameroom);
        game_refresh = (Button) findViewById(R.id.button_refresh);
    }

    private void initEvent() {
        game_build.setOnClickListener(this);
        game_start.setOnClickListener(this);
        game_refresh.setOnClickListener(this);
    }

    private void initAction() {
        //待定吧
        detail_show.setText("fuck");
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_build:
                //TODO 创建UDP服务端
                server.initUdp();
                break;
            case R.id.button_refresh:
                //TODO 用来接收房主信息
                detail_show.setText("test");
                findServerTask = new FindServerTask();
                findServerTask.execute();
                break;
            case R.id.button_start:
                //TODO 开始进行TCP连接游戏对战
                //待写，用来判断已经进入房间，用来测试UDP的
                //目前用于跳转到游戏界面
                client.stopUdp();
                Intent intent = new Intent(GameRoomActivity.this, GameTestActivity.class);
                intent.putExtra("isServer", true);
                startActivity(intent);
        }
    }

    private class FindServerTask extends AsyncTask<Void, Void, String> {

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
