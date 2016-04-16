package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.puzzleGame.R;

import com.example.administrator.puzzleGame.sqlServer.sqlserver;

/**
 * Created by Administrator on 2016-03-15.
 */
public class LogActivity extends Activity {
    private int screenWidth;
    private int screenHeight;
    private sqlserver sql;

    private EditText et_1;
    private EditText et_2;
    private String user;
    private String pwd;
    Bitmap bit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;
        sql = new sqlserver(getApplicationContext());
        et_1=(EditText)findViewById(R.id.et_user);
        et_2=(EditText)findViewById(R.id.et_pwd);

    }
   public  void back(View view){
       Intent intent = new Intent(LogActivity.this,MainActivity.class);
       startActivity(intent);
       this.finish();
   }
    public void log_save(View view){  //view 有着amzing的作用

        try {
            user = et_1.getText().toString();
            pwd = et_2.getText().toString();
            if (user.equals("") || pwd.equals("")) {
                Toast.makeText(getApplicationContext(), "账号密码不能为空", Toast.LENGTH_LONG).show();

                //Intent intent = new Intent(LogActivity.this,bef_gameActivity.class);
               // startActivity(intent);
                this.finish();
            }else{
                sql.savezc(user, pwd);
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LogActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    catch (Exception e){

            Toast.makeText(getApplicationContext(), "账号已经存在", Toast.LENGTH_LONG).show();

    }
    }

}
