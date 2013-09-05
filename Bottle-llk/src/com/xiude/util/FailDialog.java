package com.xiude.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.xiude.bottle.FirstActivity;
import com.xiude.bottle.R;
import com.xiude.bottle.WelActivity;
import com.xiude.view.GameView;

public class FailDialog extends Dialog implements OnClickListener{

	private GameView gameview;
	private Context context;
	private int guan;
	
	public FailDialog(Context context, GameView gameview,int guan, int maxCount) {
		super(context,R.style.dialog);
		this.guan = guan;
		this.gameview = gameview;
		this.context = context;
		this.setContentView(R.layout.fail_dialog);
		
		ImageButton btn_menu = (ImageButton) findViewById(R.id.menu_imgbtn);
		ImageButton btn_replay = (ImageButton) findViewById(R.id.replay_imgbtn);
		
		btn_menu.setOnClickListener(this);
		btn_replay.setOnClickListener(this);
		this.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		switch(v.getId()){
		case R.id.menu_imgbtn:
			//是否成功或失败的标识
			WelActivity.isWinOrLose = false;
			
			((WelActivity)context).quit();
			break;
		case R.id.replay_imgbtn:
			if(FirstActivity.gameMode == 3){
				guan = 1;
			}
			gameview.startPlay(guan);
			
			//是否成功或失败的标识
			WelActivity.isWinOrLose = false;
			break;
		}
	}
}
