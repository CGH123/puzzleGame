package com.example.administrator.puzzleGame.constant;


public class GameConstant {
    public static final int SHADOW_TEX_WIDTH = 512;
    public static final int SHADOW_TEX_HEIGHT = 512;
    //摄像机旋转速度移动阀值
    public static final float CAMERA_SPEED = 0.01F;
    //物体倍数
    public static final float SCALE = 10;
    //分割线颜色
    public static final float[] LINE_COLOR = {0.9f, 0.9f, 0.9f, 0.9f};
    //计算GLSurfaceView的宽高比
    public static float RATIO;
    //计算GLSurfaceView的宽
    public static int WIDTH;
    //计算GLSurfaceView的高
    public static int HEIGHT;
    //水面换帧线程工作标志
    public static boolean threadFlag = true;

    public static String NAME;
    public static String PHONE;
    public static String IP;
}
