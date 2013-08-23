package com.xiude.bottle;

import com.xiude.bottle.R;

import android.content.Context;
import android.media.MediaPlayer;

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
		m = new MediaPlayer();
		m.reset();// 恢复到未初始化的状�?

		m = MediaPlayer.create(context, R.raw.bg);// 读取音频
		m.setLooping(true);
		m.start(); // 播放
	}
}
