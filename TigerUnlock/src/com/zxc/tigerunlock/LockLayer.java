package com.zxc.tigerunlock;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class LockLayer {
	private Context mContext;
	private WindowManager mWindowManager;
	private View mLockView;
	private WindowManager.LayoutParams wmParams;
	private static LockLayer mLockLayer;
	private boolean isLocked;

	public static synchronized LockLayer getInstance(Context mContext) {
		if (mLockLayer == null) {
			mLockLayer = new LockLayer(mContext);
		}
		return mLockLayer;
	}

	public LockLayer(Context mContext) {
		this.mContext = mContext;
		init();
	}

	private void init() {
		isLocked = false;
		// 获取WindowManager
		mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService("window");
		// 设置LayoutParams(全局变量）相关参数
		wmParams = new WindowManager.LayoutParams();
		wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;//关键部分
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		// 设置Window flag
//		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		wmParams.width = WindowManager.LayoutParams.FILL_PARENT;
		wmParams.height = WindowManager.LayoutParams.FILL_PARENT;
		wmParams.flags = 1280;
	}

	public synchronized void lock() {
		if (mLockView != null && !isLocked) {
			mWindowManager.addView(mLockView, wmParams);
		}
		isLocked = true;
	}

	public synchronized void unlock() {
		if (mWindowManager != null && isLocked) {
			mWindowManager.removeView(mLockView);
		}
		isLocked = false;
	}

	public synchronized void update() {
		if (mLockView != null && !isLocked) {
			mWindowManager.updateViewLayout(mLockView, wmParams);
		}
	}

	public synchronized void setLockView(View v) {
		mLockView = v;
	}
}
