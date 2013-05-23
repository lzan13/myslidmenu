package com.lzan13.slidview;


import com.lzan13.main.ConstantQuantity;
import com.lzan13.main.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * @author Administrator
 * 
 */
public class MySlidView extends RelativeLayout {

	private View mMenuView;
	private View mSlidView;

	private RelativeLayout bgShade;

	private Context mContext;

	private float mTouchSlop;

	private Scroller mScroller; // 滑动控制
	private VelocityTracker mVelocityTracker; // 用于判断甩动手势
	private static final int SNAP_VELOCITY = 100; // 滑动速度阀值

	private float mLastMotionX;
	private float mLastMotionY;
	private float mFirstMotionX;
	private int menuWidth; 		//侧边菜单slidmenu的宽度

	public int menuState;		//菜单状态
	private boolean mIsBeingDragged = true; // 是否在滑动

	public MySlidView(Context context) {
		super(context);
		init(context);
	}

	public MySlidView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MySlidView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * @param context
	 * 初始化
	 */
	private void init(Context context) {
		mContext = context;
		mScroller = new Scroller(context);

		menuState = ConstantQuantity.MENU_STATE_CLOSE;

		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		bgShade = new RelativeLayout(context);
	}

	/**
	 * @param menu
	 * @param slid
	 */
	public void addViews(View menu, View slid) {
		setMenuView(menu);
		setSlidView(slid);
	}

