package com.xiude.bottle;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xiude.view.GameView;
import com.xiude.view.MyTouchListener;

public class PauseDialog extends Dialog implements OnClickListener{

	private GameView gameview;
	private Context context;
	private int guan;
	
	public PauseDialog(Context context, GameView gameview, int guan) {
		super(context,R.style.dialog);
		
		this.guan = guan;
		this.gameview = gameview;
		this.context = context;
//		this.setContentView(R.layout.dialog_view);
		this.setContentView(R.layout.pause_dialog);
		
		FrameLayout dialog = (FrameLayout)findViewById(R.id.view_dialog);
		
//		TextView text_msg = (TextView) findViewById(R.id.text_message);
//		TextView text_time = (TextView) findViewById(R.id.text_time);
		ImageButton btn_menu = (ImageButton) findViewById(R.id.menu_imgbtn);
//		ImageButton btn_next = (ImageButton) findViewById(R.id.next_imgbtn);
		ImageButton btn_replay = (ImageButton) findViewById(R.id.replay_imgbtn);
		ImageButton btn_play = (ImageButton) findViewById(R.id.play);
//		text_msg.setText(msg);
//		text_time.setText(text_time.getText().toString().replace("$", String.valueOf(time)));
		btn_menu.setOnClickListener(this);
//		btn_next.setOnClickListener(this);
		btn_replay.setOnClickListener(this);
		btn_play.setOnClickListener(this);
		this.setCancelable(false);
		
		
		//两个音乐按钮
		final ImageView bgMcView = (ImageView)findViewById(R.id.bg_music);
		bgMcView.setOnTouchListener(new MyTouchListener(context) {
			
			@Override
			public void postOnTouch() {
				if(BaseActivity.isAllBgMusicClickPause == true){
					BaseActivity.isAllBgMusicClickPause = false;
//					BgMediaPlayer.restartMedia();
					
					bgMcView.setImageResource(R.drawable.music_yes);
				}else{
					BaseActivity.isAllBgMusicClickPause = true;
					
//					BgMediaPlayer.pauseMedia(true);
					bgMcView.setImageResource(R.drawable.music_no);
				}
			}
		});
		
		final ImageView gameMcView = (ImageView)findViewById(R.id.game_music);
		gameMcView.setOnTouchListener(new MyTouchListener(context) {
			
			@Override
			public void postOnTouch() {
				if(BaseActivity.isGameBgPause == true){
					BaseActivity.isGameBgPause = false;
					GameView.initSound(PauseDialog.this.context);
					gameMcView.setImageResource(R.drawable.laba_yes);
				}else{
					BaseActivity.isGameBgPause = true;
					GameView.soundPlay = null;
					gameMcView.setImageResource(R.drawable.laba_no);
				}
			}
		});
		
		if(!BaseActivity.isAllBgMusicClickPause){
			bgMcView.setImageResource(R.drawable.music_yes);
		}else{
			bgMcView.setImageResource(R.drawable.music_no);
		}
		
		if(!BaseActivity.isGameBgPause){
			gameMcView.setImageResource(R.drawable.laba_yes);
		}else{
			gameMcView.setImageResource(R.drawable.laba_no);
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.menu_imgbtn:
			((WelActivity)context).quit();
			break;
		case R.id.replay_imgbtn:
			gameview.startPlay(guan);
			rePlayMusic();
			break;
		case R.id.play:
			restartGame();
			break;
		}
		this.dismiss();
	}
	
	private void restartGame(){
		rePlayMusic();
		gameview.startTimer();
		gameview.refreshTime = gameview.new RefreshTime();
		gameview.refreshTime.start();
	}
	
	private void rePlayMusic(){
		gameview.setVisibility(View.VISIBLE);
		
		if(WelActivity.player != null && !BaseActivity.isAllBgMusicClickPause){
			try{
				WelActivity.player.start();
			}catch(Exception e){
				e.printStackTrace();
				WelActivity.player = MediaPlayer.create(context, R.raw.back2new); 
				WelActivity.player.setLooping(true);//设置循环播放
				WelActivity.player.start();
			}
		}else if(WelActivity.player == null && !BaseActivity.isAllBgMusicClickPause){
			WelActivity.player = MediaPlayer.create(context, R.raw.back2new); 
			WelActivity.player.setLooping(true);//设置循环播放
			WelActivity.player.start();
		}
	}
}
