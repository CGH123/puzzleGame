package com.example.administrator.puzzleGame.game2DModel;

/**
 * Created by Administrator on 2016-03-15.
 */
public class Person {
    private String user;
    private String pwd;

    public Person(String user, String pwd) {
        this.user = user;
        this.pwd = pwd;

    }

    public String getUser() {
        return user;
    }//标题

    public void setUser(String person) {
        this.user = person;
    }//设置标题

    public String getPwd() {
        return pwd;
    }   //内容

    public void setPwd(String pwd) {
        this.pwd = pwd;
    } //设置内容
}
