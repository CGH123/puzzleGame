package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.puzzleGame.R;

import java.util.List;

import com.example.administrator.puzzleGame.sqlServer.GameDB;
import com.example.administrator.puzzleGame.sqlServer.sqlserver;
import com.example.administrator.puzzleGame.game2DModel.Person;

public class MainActivity extends Activity {
    private EditText et_user;
    private EditText et_pwd;
    private sqlserver sql;
    static String user;
    static String pwd;
    private CheckBox checkBox;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_user = (EditText) findViewById(R.id.et_u);
        et_pwd = (EditText) findViewById(R.id.et_p);
        checkBox = (CheckBox) findViewById(R.id.rem_user);
        if (!GameDB.sys_init) {
            GameDB.mImageIds.add(R.drawable.dongman0);
            GameDB.mImageIds.add(R.drawable.mm0);
            GameDB.mImageIds.add(R.drawable.mm1);
            GameDB.sys_init = true;
        }
        pref = getSharedPreferences("androidpuzzle_prferences"
                , MODE_WORLD_WRITEABLE);
        if (!GameDB.Shared_Visited) {
            String mem = pref.getString("mem", "");
            System.out.println(mem);
            if (mem.equals("Yes")) {
                GameDB.user_now = pref.getString("user_now", "");
                GameDB.pwd = pref.getString("pwd_now", "");
                GameDB.checked_box = true;
                System.out.println(GameDB.user_now);
            }
            GameDB.Shared_Visited = true;
        }
        if (GameDB.checked_box) {
            checkBox.setChecked(true);
            et_user.setText(GameDB.user_now);
            et_pwd.setText(GameDB.pwd);
        } else {
            checkBox.setChecked(false);
            et_user.setText(null);
            et_pwd.setText(null);
        }
//        DBopenHelper dbOpenHelper=new DBopenHelper(getApplicationContext());
//        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
//         dbOpenHelper.onCreate(db);

    }

    public void to_log(View view) {   //进入注册界面
        Intent intent = new Intent(MainActivity.this, LogActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void log_in(View view) {   //点击登录响应事件
        Intent intent = new Intent(MainActivity.this, bef_gameActivity.class);
        sql = new sqlserver(getApplicationContext());
        List<Person> list = sql.findbysql("");
        user = et_user.getText().toString();
        pwd = et_pwd.getText().toString();
        if (!user.equals("")) {
            if (!pwd.equals("")) {
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        String user1 = list.get(i).getUser();
                        String pwd1 = list.get(i).getPwd();
                        if (user.equals(user1)) {
                            if (pwd.equals(pwd1)) {
                                // intent.putExtra("user", user);
                                startActivity(intent);

                                if (checkBox.isChecked()) {
                                    GameDB.user_now = user;
                                    GameDB.pwd = pwd;
                                    GameDB.checked_box = true;
                                    String mem;
                                    mem = pref.getString("mem", null);
                                    if (mem == null) {
                                        mem = "Yes";
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.clear();
                                        editor.commit();
                                        editor.putString("mem", mem);
                                        String user = GameDB.user_now;
                                        String pwd = GameDB.pwd;
                                        editor.putString("user_now", user);
                                        editor.putString("pwd_now", pwd);
                                        editor.commit();
                                    }


                                } else {

                                    GameDB.checked_box = false;
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.clear();
                                    editor.commit();
                                }
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                                this.finish();
                                break;
                            } else {
                                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
                                break;
                            }
                        } else {
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "本机没有账号，请注册", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "请输入账号", Toast.LENGTH_LONG).show();
        }
    }

}

