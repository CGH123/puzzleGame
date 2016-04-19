package com.example.administrator.puzzleGame.activity;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.puzzleGame.R;

import com.example.administrator.puzzleGame.view.Game3DView;

public class Game3DActivity extends Activity {
    private Game3DView mGLSurfaceView;

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

        //初始化GLSurfaceView
        mGLSurfaceView = (Game3DView) findViewById(R.id.game_3d_view);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控

        //初始化游戏设置
        //mGLSurfaceView.init(5, Game3DView.ObjectType.QUAD_PLANE, false);
        //mGLSurfaceView.init(5, Game3DView.ObjectType.CUBE, true);
        mGLSurfaceView.init(8, Game3DView.ObjectType.SPHERE, true);
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
}
