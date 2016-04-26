package com.example.administrator.puzzleGame.activity;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.example.Client;
import com.example.ClientLAN;
import com.example.OnClientReadListener;
import com.example.OnServerReadListener;
import com.example.Server;
import com.example.ServerLAN;
import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.adapter.GameProgressAdapter;
import com.example.administrator.puzzleGame.base.BaseHandler;
import com.example.administrator.puzzleGame.util.DrawbalBuilderUtil;
import com.example.administrator.puzzleGame.view.Game3DView;
import com.example.nioFrame.NIOSocket;
import com.example.protocol.MSGProtocol;
import com.example.serialization.Serializer;
import com.example.serialization.SerializerFastJson;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity implements BaseHandler.OnMessageListener {
    private Game3DView mGLSurfaceView;
    private RecyclerView mRecyclerView;
    private GameProgressAdapter mAdapter;
    private BaseHandler.UnleakHandler handler;
    private Serializer serializer;
    private Client client;
    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //切换到主界面
        setContentView(R.layout.activity_game3d);

        serializer = SerializerFastJson.getInstance();
        handler = new BaseHandler.UnleakHandler(this);

        if(this.getIntent().getBooleanExtra("isServer", false)){
            server = ServerLAN.getInstance();
        }
        initNet();
        initGmaeView();
        initProgressView();

    }

    private void initGmaeView() {
        //初始化GLSurfaceView
        mGLSurfaceView = (Game3DView) findViewById(R.id.game_3d_view);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控

        Game3DView.MsgSender msgSender = new Game3DView.MsgSender() {
            @Override
            public void sendMsgProtocol(MSGProtocol msgProtocol) {
                client.sendToServer(serializer.serialize(msgProtocol).getBytes());
            }
        };
        //TODO 初始化游戏模式
        //mGLSurfaceView.init(5, Game3DView.ObjectType.QUAD_PLANE, false, msgSender);
        mGLSurfaceView.init(5, Game3DView.ObjectType.CUBE, true, msgSender);
        //mGLSurfaceView.init(8, Game3DView.ObjectType.SPHERE, true, msgSender);
    }

    private void initProgressView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list_game_progress_view);
        mRecyclerView.setHasFixedSize(true);
        List<GameProgressAdapter.ListData> listDatas = new ArrayList<>();


        //TODO 插入玩家数据
        /*List<User> users = (List<User>) client.getData("userList");
        for (User user : users) {
            listDatas.add((new GameProgressAdapter.ListData(user.getName(), user.getName().substring(0, 1))));
        }*/
        //test数据需要删除
        for (int i = 0; i < 5; i++) {
            listDatas.add((new GameProgressAdapter.ListData("玩家" + i, String.valueOf(i))));
        }

        mAdapter = new GameProgressAdapter(
                this,
                R.layout.listitem_game_progress,
                listDatas,
                DrawbalBuilderUtil.getDrawbalBuilder(DrawbalBuilderUtil.DRAWABLE_TYPE.SAMPLE_ROUND_RECT_BORDER)
        );

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
    }

    private void initNet() {
        client = ClientLAN.getInstance();
        final Serializer serializer = SerializerFastJson.getInstance();
        client.addClientReadListener(new OnClientReadListener() {
            @Override
            public void processMsg(byte[] packet) {
                MSGProtocol msgProtocol = serializer.parseNull(new String(packet), MSGProtocol.class);
                int command = msgProtocol.getCommand();
                Message message = new Message();
                message.what = command;
                switch (command){

                }
                handler.sendMessage(message);
            }
        });
        if (server != null) {
            server.addServerReadListener(new OnServerReadListener() {
                @Override
                public void processMsg(byte[] packet, NIOSocket nioSocket) {

                }
            });
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    public void processMessage(Message message) {
        switch (message.what) {

        }
    }
}
