package com.zxc.tigerunlock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static String TAG = "QINZDLOCK";
	private TextView tvTime, tvDate;
	private ImageView ivHint;
	private AnimationDrawable animArrowDrawable = null;
	public static int MSG_LOCK_SUCESS = 0x123;
	public static int UPDATE_TIME = 0x234;
	private boolean isTime = true;
	private LockLayer lockLayer;
	private View lock;
//	private KeyguardManager mKeyguardManager = null;
//	private KeyguardManager.KeyguardLock mKeyguardLock = null;
	/**
	 * 消息处理
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (MSG_LOCK_SUCESS == msg.what) {
				lockLayer.unlock();
				finish(); // 锁屏成功时，结束我们的Activity界面
			}
			if (UPDATE_TIME == msg.what) {
				Date date = (Date) msg.obj;
				String timeFormat = getChangeTimeFormat(date);
				String dateFormat = getChangeDateFormat(date);
				String weekFormat = getChangeWeekFormat(date);
				tvTime.setText(timeFormat);
				tvDate.setText(dateFormat + "\t" + weekFormat);
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("test", "onCreate()");
//		/* 设置全屏，无标题 */
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		mKeyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//		mKeyguardLock = mKeyguardManager.newKeyguardLock("");
//		mKeyguardLock.disableKeyguard();
		//
		lock = View.inflate(this, R.layout.main, null);
		lockLayer = new LockLayer(this);
		lockLayer.setLockView(lock);
		lockLayer.lock();
		PullDoorView.setMainHandler(mHandler);
		initView();
		startService(new Intent(MainActivity.this, ZdLockService.class));
	}

	private void initView() {
		tvTime = (TextView) lock.findViewById(R.id.tv_time);
		tvDate = (TextView) lock.findViewById(R.id.tv_date);
		ivHint = (ImageView) lock.findViewById(R.id.iv_hint);
		ivHint.setImageResource(R.anim.slider_tip_anim);
		animArrowDrawable = (AnimationDrawable) ivHint.getDrawable();
		animArrowDrawable.start();
		getNewTime();
		// // 获取壁纸图片
		// WallpaperManager wallpaperManager =
		// WallpaperManager.getInstance(this);
		// Drawable wallpaperDrawable = wallpaperManager.getDrawable();
		// //处理图片
		// BitmapDrawable bd= (BitmapDrawable) wallpaperDrawable;
		// Bitmap bitmap = bd.getBitmap();
		// Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, 480,
		// 800, false);
		// BitmapDrawable resultBD = new BitmapDrawable(createScaledBitmap);
		// FrameLayout fl = (FrameLayout) lock.findViewById(R.id.mainFrame);
		// fl.setBackgroundDrawable(resultBD);
	}

	private void getNewTime() {
		new Thread() {
			public void run() {
				while (isTime) {
					Log.i(TAG, "循环");
					Date date = new Date();
					Message msg = new Message();
					msg.obj = date;
					msg.what = UPDATE_TIME;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	/**
	 * 转换日期格式为yyyy-MM-dd
	 */
	public static String getChangeDateFormat(Date date) {
		String str = null;
		if (date != null && !"".equals(date)) {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			str = sd.format(date);
		}
		return str;
	}

	/**
	 * 转换日期格式为HH:mm
	 */
	public static String getChangeTimeFormat(Date date) {
		String str = null;
		if (date != null && !"".equals(date)) {
			SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
			str = sd.format(date);
		}
		return str;
	}

	/**
	 * 获取当前为星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getChangeWeekFormat(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hour = c.get(Calendar.DAY_OF_WEEK);
		String str = "" + hour;
		if ("1".equals(str)) {
			str = "星期日";
		} else if ("2".equals(str)) {
			str = "星期一";
		} else if ("3".equals(str)) {
			str = "星期二";
		} else if ("4".equals(str)) {
			str = "星期三";
		} else if ("5".equals(str)) {
			str = "星期四";
		} else if ("6".equals(str)) {
			str = "星期五";
		} else if ("7".equals(str)) {
			str = "星期六";
		}
		return str;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		isTime = true;
		// getNewTime();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		isTime = false;
	}

	protected void onDestory() {
		super.onDestroy();
		Log.i(TAG, "onDestory");
		isTime = false;
	}

	// // 通过延时控制当前绘制bitmap的位置坐标
	// private Runnable AnimationDrawableTask = new Runnable() {
	//
	// public void run() {
	// mHandler.postDelayed(AnimationDrawableTask, 300);
	// }
	// };

	// // 屏蔽掉Home键
	// public void onAttachedToWindow() {
	// this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
	// super.onAttachedToWindow();
	// }
	//
	// // 屏蔽掉Back键
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() ==
	// KeyEvent.KEYCODE_HOME) {
	// return true;
	// } else {
	// return super.onKeyDown(keyCode, event);
	//
	// }
	//
	// }
}