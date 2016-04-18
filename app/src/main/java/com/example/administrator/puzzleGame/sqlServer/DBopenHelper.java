package com.example.administrator.puzzleGame.sqlServer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016-03-15.
 */
public class DBopenHelper extends SQLiteOpenHelper {

    public DBopenHelper(Context context) {
        super(context, "puzzle.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // db.execSQL("DROP TABLE person");
        db.execSQL("CREATE TABLE person(user varchar(20) primary key,pwd varchar(20))");
        // db.execSQL("DROP TABLE file");
        // db.execSQL("CREATE TABLE file(name varchar(20) primary key,content varchar(300),user varchar(20))");
    }

    /**
     * 修改数据库表结构的方法
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}

