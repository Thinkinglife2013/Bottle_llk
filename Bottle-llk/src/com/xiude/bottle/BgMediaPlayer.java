package com.xiude.bottle;

import com.xiude.bottle.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class BgMediaPlayer {
	private static MediaPlayer m;
//	private Context context;
	
//	public LocalBinder(Context context){
//		this.context = context;
//	}
	
	public static void stopMedia() {
		m.stop();
		m.release();
	}

	public static void pauseMedia(boolean isClickPause){
		m.pause();
		BaseActivity.isPausePreMedia = true;
		BaseActivity.isAllBgMusicClickPause = isClickPause;
	}
	
	public static void pauseMedia(){
		m.pause();
		BaseActivity.isPausePreMedia = true;
	}
	
	public static void restartMedia() {
		m.start();
		BaseActivity.isPausePreMedia = false;
		BaseActivity.isAllBgMusicClickPause = false;
	}
	
	public static void startMedia(Context context){
		if(m != null){
			Log.i("media", "media = null");
			m.release();
		}
		Log.i("media", "startMedia");
		m = new MediaPlayer();
		m.reset();

		m = MediaPlayer.create(context, R.raw.bg);
		m.setLooping(true);
		m.start(); 
	}
}
