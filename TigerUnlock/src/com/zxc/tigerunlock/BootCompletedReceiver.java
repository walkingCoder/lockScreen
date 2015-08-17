package com.zxc.tigerunlock;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ¿ª»ú¹ã²¥
 * 
 * @author ZhangXuanChen
 * @date 2014-3-20
 * @package com.qin.zdlock - BootCompletedReceiver.java
 * @description
 */
public class BootCompletedReceiver extends BroadcastReceiver {
	private KeyguardManager mKeyguardManager = null;
	private KeyguardManager.KeyguardLock mKeyguardLock = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		mKeyguardLock = mKeyguardManager.newKeyguardLock("");
		mKeyguardLock.disableKeyguard();
		intent = new Intent(context, ZdLockService.class);
		context.startService(intent);
	}
}
