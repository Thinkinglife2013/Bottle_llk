package com.xiude.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xiude.bottle.FirstActivity;
import com.xiude.bottle.R;
import com.xiude.bottle.SelectGuanActivity;
import com.xiude.bottle.SoundPlay;


public class MyItemClickListener implements OnItemClickListener {
	public static SoundPlay soundPlay; //²¥·Åµã»÷ÉùÒô
	public static int CLICK_SOUND;
//	private Context context;
	
	public MyItemClickListener(Context context){
//		this.context = context;
		initSound(context);
	}
	
	public static void initSound(Context context){
		if(soundPlay == null){
			soundPlay = new SoundPlay();
	        soundPlay.initSounds(context);
	        soundPlay.loadSfx(context, R.raw.click, CLICK_SOUND);
		}
	}
	
	public static void destorySound(){
		 soundPlay = null;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3) {
		soundPlay.play(CLICK_SOUND, 0);
//	    postOnItemClick();
	}
	
//	public abstract void postOnItemClick();

}
