package com.xiude.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.xiude.bottle.R;
import com.xiude.bottle.SoundPlay;


public class MyClickListener implements OnClickListener {
	public static SoundPlay soundPlay; //²¥·Åµã»÷ÉùÒô
	public static int CLICK_SOUND;
	
	public MyClickListener(Context context){
		initSound(context);
	}
	
	public static void initSound(Context context){
		 soundPlay = new SoundPlay();
	        soundPlay.initSounds(context);
	        soundPlay.loadSfx(context, R.raw.click, CLICK_SOUND);
	}
	
	public static void destorySound(){
		 soundPlay = null;
	}
	
	@Override
	public void onClick(View v) {
		soundPlay.play(CLICK_SOUND, 0);
		
	}

}
