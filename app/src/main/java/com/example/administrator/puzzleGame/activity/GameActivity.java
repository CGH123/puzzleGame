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
import com.example.administrator.puzzleGame.constant.CmdConstant;
import com.example.administrator.puzzleGame.constant.GameConstant;
import com.example.administrator.puzzleGame.msgbean.GameProcess;
import com.example.administrator.puzzleGame.msgbean.User;
import com.example.administrator.puzzleGame.util.DrawbalBuilderUtil;
import com.example.administrator.puzzleGame.view.Game3DView;
import com.example.nioFrame.NIOSocket;
import com.example.protocol.MSGProtocol;
import com.example.serialization.Serializer;
import com.example.serialization.SerializerFastJson;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity implements
        BaseHandler.OnMessageListener,
        Game3DView.MsgSender {
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

        client = ClientLAN.getInstance();
        if (this.getIntent().getBooleanExtra("isServer", false)) {
            server = ServerLAN.getInstance();
            List<User> users = client.getData("users");
            List<GameProcess> gameProcesses = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                gameProcesses.add(new GameProcess(0f));
            }
            server.putData("processes", gameProcesses);
        }
        initNet();
        initGmaeView();
        initProgressView();

        newGame();

        System.out.println("TCP onCreate");
    }


    private void initGmaeView() {
        //初始化GLSurfaceView
        mGLSurfaceView = (Game3DView) findViewById(R.id.game_3d_view);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控

    }

    private void newGame(){
        //TODO 初始化游戏模式
        //mGLSurfaceView.init(5, Game3DView.ObjectType.QUAD_PLANE, false, this);
        //mGLSurfaceView.init(5, Game3DView.ObjectType.CUBE, true, this);
        mGLSurfaceView.init(8, Game3DView.ObjectType.SPHERE, true, this);
    }

    private void initProgressView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list_game_progress_view);
        mRecyclerView.setHasFixedSize(true);
        List<GameProgressAdapter.ListData> listDatas = new ArrayList<>();

        //TODO 插入玩家数据
        List<User> users = client.getData("users");
        for (User user : users) {
            listDatas.add((new GameProgressAdapter.ListData(user.getName(), user.getName().substring(0, 1))));
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
        final Serializer serializer = SerializerFastJson.getInstance();
        client.addClientReadListener(new OnClientReadListener() {
            @Override
            public void processMsg(byte[] packet) {
                MSGProtocol msgProtocol = serializer.parse(new String(packet), MSGProtocol.class);
                Message message = new Message();
                int cmd = msgProtocol.getCommand();
                message.what = cmd;
                switch (cmd) {
                    case CmdConstant.PROGRESS:
                        List<GameProcess> gameProcess = (List<GameProcess>) msgProtocol.getAddObjects();
                        System.out.println("TCP client receive ="+gameProcess.get(0).getProgress());
                        client.putData("processes", gameProcess);
                        break;
                }
                handler.sendMessage(message);
            }
        });
        if (server != null) {
            server.addServerReadListener(new OnServerReadListener() {
                @Override
                public void processMsg(byte[] packet, NIOSocket nioSocket) {
                    MSGProtocol msgProtocol = serializer.parse(new String(packet), MSGProtocol.class);
                    int cmd = msgProtocol.getCommand();
                    switch (cmd) {
                        case CmdConstant.PROGRESS:
                            GameProcess gameProcess = (GameProcess) msgProtocol.getAddObject();
                            List<GameProcess> gameProcesses = (List<GameProcess>) server.getData("processes");
                            for (GameProcess gameProcess1 : gameProcesses) {
                                if (gameProcess1.equals(gameProcess))
                                    gameProcess1.setProgress(gameProcess.getProgress());
                            }
                            msgProtocol = new MSGProtocol(GameConstant.PHONE, CmdConstant.PROGRESS, gameProcesses);
                            server.putData("processes", gameProcess);
                            System.out.println("TCP server receive="+gameProcess.getProgress());
                            break;
                    }
                    server.sendAllClient(serializer.serialize(msgProtocol).getBytes());
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
            case CmdConstant.PROGRESS:
                List<GameProcess> gameProcesses = client.getData("processes");
                for (int i = 0; i < gameProcesses.size(); i++) {
                    mAdapter.setGameProgress(i, gameProcesses.get(i).getProgress());
                }
                mAdapter.notifyDataSetChanged();
                break;

        }
    }

    @Override
    public void sendMsgProtocol(MSGProtocol msgProtocol) {
        GameProcess gameProcess = (GameProcess) msgProtocol.getAddObject();
        System.out.println("TCP client send="+gameProcess.getProgress());
        client.sendToServer(serializer.serialize(msgProtocol).getBytes());
    }
}
