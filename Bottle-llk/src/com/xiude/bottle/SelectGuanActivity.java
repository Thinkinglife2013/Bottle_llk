package com.xiude.bottle;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiude.view.MyClickListener;

public class SelectGuanActivity extends BaseActivity implements OnClickListener,
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
	GridAdapter gridAdapter;
	int level;
	

	class GridAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;
		private List<Guan> guanList;
		private Bitmap bmSrc;
		private int level;
		private SharedPreferences starPreference;

		public GridAdapter(Context context, List<Guan> guanList, int level) {
			layoutInflater = LayoutInflater.from(context);
			this.guanList = guanList;
			bmSrc = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.number);
			
			starPreference = context.getSharedPreferences("star", 0);
			
//			if(level - 12 > 0){
//				this.level = level - 12;
//			}else if(level - 24 > 0){
//				this.level = level - 24;
//			}else{
				this.level = level;
//			}
		}

		public void setLevel(int level) {
//			if(level - 12 > 0){
//				this.level = level - 12;
//			}else if(level - 24 > 0){
//				this.level = level - 24;
//			}else{
				this.level = level;
//			}
		}

		@Override
		public int getCount() {
			return guanList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return guanList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convetView, ViewGroup arg2) {
			convetView = layoutInflater.inflate(R.layout.grid_item, null);
			ImageView firstGuan = (ImageView) convetView
					.findViewById(R.id.one_guan);
			ImageView tenGuan = (ImageView) convetView
					.findViewById(R.id.ten_guan);
			FrameLayout lockView = (FrameLayout) convetView
					.findViewById(R.id.lock);
			LinearLayout guanLayout = (LinearLayout) convetView
					.findViewById(R.id.guan_num);
			LinearLayout starGroup = (LinearLayout) convetView
					.findViewById(R.id.star_group);
			
			
			Guan guan = (Guan) getItem(position);

			String guanNum = guan.getNumStr();
			
			int starCount = starPreference.getInt(Integer.parseInt(guanNum)+"star_count", 0);
			if(starCount > 0){
				starGroup.setVisibility(View.VISIBLE);
				if(starCount >= 1){
					ImageView starOne = (ImageView) convetView
							.findViewById(R.id.star_one);
					starOne.setVisibility(View.VISIBLE);
					if(starCount >= 2){
						ImageView starTwo = (ImageView) convetView
								.findViewById(R.id.star_two);
						starTwo.setVisibility(View.VISIBLE);
						if(starCount == 3){
							ImageView starThree = (ImageView) convetView
									.findViewById(R.id.star_three);
							starThree.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			
			if(Integer.parseInt(guanNum) <= level){
				guanLayout.setVisibility(View.VISIBLE);
				lockView.setBackgroundColor(getResources().getColor(R.color.black_00_transparent));
			}else{
				guanLayout.setVisibility(View.GONE);
				lockView.setBackgroundResource(R.drawable.lock);
			}

			String[] nums;
			if (guanNum.length() == 1) {
				nums = new String[] { guanNum };
			} else {
				nums = new String[2];
				nums[0] = guanNum.charAt(0) + "";
				nums[1] = guanNum.charAt(1) + "";
			}

			if (nums.length == 1) {
				firstGuan.setVisibility(View.VISIBLE);
				tenGuan.setVisibility(View.GONE);

				int num = Integer.parseInt(nums[0]);
				Bitmap bmDst = Bitmap.createBitmap(bmSrc,
						(num - 1) * bmSrc.getWidth() / 10, 0,
						bmSrc.getWidth() / 10, bmSrc.getHeight());
				firstGuan.setImageBitmap(bmDst);
			} else {
				firstGuan.setVisibility(View.VISIBLE);
				tenGuan.setVisibility(View.VISIBLE);

				try {
					int num = Integer.parseInt(nums[0]);
					Bitmap bmDst = Bitmap.createBitmap(bmSrc,
							(num - 1) * bmSrc.getWidth() / 10, 0,
							bmSrc.getWidth() / 10, bmSrc.getHeight());
					firstGuan.setImageBitmap(bmDst);

					int num2 = Integer.parseInt(nums[1]);
					Bitmap bmDst2 = Bitmap.createBitmap(bmSrc,
							num2 - 1 == -1 ? 9 * bmSrc.getWidth() / 10
									: (num2 - 1) * bmSrc.getWidth() / 10, 0,
							bmSrc.getWidth() / 10, bmSrc.getHeight());
					tenGuan.setImageBitmap(bmDst2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return convetView;
		}
	}
	
//	ViewGroup fatherLayout; //广告的展示位

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guan);
		
		ImageView backView = (ImageView)findViewById(R.id.back);
		backView.setOnClickListener(new MyClickListener(this) {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				finish();
			}
		});
		
		//現在是哪種模式
		/*Intent intent = getIntent();
		int mode = intent.getIntExtra("gamemode", 1);
		gameMode = mode;*/
		
		
		detector = new GestureDetector(this);

		views = new ArrayList<View>();

		LayoutInflater inflater = getLayoutInflater();

		View guanPager = inflater.inflate(R.layout.guan_pager, null);
		GridView gridView = (GridView) guanPager.findViewById(R.id.guan_grid);
		List<Guan> guanList = new ArrayList<Guan>();

		for (int i = 1; i <= 12; i++) {
			Guan guan = new Guan();
			guan.setNumStr(String.valueOf(i));

			guanList.add(guan);
		}
		
		SharedPreferences levelPreference = this.getSharedPreferences("level", 0);
		
		if(FirstActivity.gameMode == 1){
			level = levelPreference.getInt("custom_level", 1);
		}else if(FirstActivity.gameMode == 2){
			level = levelPreference.getInt("challenge_level", 1);
		}
		
		gridAdapter = new GridAdapter(this, guanList, level);
		gridView.setAdapter(gridAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Guan guan = (Guan)parent.getItemAtPosition(position);
				String guanNum = guan.getNumStr();
				int num = Integer.parseInt(guanNum);
				
				if(num <= level){
					Intent i = new Intent(SelectGuanActivity.this, WelActivity.class);
					i.putExtra("num", num);
					startActivity(i);
				}
			}
			
		});
		
		views.add(guanPager);

		View guanPager2 = inflater.inflate(R.layout.guan_pager, null);
		GridView gridView2 = (GridView) guanPager2.findViewById(R.id.guan_grid);
		List<Guan> guanList2 = new ArrayList<Guan>();
		for (int i = 13; i <= 24; i++) {
			Guan guan = new Guan();
			guan.setNumStr(String.valueOf(i));

			guanList2.add(guan);
		}
		GridAdapter gridAdapter2 = new GridAdapter(this, guanList2, level);
		gridView2.setAdapter(gridAdapter2);
		
		gridView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Guan guan = (Guan)parent.getItemAtPosition(position);
				String guanNum = guan.getNumStr();
				int num = Integer.parseInt(guanNum);
				
				if(num <= level){
					Intent i = new Intent(SelectGuanActivity.this, WelActivity.class);
					i.putExtra("num", num);
					startActivity(i);
				}
			}
			
		});
		
		views.add(guanPager2);

		View guanPager3 = inflater.inflate(R.layout.guan_pager, null);
		GridView gridView3 = (GridView) guanPager3.findViewById(R.id.guan_grid);
		List<Guan> guanList3 = new ArrayList<Guan>();
		for (int i = 25; i <= 30; i++) {
			Guan guan = new Guan();
			guan.setNumStr(String.valueOf(i));

			guanList3.add(guan);
		}
		GridAdapter gridAdapter3 = new GridAdapter(this, guanList3, level);
		gridView3.setAdapter(gridAdapter3);
		
		gridView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Guan guan = (Guan)parent.getItemAtPosition(position);
				String guanNum = guan.getNumStr();
				int num = Integer.parseInt(guanNum);
				
				if(num <= level){
					Intent i = new Intent(SelectGuanActivity.this, WelActivity.class);
					i.putExtra("num", num);
					startActivity(i);
				}
			}
			
		});
		
		views.add(guanPager3);

		vp = (ViewPager) findViewById(R.id.viewpager);
		// 初始化Adapter
		vpAdapter = new GuideAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);

		// 初始化底部小点
		initDots();

	}

	@Override
	protected void onResume() {
		super.onResume();
//		fatherLayout.setVisibility(View.VISIBLE);
		
		SharedPreferences levelPreference = this.getSharedPreferences("level", 0);
		if(FirstActivity.gameMode == 1){
			level = levelPreference.getInt("custom_level", 1);
		}else if(FirstActivity.gameMode == 2){
			level = levelPreference.getInt("challenge_level", 1);
		}else{
			level = levelPreference.getInt("infinite_level", 1);
		}
		gridAdapter.setLevel(level);
		gridAdapter.notifyDataSetChanged();
	}
	
	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[3];

		// 循环取得小点图片
		for (int i = 0; i < 3; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
		dots[currentIndex].setImageResource(R.drawable.cur_page);
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
		setCurDot(arg0);
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
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
