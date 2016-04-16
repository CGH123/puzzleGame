package com.example.administrator.puzzleGame.game2DModel;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * 设置屏幕属性
 * 获取屏幕的高与宽
 * */
public class ClassSetScreenWH {
	/** 窗口的宽度*/
	private int screenWidth = 0;
	/**  窗口的高度*/
	private int screenHeight = 0;
	public ClassSetScreenWH(Activity activity){

		// 不显示程序的标题栏
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 不显示系统的标题栏
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 定义DisplayMetrics对象
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 窗口的宽度
		screenWidth = dm.widthPixels;
		// 窗口的高度
		screenHeight = dm.heightPixels;		

	}
	/**
	 * 获取屏幕的宽
	 * */
	public int getScreenWidth() {
		return screenWidth;
	}
	/**
	 * 获取屏幕的高
	 * */
	public int getScreenHeight() {
		return screenHeight;
	}

}
