package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Client;
import com.example.ClientLAN;
import com.example.NetConstant;
import com.example.Server;
import com.example.ServerLAN;
import com.example.administrator.puzzleGame.adapter.RoomAdapter;
import com.example.administrator.puzzleGame.msgbean.User;
import com.example.administrator.puzzleGame.view.MultiListView;
import com.example.administrator.puzzleGame.R;

import java.util.ArrayList;

/**
 * Created by HUI on 2016-04-26.
 */
public class GameRoomActivity extends Activity implements View.OnClickListener,MultiListView.OnRefreshListener,AdapterView.OnItemClickListener {

    private boolean isMeReady;
    private boolean haveFind;


    private RoomAdapter mAdapter;
    private Button mBtnStart;
    private Button mBtnCreate;
    /**
     * 图标显示
     */
    private ImageView mIvAvatar;
    /**
     * ListView的显示
     */
    private MultiListView mListView;
    /**
     * 名字显示
     */
    private TextView mName;
    /**
     * 黑色框框显示玩家数量
     */
    private TextView mPlayersNum;
    /**
     *图标右边的玩家显示
     */
    private TextView mOrder;

    private ArrayList<User> mLocalUsersList; // 客户端临时在线用户列表，用于adapter初始化

    private Server server;
    private Client client;

    private boolean isServer = false;

    private String roomOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameroom);
        initViews();
        initEvent();



    }

    @Override
    protected void onStart() {
        super.onStart();
        isMeReady = false;
        haveFind = false;
    }

    private void initViews(){
        mIvAvatar = (ImageView) findViewById(R.id.gameroom_my_avatar);
        mName = (TextView) findViewById(R.id.gameroom_my_name);
        mPlayersNum = (TextView) findViewById(R.id.gameroom_tv_playersnumber);
        mOrder = (TextView) findViewById(R.id.gameroom_my_order);


        mBtnStart = (Button) findViewById(R.id.gameroom_start);
        mBtnCreate = (Button) findViewById(R.id.gameroom_create);

        mListView = (MultiListView) findViewById(R.id.friends_list);

        mIvAvatar.setImageDrawable(getResources().getDrawable(R.drawable.water));
        mPlayersNum.setText(R.string.gameroom_emptyplayer);

    }

    private void initEvent(){

        roomOwner = "";
        //测试使用的初始化条件
        mLocalUsersList = new ArrayList<User>();

        mBtnCreate.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mAdapter = new RoomAdapter(this, mLocalUsersList);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);

        server = ServerLAN.getInstance();
        client = ClientLAN.getInstance();
        //初始化Bind
        client.startUdp(NetConstant.UDP_PORT);
 
    }

    /**
     * 创建服务器
     */
    private void createServer(){
        server.startUdp(NetConstant.UDP_PORT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gameroom_start:
                //TODO 完成联机对战
                break;
            case R.id.gameroom_create:
                createServer();
                mBtnCreate.setVisibility(View.GONE);
                mBtnStart.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onRefresh() {
        new DownTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 用于显示房主信息的
     */
    private class DownTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return client.findServerIP(2000);
        }

        @Override
        protected void onPostExecute(String ip) {

            if(!ip.equals("")){
                roomOwner = ip;
                User roomHost = new User(roomOwner);
                mLocalUsersList.add(roomHost);
                mAdapter.setmData(mLocalUsersList); // Adapter加载List数据
                mAdapter.notifyDataSetChanged();
            }else{
                System.out.println("UDP not found roomOwner");
            }

            mListView.onRefreshComplete();
        }

    }
}
