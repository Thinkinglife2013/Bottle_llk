package com.xiude.bottle;



import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class GuideAdapter extends PagerAdapter{
	
	//界面列表
	private List<View> views;
	
	public GuideAdapter (List<View> views){
		this.views = views;
	}

	//�?��arg1位置的界�?
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));		
	}

	@Override
	public void finishUpdate(View arg0) {
		
	}

	//获得当前界面�?
	@Override
	public int getCount() {
		if (views != null)
		{
			return views.size();
		}
		
		return 0;
	}
	

	//初始化arg1位置的界�?
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		
		((ViewPager) arg0).addView(views.get(arg1), 0);
		
		return views.get(arg1);
	}

	//判断是否由对象生成界�?
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		
	}

}
