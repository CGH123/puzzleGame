package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.Client;
import com.example.ClientLAN;
import com.example.NetConstant;
import com.example.OnClientReadListener;
import com.example.OnServerReadListener;
import com.example.Server;
import com.example.ServerLAN;
import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.adapter.RoomAdapter;
import com.example.administrator.puzzleGame.base.BaseHandler;
import com.example.administrator.puzzleGame.constant.CmdConstant;
import com.example.administrator.puzzleGame.constant.GameConstant;
import com.example.administrator.puzzleGame.msgbean.User;
import com.example.administrator.puzzleGame.util.DrawbalBuilderUtil;
import com.example.administrator.puzzleGame.view.MultiListView;
import com.example.nioFrame.NIOSocket;
import com.example.protocol.MSGProtocol;
import com.example.serialization.Serializer;
import com.example.serialization.SerializerFastJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HUI on 2016-04-26.
 *
 * @author cgh
 */
public class GameRoomActivity extends Activity implements
        View.OnClickListener,
        MultiListView.OnRefreshListener,
        AdapterView.OnItemClickListener,
        BaseHandler.OnMessageListener,
        OnServerReadListener,
        OnClientReadListener {
    private RoomAdapter mAdapter;
    private Button mBtnStart;
    private Button mBtnCreate;
    /**
     * ListView的显示
     */
    private MultiListView mListView;
    /**
     * 图标显示
     */
    private ImageView mIvAvatar;
    /**
     * 名字显示
     */
    private TextView mName;
    /**
     * 黑色框框显示玩家数量
     */
    private TextView mPlayersNum;
    /**
     * 图标右边的玩家显示
     */
    private TextView mOrder;

    private boolean isFind = false;

    private Server server;
    private Client client;
    private FindServerTask findServerTask;
    private CreateServerTask createServerTask;
    private BaseHandler.UnleakHandler handler;
    private Serializer serializer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameroom);

        GameConstant.NAME = Build.MANUFACTURER;
        GameConstant.PHONE = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        initNet();
        initViews();
        initEvent();
        handler = new BaseHandler.UnleakHandler(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (findServerTask != null && !findServerTask.isCancelled()) {
            findServerTask.cancel(true);
        }
        if (createServerTask != null && !createServerTask.isCancelled()) {
            createServerTask.cancel(true);
        }
    }

    private void initNet() {
        serializer = SerializerFastJson.getInstance();
        client = ClientLAN.getInstance()
                .startUdp(NetConstant.UDP_PORT);
        List<User> mLocalUsersList = new ArrayList<>();
        client.putData("users", mLocalUsersList);
    }


    private void initViews() {
        mIvAvatar = (ImageView) findViewById(R.id.gameroom_my_avatar);
        mName = (TextView) findViewById(R.id.gameroom_my_name);
        mPlayersNum = (TextView) findViewById(R.id.gameroom_tv_playersnumber);
        mOrder = (TextView) findViewById(R.id.gameroom_my_order);

        mBtnStart = (Button) findViewById(R.id.gameroom_start);
        mBtnCreate = (Button) findViewById(R.id.gameroom_create);

        mListView = (MultiListView) findViewById(R.id.friends_list);

    }

    private void initEvent() {
        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        TextDrawable drawable = DrawbalBuilderUtil.getDrawbalBuilder(DrawbalBuilderUtil.DRAWABLE_TYPE.SAMPLE_ROUND_RECT_BORDER)
                .build(GameConstant.NAME.substring(0, 1), mColorGenerator.getColor(GameConstant.NAME));
        mName.setText(GameConstant.NAME);
        mIvAvatar.setImageDrawable(drawable);
        mPlayersNum.setText(R.string.gameroom_emptyplayer);

        mBtnCreate.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        List<User> users = client.getData("users");
        mAdapter = new RoomAdapter(this, users);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View v) {
        MSGProtocol<User> msgProtocol;
        switch (v.getId()) {
            case R.id.gameroom_start:
                msgProtocol = new MSGProtocol<>(GameConstant.NAME, CmdConstant.START);
                client.sendToServer(serializer.serialize(msgProtocol).getBytes());

                break;
            case R.id.gameroom_create:
                createServerTask = new CreateServerTask();
                createServerTask.execute();

                mBtnCreate.setVisibility(View.GONE);
                mBtnStart.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onRefresh() {
        if (server != null || isFind) {
            mListView.onRefreshComplete();
        }
        else {
            findServerTask = new FindServerTask();
            findServerTask.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //用来选择房间，暂时不用这个功能，默认第一个房间
    }

    @Override
    //客户端回调
    public void processMsg(byte[] packet) {
        String s = new String(packet);
        MSGProtocol msgProtocol = serializer.parse(s, MSGProtocol.class);
        int cmd = msgProtocol.getCommand();
        Message message = new Message();
        message.what = cmd;
        switch (cmd) {
            case CmdConstant.CONNECT:
                List<User> users = msgProtocol.getAddObjects();
                for (int i = 0; i < users.size(); i++)
                    if ((users.get(i)).getName().equals(GameConstant.NAME))
                        message.getData().putInt("order", i);
                client.putData("users", users);
                mAdapter.setData(users); // Adapter加载List数据
                break;
            case CmdConstant.START:
                //不做任何事，在handler中启动activity
                break;
        }
        handler.sendMessage(message);
    }

    @Override
    //服务器端回调
    public void processMsg(byte[] packet, NIOSocket nioSocket) {
        String s = new String(packet);
        MSGProtocol msgProtocol = serializer.parse(s, MSGProtocol.class);
        int cmd = msgProtocol.getCommand();
        switch (cmd) {
            case CmdConstant.CONNECT:
                User user = (User) msgProtocol.getAddObject();
                List<User> users = server.getData("users");
                users.add(user);
                msgProtocol = new MSGProtocol<>(GameConstant.PHONE, CmdConstant.CONNECT, users);
                break;
            case CmdConstant.START:
                msgProtocol = new MSGProtocol<>(GameConstant.PHONE, CmdConstant.START);
                break;
        }
        server.sendAllClient(serializer.serialize(msgProtocol).getBytes());
    }


    @Override
    //handler回调
    public void processMessage(Message message) {
        switch (message.what) {
            case CmdConstant.CONNECT:
                mAdapter.notifyDataSetChanged();

                mListView.onRefreshComplete();
                mOrder.setText(String.valueOf(message.getData().getInt("order") + 1));

                mBtnCreate.setVisibility(View.GONE);

                break;
            case CmdConstant.START:
                client.stopUdp();
                Intent intent = new Intent(GameRoomActivity.this, GameActivity.class);
                intent.putExtra("isServer", true);
                startActivity(intent);
                break;
        }
    }

    /**
     * 用于显示房主信息的
     */
    private class FindServerTask extends AsyncTask<Void, Void, String> {
  
        @Override
        protected String doInBackground(Void... params) {
            String ip = client.findServerIP(2000);
            if (!ip.equals("")) {
                isFind = true;
                client.bind(ip, NetConstant.TCP_PORT)
                        .start()
                        .addClientReadListener(GameRoomActivity.this);

                MSGProtocol<User> msgProtocol = new MSGProtocol<>(GameConstant.PHONE, CmdConstant.CONNECT, new User(GameConstant.NAME));
                client.sendToServer(serializer.serialize(msgProtocol).getBytes());
            } else {
                System.out.println("UDP not found roomOwner");
                return "FAIL";
            }
            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String ip) {
            mListView.onRefreshComplete();
        }

    }

    /**
     * 用于显示房主信息的
     */
    private class CreateServerTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            server = ServerLAN.getInstance();
            server.initUdp();
            List<User> userList = new ArrayList<>();
            server.putData("users", userList);
            server.bind(NetConstant.TCP_PORT)
                    .start()
                    .addServerReadListener(GameRoomActivity.this);

            client.bind(GameConstant.IP, NetConstant.TCP_PORT)
                    .start()
                    .addClientReadListener(GameRoomActivity.this);

            MSGProtocol<User> msgProtocol = new MSGProtocol<>(GameConstant.PHONE, CmdConstant.CONNECT, new User(GameConstant.NAME));
            client.sendToServer(serializer.serialize(msgProtocol).getBytes());

            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String ip) {

        }

    }
}
