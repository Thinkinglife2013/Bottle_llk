package com.xiude.bottle;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	public static boolean isPausePreMedia = false;
	private boolean isLaunch = false;
//	public static LocalBinder mBinder;
	public static boolean isFront = false;
	public static boolean isAllBgMusicClickPause = false; //µ„ª˜Õ£÷π±≥æ∞“Ù¿÷
	
	public static boolean isGameBgPause = false;
//	public static boolean isFirstCreate = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		isFront = true;
		super.onResume();
		if (isPausePreMedia == true && isAllBgMusicClickPause == false) {
//			mBinder.restartMedia();
			BgMediaPlayer.restartMedia();
		}
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		isFront = false;
	
		super.onPause();
	}
	
	@Override
	protected void onStop() {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(isFront == false){
						Log.i("base", "pause");
//						mBinder.pauseMedia(false);
						BgMediaPlayer.pauseMedia(isAllBgMusicClickPause);
					}
				}
			}).start();
			
		super.onStop();
	}



}
