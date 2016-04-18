package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.util.WifiUtils;

/**
 * Created by HUI on 2016-04-03.
 */
public class ConnectModeActivtiy extends Activity {

    private WifiUtils temp;

    Button wifi;
    Button bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        wifi = (Button) findViewById(R.id.btn_wifi);
        bluetooth = (Button) findViewById(R.id.btn_bluetooth);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectModeActivtiy.this, WifiapActivity.class);
                startActivity(intent);
            }
        });

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Indo 跳转到另外一个蓝牙页面
                Intent intent = new Intent(ConnectModeActivtiy.this, Game3DActivity.class);
                startActivity(intent);

            }
        });

    }

}
