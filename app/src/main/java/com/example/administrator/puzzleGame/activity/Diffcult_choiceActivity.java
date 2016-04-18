package com.example.administrator.puzzleGame.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.puzzleGame.R;

import com.example.administrator.puzzleGame.sqlServer.GameDB;

/**
 * Created by Administrator on 2016-03-24.
 */
public class Diffcult_choiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.difficult_choice);
    }

    public void easy_game(View view) {
        GameDB.Diffcult_choice = 0;
        Toast.makeText(this, "当前游戏难度为： 简单模式", Toast.LENGTH_SHORT).show();
    }

    public void normal_game(View view) {
        GameDB.Diffcult_choice = 1;
        Toast.makeText(this, "当前游戏难度为： 正常模式", Toast.LENGTH_SHORT).show();
    }

    public void hard_game(View view) {
        GameDB.Diffcult_choice = 2;
        Toast.makeText(this, "当前游戏模式为困难模式", Toast.LENGTH_SHORT).show();
    }
}
