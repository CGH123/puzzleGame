package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.puzzleGame.R;

import com.example.administrator.puzzleGame.sqlServer.GameDB;

/**
 * Created by Administrator on 2016/3/24.
 */
public class bg_musicActivity extends Activity {
    private  RadioGroup ra; 
    private Button bt;
    private RadioButton bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt10,bt11;
    private Intent intentSer = new Intent("com.angel.Android.MUSIC");
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bg_music);
        ra = (RadioGroup) findViewById(R.id.radioBt);
        bt = (Button) findViewById(R.id.sureSel);
        bt1 = (RadioButton) findViewById(R.id.radio0);
        bt2 = (RadioButton) findViewById(R.id.radio1);
        bt3 = (RadioButton) findViewById(R.id.radio2);
        bt4 = (RadioButton) findViewById(R.id.radio3);
        bt5 = (RadioButton) findViewById(R.id.radio4);
        bt6 = (RadioButton) findViewById(R.id.radio5);
        bt7 = (RadioButton) findViewById(R.id.radio6);
        bt8 = (RadioButton) findViewById(R.id.radio7);
        bt9 = (RadioButton) findViewById(R.id.radio8);
        bt10 = (RadioButton) findViewById(R.id.radio9);
        bt11 = (RadioButton) findViewById(R.id.radio10);
        ra.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                stopService(intentSer);
                if (bt1.getId() == checkedId) {
                    GameDB.musicSelect = 0;
                    startService(intentSer);
                } else if (bt2.getId() == checkedId) {
                    GameDB.musicSelect = 1;
                    startService(intentSer);
                } else if (bt3.getId() == checkedId) {
                    GameDB.musicSelect = 2;
                    startService(intentSer);
                } else if (bt4.getId() == checkedId) {
                    GameDB.musicSelect = 3;
                    startService(intentSer);
                } else if (bt5.getId() == checkedId) {
                    GameDB.musicSelect = 4;
                    startService(intentSer);
                } else if (bt6.getId() == checkedId) {
                    GameDB.musicSelect = 5;
                    startService(intentSer);
                } else if (bt7.getId() == checkedId) {
                    GameDB.musicSelect = 6;
                    startService(intentSer);
                } else if (bt8.getId() == checkedId) {
                    GameDB.musicSelect = 7;
                    startService(intentSer);
                } else if (bt9.getId() == checkedId) {
                    GameDB.musicSelect = 8;
                    startService(intentSer);
                } else if (bt10.getId() == checkedId) {
                    GameDB.musicSelect = 9;
                    startService(intentSer);
                } else {
                    GameDB.musicSelect = 10;
                    startService(intentSer);
                }

            }
        });
    }
    public void sureSelect(View view){
        startService(intentSer);
        Toast.makeText(getApplicationContext(), "选择成功", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(bg_musicActivity.this,bef_gameActivity.class);
        startActivity(intent);
        stopService(intentSer);
        this.finish();
    }
    public void back(View view){
        Intent intent=new Intent(bg_musicActivity.this,bef_gameActivity.class);
        startActivity(intent);
        stopService(intentSer);
        this.finish();
    }
}
