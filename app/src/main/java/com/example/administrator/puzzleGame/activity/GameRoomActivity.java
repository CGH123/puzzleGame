package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.view.MultiListView;
import com.squareup.picasso.Picasso;

/**
 * Created by HUI on 2016-04-26.
 */
public class GameRoomActivity extends Activity {

    private boolean isMeReady;
    private boolean haveFind;

//    private PlayersAdapter mAdapter;
    private Button mBtnReady;
    private Button mBtnStart;
    private Button mBtnCreate;
    private ImageView mIvAvatar;
    private MultiListView mListView;
    private TextView mName;
    private TextView mPlayersNum;
    private TextView mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameroom);
        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews(){
        mIvAvatar = (ImageView) findViewById(R.id.gameroom_my_avatar);
        mName = (TextView) findViewById(R.id.gameroom_my_name);
        mPlayersNum = (TextView) findViewById(R.id.gameroom_tv_playersnumber);
        mOrder = (TextView) findViewById(R.id.gameroom_my_order);
        

        mBtnReady = (Button) findViewById(R.id.gameroom_ready);
        mBtnStart = (Button) findViewById(R.id.gameroom_start);
        mBtnCreate = (Button) findViewById(R.id.gameroom_create);

        mListView = (MultiListView) findViewById(R.id.friends_list);

        //Picasso.with(mContext).load(ImageUtils.getImageID(User.AVATAR + SessionUtils.getAvatar())).into(mIvAvatar);
        //mName.setText(SessionUtils.getNickname());
        mIvAvatar.setImageDrawable(getResources().getDrawable(R.drawable.icon));
        mPlayersNum.setText(R.string.gameroom_emptyplayer);

    }


}
