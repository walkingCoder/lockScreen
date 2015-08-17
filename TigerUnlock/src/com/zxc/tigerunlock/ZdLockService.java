package com.zxc.tigerunlock;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ZdLockService extends Service {

	private static String TAG = "ZdLockService";
	private Intent zdLockIntent = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();

		zdLockIntent = new Intent(ZdLockService.this, MainActivity.class);
		zdLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		/* 注册广播 */
		IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");
		ZdLockService.this.registerReceiver(mScreenOnReceiver, mScreenOnFilter);

		/* 注册广播 */
		IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
		ZdLockService.this.registerReceiver(mScreenOffReceiver, mScreenOffFilter);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onDestroy");
		return Service.START_STICKY;

	}

	public void onDestroy() {
		super.onDestroy();
		ZdLockService.this.unregisterReceiver(mScreenOnReceiver);
		ZdLockService.this.unregisterReceiver(mScreenOffReceiver);
		Log.i(TAG, "onDestroy");
		// 在此重新启动
		startService(new Intent(ZdLockService.this, ZdLockService.class));
	}

	private KeyguardManager mKeyguardManager = null;
	private KeyguardManager.KeyguardLock mKeyguardLock = null;
	// 屏幕变亮的广播,我们要隐藏默认的锁屏界面
	private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
				mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
				mKeyguardLock = mKeyguardManager.newKeyguardLock("");
				mKeyguardLock.disableKeyguard();
			}
		}

	};

	// 屏幕变暗的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
	private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.intent.action.SCREEN_OFF")) {
				startActivity(zdLockIntent);
			}
		}
	};
}
