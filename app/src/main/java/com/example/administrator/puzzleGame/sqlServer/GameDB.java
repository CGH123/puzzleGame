package com.example.administrator.puzzleGame.sqlServer;

import com.example.administrator.puzzleGame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-03-15.
 */
public class GameDB {
    /**
     * 游戏难度变量:简单
     */
    public static final int Diffcult_easy = 0;
    /**
     * 游戏难度变量:一般
     */
    public static final int Diffcult_normal = 1;
    /**
     * 游戏难度变量:困难
     */
    public static final int Diffcult_hard = 2;
    /**
     * 游戏难度变量:困难
     */
    public static int Diffcult_choice = 0;
    /**
     * 记录游戏的状态
     */
    public static int game_state = 0;
    /**
     * 正在游戏中
     */
    public static final int game_playing = 0;
    /**
     * 游戏完成
     */
    public static final int game_win = 1;
    /**
     * 游戏背景音乐选择
     */
    public static int musicSelect = 10000;
    /**
     * 记录当前的用户和相应的密码
     */
    public static String user_now;
    /**
     * 暂时记住当前的密码
     */
    public static String pwd;
    /**
     * 记住密码
     */
    public static boolean checked_box = false;
    /**
     * 判断是否访问过首选项
     */
    public static boolean Shared_Visited = false;
    /**
     * 定义本地静态图片资源
     */
    public static List<Integer> mImageIds = new ArrayList<Integer>();
    public static Integer[] mImage_sou = {
            R.drawable.dongman0,
            R.drawable.mm0,
            R.drawable.mm1,
    };
    /**
     * 静态图片资源的选择
     */
    public static int pic_res_choice = 2;
    /**
     * 系统初始化
     */
    public static boolean sys_init = false;

}
