package com.xiude.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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
	
	public WinDialog(Context context, GameView gameview, int time, int integral, int guan, int maxCount) {
		super(context,R.style.dialog);
		this.guan = guan;
		this.gameview = gameview;
		this.context = context;
		this.setContentView(R.layout.win_dialog);
		
//		TextView text_msg = (TextView) findViewById(R.id.text_message);
		TextView text_time = (TextView) findViewById(R.id.text_time);
		ImageButton btn_menu = (ImageButton) findViewById(R.id.menu_imgbtn);
		ImageButton btn_next = (ImageButton) findViewById(R.id.next_imgbtn);
		ImageButton btn_replay = (ImageButton) findViewById(R.id.replay_imgbtn);
		TextView integralView = (TextView) findViewById(R.id.integral);
		
		btn_next.setVisibility(View.VISIBLE);
		
		if(maxCount == -1){
			TextView line_text = (TextView) findViewById(R.id.line_message);
			line_text.setVisibility(View.GONE);
		}else{
			TextView line_text = (TextView) findViewById(R.id.line_message);
			line_text.setVisibility(View.VISIBLE);
			line_text.setText("最大连击数："+String.valueOf(maxCount));
		}
		
		//计算积分
		if(SelectModeActivity.gameMode == 3){
			integralView.setText(context.getString(R.string.integral).replace("$", String.valueOf(integral)));
		}
		
		Constants.maxCount = 0;
		Constants.lineCount = 0;
		Constants.lastTime = 0;
		
//		text_msg.setText(msg);
		text_time.setText(text_time.getText().toString().replace("$", String.valueOf(time)));
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
		/*	Dialog dialog = new AlertDialog.Builder(context)
            .setIcon(R.drawable.icon)
            .setTitle(R.string.quit)
            .setMessage(R.string.sure_quit)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	((WelActivity)context).quit();
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .create();
			dialog.show();*/
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
