package com.lzan13.main;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lzan13.fragment.MenuFragment;
import com.lzan13.fragment.SlidFragment;
import com.lzan13.slidview.MySlidView;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.WindowManager;



public class MainActivity extends SherlockFragmentActivity{
	
	private MySlidView mSlidView;
	private MenuFragment menuFragment;
	private SlidFragment slidFragment;
	
	private ActionBar actionBar;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		init();
		
	}
	
	private void init(){
		
		getScreenSize();
		
		//设置action bar 的 logo 可点击
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		mSlidView = (MySlidView)findViewById(R.id.slid_view);
		
		mSlidView.setMenuView(getLayoutInflater().inflate(R.layout.menu_fragment, null));
		mSlidView.setSlidView(getLayoutInflater().inflate(R.layout.slid_fragment, null));
		
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		
		menuFragment = new MenuFragment();
		ft.replace(R.id.menu_fragment, menuFragment);
		
		slidFragment = new SlidFragment();
		ft.replace(R.id.slid_fragment, slidFragment);
		
		ft.commit();
		
	}

	
	
	
	public void showMenu(){
		mSlidView.openMenuView();
		if(mSlidView.menuState == ConstantQuantity.MENU_STATE_OPEN){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}else{
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}
	//菜单项被选择事件
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			showMenu();
			break;

		}
		return super.onOptionsItemSelected(item);
	}
	//创建菜单方法
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_main, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 获得屏幕分辨率
	 */
	private void getScreenSize() {
		/*
		 * 实例化DisplayMetrics对象，为得到屏幕分辨率 getWindowManager()获得现有Activity的Handler,
		 * getDefaultDisplay()获得屏幕宽高度，
		 */
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowManager = getWindow().getWindowManager();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);

		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		float density = displayMetrics.density;
		
		ConstantQuantity.setScreenSize(width, height, density);
		
	}

}
