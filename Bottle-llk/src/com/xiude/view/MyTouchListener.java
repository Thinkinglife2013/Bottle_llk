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

import com.xiude.bottle.FirstActivity;
import com.xiude.bottle.R;
import com.xiude.bottle.SelectGuanActivity;
import com.xiude.bottle.SoundPlay;


public abstract class MyTouchListener implements OnTouchListener {
	public static SoundPlay soundPlay; //播放点击声音
	public static int CLICK_SOUND;
	private Context context;
	
	public MyTouchListener(Context context){
		this.context = context;
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
	
	private Drawable upDraw = null;
//	private Drawable downDraw = null;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		   int iAction = event.getAction();  
	        if (iAction == MotionEvent.ACTION_DOWN) {   // 按下  
	        	Log.i("onTouch", "down");
	        	soundPlay.play(CLICK_SOUND, 0);
	        	Matrix matrix=new Matrix();
	        	matrix.postScale(0.9f, 0.9f);
	        	
	        	if(v.getBackground() != null){
	        		upDraw = v.getBackground();
	        	}
	        	
	        	Bitmap bitmap = drawableToBitmap(upDraw);
	        	Bitmap temp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
	        	BitmapDrawable bd= new BitmapDrawable(context.getResources(), temp); 
	        	v.setBackgroundDrawable(bd);
	        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
	        	Log.i("onTouch", "up");
	        	v.setBackgroundDrawable(upDraw);
	        	postOnTouch();
	        }
		return true;
	}
	
	
	private Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap  
	{  
	    int width = drawable.getIntrinsicWidth();// 取drawable的长宽  
	    int height = drawable.getIntrinsicHeight();  
	    Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;// 取drawable的颜色格式  
	    Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap  
	    Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布  
	    drawable.setBounds(0, 0, width, height);  
	    drawable.draw(canvas);// 把drawable内容画到画布中  
	    return bitmap;  
	}  
	
	public abstract void postOnTouch();

}
