package com.example.administrator.puzzleGame.sqlServer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.puzzleGame.game2DModel.Person;

/**
 * Created by Administrator on 2016-03-15.
 */
public class sqlserver {

    private Context context;

    public sqlserver(Context context){
        this.context=context;
    }


    public void savezc(String user,String pwd){
        DBopenHelper dbOpenHelper=new DBopenHelper(context);
        SQLiteDatabase database=dbOpenHelper.getReadableDatabase();
        String sql="insert into person (user,pwd) values('"+user+"','"+pwd+"')";
        database.execSQL(sql);
    }

    public List<Person> findbysql(String sql){
        List<Person> list=new ArrayList<Person>();
        DBopenHelper dbOpenHelper=new DBopenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from person ", null);

        while (cursor.moveToNext()) {

            String person1=cursor.getString(cursor.getColumnIndex("user"));
            String pwd1=cursor.getString(cursor.getColumnIndex("pwd"));
            Person personlist=new Person(person1,pwd1);
            list.add(personlist);
        }

        return list;

    }

}
