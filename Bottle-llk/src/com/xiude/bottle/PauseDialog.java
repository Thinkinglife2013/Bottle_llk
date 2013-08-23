package com.xiude.bottle;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.xiude.view.GameView;
import com.xiude.view.GameView.RefreshTime;

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
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.menu_imgbtn:
	/*		Dialog dialog = new AlertDialog.Builder(context)
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
                	restartGame();
                }
            })
            .create();
			dialog.show();*/
			((WelActivity)context).quit();
			break;
		case R.id.replay_imgbtn:
			gameview.startPlay(guan);
			gameview.setVisibility(View.VISIBLE);
			break;
		case R.id.play:
			restartGame();
			break;
		}
		this.dismiss();
	}
	
	private void restartGame(){
		gameview.setVisibility(View.VISIBLE);
		
		if(gameview.player != null){
			gameview.player.start();
		}
		gameview.startTimer();
		gameview.refreshTime = gameview.new RefreshTime();
		gameview.refreshTime.start();
	}
}
