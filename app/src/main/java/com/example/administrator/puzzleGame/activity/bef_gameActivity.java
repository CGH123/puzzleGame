package com.example.administrator.puzzleGame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import com.example.administrator.puzzleGame.R;

/**
 * Created by Administrator on 2016-03-15.
 */
public class bef_gameActivity extends AppCompatActivity {
    private TextView b_newgame;
    private Button b_backmusic;
    private Button b_choseclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_in);
    }

    /*
    用来测试专用的入口
     */
    public void test_entry(View view) {
        Intent intent = new Intent(bef_gameActivity.this, ConnectModeActivtiy.class);
        startActivity(intent);
    }


    /*
    新游戏
     */
    public void b_newgame_(View view) {
        Intent intent = new Intent(bef_gameActivity.this, GamePlayActivity.class);
        startActivity(intent);
    }

    public void bgMusicSel(View view) {
        Intent intent = new Intent(bef_gameActivity.this, bg_musicActivity.class);
        startActivity(intent);
    }

    public void to_pic_chose(View view) {
//        Intent intent= new Intent(bef_gameActivity.this,Pic_choseActivity.class);
//        startActivity(intent);
    }

    /*
    难度选择
     */
    public void Diffcult_choice(View view) {
        Intent intent = new Intent(bef_gameActivity.this, Diffcult_choiceActivity.class);
        startActivity(intent);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitBy2Click();
           /* new AlertDialog.Builder(bef_gameActivity.this).setTitle("系统提示").setMessage("是否退出游戏")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        bef_gameActivity.this.finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            }).create().show();*/

        }
        return true;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}
