package com.lzan13.main;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class ConstantQuantity {
	
	private static Context mContext;
	
	public static final int MENU_STATE_CLOSE = 0;
	public static final int MENU_STATE_OPEN = 1;
	
	public static final int DURATION_TIME = 500; 	//滑动持续时间
	
	public static int screenWidth; // 屏幕宽度
	public static int screenHeight; // 屏幕高度（不包含状态栏）
	public static float screenDensity;	//屏幕密度
	
	
	
	public static void setScreenSize(int width, int height, float density){
		screenWidth = width;
		screenHeight = height;
		screenDensity = density;
	}
	
}
