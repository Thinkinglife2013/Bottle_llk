package com.xiude.bottle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.xiude.view.MyOnClickListener;
import com.xiude.view.MyTouchListener;

//company test
public class FirstActivity extends BaseActivity {
	public static int gameMode = 1; //当前是哪种模式
	ImageView gameMcView;
	ImageView bgMcView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_view);
        
		BgMediaPlayer.startMedia(this);
		
		//友盟自動更新 begin
		 //如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
			UmengUpdateAgent.setUpdateOnlyWifi(false);
//		 	如果您同时使用了手动更新和自动检查更新，为了避免更新回调被多次调用，请加上下面这句代码
			UmengUpdateAgent.setDownloadListener(null);
			UmengUpdateAgent.update(this);
			//end
			
		final ImageView customMode = (ImageView)findViewById(R.id.custom_mode);
		customMode.setOnClickListener(new MyOnClickListener(this){

			@Override
			public void onClick(View v) {
				super.onClick(v);
			  	FirstActivity.gameMode = 1;
				Intent i = new Intent(FirstActivity.this, SelectGuanActivity.class);
				startActivity(i);
			}
		});
		
		final ImageView challegeMode = (ImageView)findViewById(R.id.challege_mode);
		challegeMode.setOnClickListener(new MyOnClickListener(this){

			@Override
			public void onClick(View v) {
				super.onClick(v);
				Toast.makeText(FirstActivity.this, R.string.mode_tip, 0).show();
			}
		});
		
		final ImageView infiniteMode = (ImageView)findViewById(R.id.infinite_mode);
		infiniteMode.setOnClickListener(new MyOnClickListener(this){

			@Override
			public void onClick(View v) {
				super.onClick(v);
				Toast.makeText(FirstActivity.this, R.string.mode_tip, 0).show();
			}
		});
		
		final ImageView mengmoMode = (ImageView)findViewById(R.id.mengmo_mode);
		mengmoMode.setOnClickListener(new MyOnClickListener(this){

			@Override
			public void onClick(View v) {
				super.onClick(v);
				Toast.makeText(FirstActivity.this, R.string.mode_tip, 0).show();
			}
		});
			
		bgMcView = (ImageView)findViewById(R.id.bg_music);
		bgMcView.setOnClickListener(new MyOnClickListener(this) {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				if(isAllBgMusicClickPause == true){
					isAllBgMusicClickPause = false;
					BgMediaPlayer.restartMedia();
					bgMcView.setImageResource(R.drawable.music_yes);
				}else{
					isAllBgMusicClickPause = true;
					BgMediaPlayer.pauseMedia(true);
					bgMcView.setImageResource(R.drawable.music_no);
				}
			}
		});
		
		gameMcView = (ImageView)findViewById(R.id.game_music);
		gameMcView.setOnClickListener(new MyOnClickListener(this) {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				if(isGameBgPause == true){
					isGameBgPause = false;
					gameMcView.setImageResource(R.drawable.laba_yes);
				}else{
					isGameBgPause = true;
					gameMcView.setImageResource(R.drawable.laba_no);
				}
			}
		});
		
	
	}
	
	@Override
	protected void onResume() {
		if(!BaseActivity.isAllBgMusicClickPause){
			bgMcView.setImageResource(R.drawable.music_yes);
		}else{
			bgMcView.setImageResource(R.drawable.music_no);
		}
		
		if(!isGameBgPause){
			gameMcView.setImageResource(R.drawable.laba_yes);
		}else{
			gameMcView.setImageResource(R.drawable.laba_no);
		}
		
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	

	
	@Override
	protected void onDestroy() {
		MyTouchListener.destorySound();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Dialog dialog = new AlertDialog.Builder(this)
//            .setIcon(R.drawable.icon)
            .setTitle(R.string.quit)
            .setMessage(R.string.sure_quit)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	finish();
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .create();
			dialog.show();
			return true;
		}
					
		return false;
	}
	

}
