package com.xiude.util;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiude.bottle.Constants;
import com.xiude.bottle.FirstActivity;
import com.xiude.bottle.R;
import com.xiude.bottle.SelectModeActivity;
import com.xiude.bottle.WelActivity;
import com.xiude.view.GameView;

public class WinDialog extends Dialog implements OnClickListener{

	private GameView gameview;
	private Context context;
	private int guan;
	
	public WinDialog(Context context, GameView gameview, int time, int integral, int guan, int maxCount, SeekBar seekBar) {
		super(context,R.style.dialog);
		this.guan = guan;
		this.gameview = gameview;
		this.context = context;
		this.setContentView(R.layout.win_dialog);
		
//		TextView text_msg = (TextView) findViewById(R.id.text_message);
//		TextView text_time = (TextView) findViewById(R.id.text_time);
		ImageButton btn_menu = (ImageButton) findViewById(R.id.menu_imgbtn);
		ImageButton btn_next = (ImageButton) findViewById(R.id.next_imgbtn);
		ImageButton btn_replay = (ImageButton) findViewById(R.id.replay_imgbtn);
		TextView integralView = (TextView) findViewById(R.id.integral);
		ImageView starTwoView = (ImageView) findViewById(R.id.star_two);
		ImageView starThreeView = (ImageView) findViewById(R.id.star_three);
		
		btn_next.setVisibility(View.VISIBLE);
		
	/*	if(maxCount == -1){
			TextView line_text = (TextView) findViewById(R.id.line_message);
			line_text.setVisibility(View.GONE);
		}else{
			TextView line_text = (TextView) findViewById(R.id.line_message);
			line_text.setVisibility(View.VISIBLE);
			line_text.setText("最大连击数："+String.valueOf(maxCount));
		}*/
		
		//计算积分
		if(SelectModeActivity.gameMode == 3){
			integralView.setText(context.getString(R.string.integral).replace("$", String.valueOf(integral)));
		}else if(SelectModeActivity.gameMode == 1){
			//经典模式，设置得分
			int score = gameview.getCustomIntegral();
			integralView.setText(String.valueOf(score));
			
			int MaxProgress = seekBar.getMax();
			int progress = seekBar.getProgress();
			
			float percent = (float)progress / (float)MaxProgress;
			
			SharedPreferences starPreference = context.getSharedPreferences("star", 0);
			if(percent > 0.5){
				starTwoView.setBackgroundResource(R.drawable.star_two);
				starThreeView.setBackgroundResource(R.drawable.star_three);
				
				starPreference.edit().putInt(guan+"star_count", 3).commit();
			}else if(percent > 0.33){
				starTwoView.setBackgroundResource(R.drawable.star_two);
				starPreference.edit().putInt(guan+"star_count", 2).commit();
			}else{
				starPreference.edit().putInt(guan+"star_count", 1).commit();
			}
		}
		
		Constants.maxCount = 0;
		Constants.lineCount = 0;
		Constants.lastTime = 0;
		
//		text_msg.setText(msg);
//		text_time.setText(text_time.getText().toString().replace("$", String.valueOf(time)));
		btn_menu.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_replay.setOnClickListener(this);
		this.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		switch(v.getId()){
		case R.id.menu_imgbtn:
			((WelActivity)context).quit();
			break;
		case R.id.replay_imgbtn:
			if(FirstActivity.gameMode == 3){
				guan = 1;
			}
			gameview.startPlay(guan);
			break;
		case R.id.next_imgbtn:
			gameview.startNextPlay(guan+1);
			break;
		}
	}
}