	/**
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldX = mSlidView.getScrollX();
				int x = mScroller.getCurrX();
				if (oldX != x) {
					if (mSlidView != null) {
						mSlidView.scrollTo(mScroller.getCurrX(), 0);
						bgShade.scrollTo(x + 10, 0);//
					}
				}
				invalidate();
			}
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	/**
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 * 拦截手势，在处理触屏操作之前先拦截下触屏事件进行判断之后的操作！
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		menuWidth = getMenuViewWidth();
		int action = event.getAction();

		float x = event.getX();
		float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mFirstMotionX = x;
			mLastMotionX = x;
			mLastMotionY = y;
			mIsBeingDragged = false;
			
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaX = mLastMotionX - x;
			float deltaY = mLastMotionY - y;
			if (Math.abs(deltaX) > mTouchSlop
					&& Math.abs(deltaX) > Math.abs(deltaY)) {
				float oldScrollX = mSlidView.getScrollX();
				if (oldScrollX <= 0) {
					mIsBeingDragged = true;
					mLastMotionX = x;
				} else {
					if (deltaX < 0) {
						mIsBeingDragged = true;
						mLastMotionX = x;
					}
				}
			}
			if (menuState == ConstantQuantity.MENU_STATE_OPEN) {
				System.out.println(mFirstMotionX + " ; "
						+ mSlidView.getScrollX());
				if (mFirstMotionX >= 0
						&& mFirstMotionX < Math.abs(mSlidView.getScrollX())) {
					mIsBeingDragged = false;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (menuState == ConstantQuantity.MENU_STATE_OPEN) {
				if(mFirstMotionX >= menuWidth && mLastMotionX >= menuWidth){
					openMenuView();
				}
			}
			break;
		}	
		return mIsBeingDragged;
	}

	/**
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent) 
	 * 手势处理
	 * 这里去处理触屏操作
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
			mVelocityTracker.addMovement(event);
		}

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			System.out.println("mSlidView-scrollX-->" + mSlidView.getScrollX());
			System.out.println("mMenuView-Width-->" + getMenuViewWidth());
			if (mSlidView.getScrollX() == -getMenuViewWidth()
					&& mLastMotionX < getMenuViewWidth()) {
				// return false;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsBeingDragged) {
				float delateX = mLastMotionX - x;
				mLastMotionX = x;
				if (!(mSlidView.getScrollX() < 0)) {
					if (delateX > 0) {
						break;
					}
				}

				if ((mSlidView.getScrollX() <= 0)
						&& (mSlidView.getScrollX() >= -(ConstantQuantity.screenWidth - ConstantQuantity.screenDensity * 50))) {
					mSlidView.scrollBy((int) delateX, 0);
				}

				System.out.println("delateX--->" + delateX);
				System.out.println("mSlidView.ScrollX--->"
						+ mSlidView.getScrollX());
			}

			if (mSlidView.getScrollX() >= 0) {
				return false;
			}

			if (mSlidView != null) {
				bgShade.scrollTo(mSlidView.getScrollX() + 10, 0);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}
			//当离开屏幕时根据滑动的速度来判断菜单是否打开或关闭
			showVelocityMenuView(velocityX);
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}

		return true;
	}

	/**
	 * @param view
	 * 设置侧边菜单布局
	 */
	public void setMenuView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		addView(view, behindParams);
		mMenuView = view;

	}

	/**
	 * @param view
	 * 设置中间滑动布局 设置中间滑动布局的同时，增加一个背景布局，用来显示侧边阴影！增加层级立体效果
	 */
	public void setSlidView(View view) {
		LayoutParams bgShadeParams = new LayoutParams(
				ConstantQuantity.screenWidth, ConstantQuantity.screenHeight);
		bgShadeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		bgShade.setLayoutParams(bgShadeParams);

		LayoutParams bgParams = new LayoutParams(ConstantQuantity.screenWidth,
				ConstantQuantity.screenHeight - 48);
		bgParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		View bgShadeContent = new View(mContext);

		bgShadeContent.setBackgroundResource(R.drawable.view_left_bg);

		bgShade.removeAllViews();
		bgShade.addView(bgShadeContent, bgParams);
		if (getChildCount() > 1) {
			removeViewAt(1);
			removeViewAt(1);
		}

		addView(bgShade, bgParams);

		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(view, aboveParams);
		mSlidView = view;
		mSlidView.bringToFront();
		System.out.println(getChildCount());
	}

	/**
	 * @return 获得菜单的宽度
	 */
	private int getMenuViewWidth() {
		if (mMenuView == null) {
			return 0;
		}
		return mMenuView.getWidth();
	}

	
	/**
	 * @param velocity
	 * 判断菜单的打开或关闭
	 */
	private void showVelocityMenuView(int velocity) {
		if (velocity > 0) {
			if (velocity > SNAP_VELOCITY) {
				smoothScrollTo(-(menuWidth + mSlidView.getScrollX()));
				setMenuState();
			} else {
				if (mSlidView.getScrollX() > -ConstantQuantity.screenWidth / 2) {
					smoothScrollTo(-mSlidView.getScrollX());
				} else {
					smoothScrollTo(-(menuWidth + mSlidView.getScrollX()));
					setMenuState();
				}
			}
		}

		if (velocity < 0) {
			if (velocity < -SNAP_VELOCITY) {
				smoothScrollTo(-mSlidView.getScrollX());
				setMenuState();
			} else {
				if (mSlidView.getScrollX() > -ConstantQuantity.screenWidth / 2) {
					smoothScrollTo(-mSlidView.getScrollX());
				} else {
					smoothScrollTo(-(menuWidth + mSlidView.getScrollX()));
					setMenuState();
				}
			}
		}

		if (velocity == 0) {
			if (mSlidView.getScrollX() > -(ConstantQuantity.screenWidth - ConstantQuantity.screenDensity * 50) / 2) {
				smoothScrollTo(-mSlidView.getScrollX());
			} else {
				smoothScrollTo(-(menuWidth + mSlidView.getScrollX()));
				setMenuState();
			}
		}
	}
	
	/**
	 * 去打开或者关闭菜单界面
	 */
	public void openMenuView() {
		menuWidth = getMenuViewWidth();
		if (mSlidView.getScrollX() == 0) {
			// mMenuView.setVisibility(View.VISIBLE);
			smoothScrollTo(-menuWidth);
			setMenuState();
		} else if (mSlidView.getScrollX() == -menuWidth) {
			smoothScrollTo(menuWidth);
			setMenuState();
		}
	}
	
	/**
	 * 真正的实现打开与关闭的滑动效果
	 * @param distanceX
	 */
	private void smoothScrollTo(int distanceX) {
		mScroller.startScroll(mSlidView.getScrollX(), 0, 
				distanceX, 0, ConstantQuantity.DURATION_TIME);
		invalidate();
	}
	
	/**
	 * 设置菜单打开或关闭状态
	 */
	private void setMenuState() {
		if (menuState == ConstantQuantity.MENU_STATE_CLOSE) {
			menuState = ConstantQuantity.MENU_STATE_OPEN;
			// mSlidView.setEnabled(false);
			System.out.println("菜单将打开");
		} else {
			menuState = ConstantQuantity.MENU_STATE_CLOSE;
			// mSlidView.setEnabled(true);
			System.out.println("菜单将关闭");
		}
	}

	

}