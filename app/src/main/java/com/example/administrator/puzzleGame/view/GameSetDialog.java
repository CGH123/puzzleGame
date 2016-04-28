package com.example.administrator.puzzleGame.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.constant.GameConstant;
import com.example.administrator.puzzleGame.gameModel.Vector2f;

public class GameSetDialog extends Dialog {

    private QuadSetView quadSetView;
    private Button btnSphere, btnQuad, btnCube, btnSure;
    private Spinner spinner;

    private OnGameChangedListener mListener;
    private Vector2f[] quadPoints;
    private int mode;
    private int num;
    int layout;
    public interface OnGameChangedListener {
        void gameChanged(int mode, int num, Vector2f[] quadPoints);
    }

    /**
     * 构造对话框
     *
     * @param context  上下文
     * @param listener 监听
     */
    public GameSetDialog(Context context, OnGameChangedListener listener, int layout) {
        super(context);
        this.layout = layout;
        this.mListener = listener;
        mode = 1;
        num = 3;
        quadPoints = new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("游戏模式设置");
        if(layout == 1)
            setContentView(R.layout.dialog_game_set);
        else
            setContentView(R.layout.dialog_game_set2);
        btnCube = (Button) findViewById(R.id.game_cube);
        btnSphere = (Button) findViewById(R.id.game_sphere);
        btnQuad = (Button) findViewById(R.id.game_quad);
        btnSure = (Button) findViewById(R.id.game_sure);
        spinner = (Spinner) findViewById(R.id.game_spinner);
        quadSetView = (QuadSetView) findViewById(R.id.game_quad_set);

        btnCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnQuad.setVisibility(View.GONE);
                btnSphere.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                mode = 1;
            }
        });

        btnQuad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCube.setVisibility(View.GONE);
                btnSphere.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                quadSetView.setVisibility(View.VISIBLE);
                mode = 2;
            }
        });

        btnSphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnQuad.setVisibility(View.GONE);
                btnCube.setVisibility(View.GONE);
                mode = 3;
                num = 8;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num = position + 3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                num = 3;
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gameChanged(mode, num, quadPoints);
                GameSetDialog.this.dismiss();
            }
        });

        quadSetView.setPoints(quadPoints);

        quadSetView.setOnPointsChanged(new QuadSetView.OnPointsChanged() {
            @Override
            public void pointsChanged(Vector2f[] points) {
                quadPoints = points;
            }
        });

    }
}
