package com.xiude.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiude.bottle.Constants;
import com.xiude.bottle.FirstActivity;
import com.xiude.bottle.R;

/**
 * @author Administrator
 *
 */
public class BoardView extends View {

	/**
	 * 时间监听器
	 */
	public OnTimerListener timerListener = null;
	
	/**
	 * 游戏剩余时间
	 */
	public int leftTime;
	
	/**
	 * xCount x轴方向的图标数+1
	 */
	protected  static final int xCount =10;
	/**
	 * yCount y轴方向的图标数+1
	 */
	protected static final int  yCount =12;
	/**
	 * map 连连看游戏棋盘
	 */
	protected int[][] map = new int[xCount][yCount];
	/**
	 * iconSize 图标大小
	 */
	protected int iconSize;
	/**
	 * iconCounts 图标的数目
	 */
	protected int iconCounts=28;
	/**
	 * icons 所有的图片
	 */
	protected Bitmap[] icons = new Bitmap[iconCounts];
	
	/**
	 * path 可以连通点的路径
	 */
	private Point[] path = null;
	/**
	 * selected 选中的图标
	 */
	protected List<Point> selected = new ArrayList<Point>();
	
	/**
	 * customIntegral 经典模式的积分
	 */
	private int customIntegral;
	
	private TextView customIntegralView; //第一种经典模式的积分（右上角）
	
	public TextView getCustomIntegralView() {
		return customIntegralView;
	}

	public void setCustomIntegralView(TextView customIntegralView) {
		this.customIntegralView = customIntegralView;
	}
	
	public int getCustomIntegral() {
		return customIntegral;
	}

	public void setCustomIntegral(int customIntegral) {
		this.customIntegral = customIntegral;
	}
	
	public BoardView(Context context,AttributeSet atts) {
		super(context,atts);
		
		calIconSize();
		
		Resources r = getResources();
		loadBitmaps(1, r.getDrawable(R.drawable.fruit_01));
		loadBitmaps(2, r.getDrawable(R.drawable.fruit_02));
		loadBitmaps(3, r.getDrawable(R.drawable.fruit_03));
		loadBitmaps(4, r.getDrawable(R.drawable.fruit_04));
		loadBitmaps(5, r.getDrawable(R.drawable.fruit_05));
		loadBitmaps(6, r.getDrawable(R.drawable.fruit_06));
		loadBitmaps(7, r.getDrawable(R.drawable.fruit_07));
		loadBitmaps(8, r.getDrawable(R.drawable.fruit_08));
		loadBitmaps(9, r.getDrawable(R.drawable.fruit_09));
		loadBitmaps(10, r.getDrawable(R.drawable.fruit_10));
		loadBitmaps(11, r.getDrawable(R.drawable.fruit_11));
		loadBitmaps(12, r.getDrawable(R.drawable.fruit_12));
		loadBitmaps(13, r.getDrawable(R.drawable.fruit_13));
		loadBitmaps(14, r.getDrawable(R.drawable.fruit_14));
		loadBitmaps(15, r.getDrawable(R.drawable.fruit_15));
		loadBitmaps(16, r.getDrawable(R.drawable.fruit_16));
		loadBitmaps(17, r.getDrawable(R.drawable.fruit_17));
		loadBitmaps(18, r.getDrawable(R.drawable.fruit_18));
		loadBitmaps(19, r.getDrawable(R.drawable.fruit_19));
		loadBitmaps(20, r.getDrawable(R.drawable.fruit_20));
		loadBitmaps(21, r.getDrawable(R.drawable.fruit_21));
		loadBitmaps(22, r.getDrawable(R.drawable.fruit_22));
		loadBitmaps(23, r.getDrawable(R.drawable.fruit_23));
		loadBitmaps(24, r.getDrawable(R.drawable.fruit_24));
		loadBitmaps(25, r.getDrawable(R.drawable.fruit_25));
		loadBitmaps(26, r.getDrawable(R.drawable.fruit_26));
		loadBitmaps(27, r.getDrawable(R.drawable.fruit_27));
		
	}
	
	/**
	 * 
	 * 计算图标的长宽
	 */
	private void calIconSize()
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) this.getContext()).getWindowManager()
		.getDefaultDisplay().getMetrics(dm);
        iconSize = dm.widthPixels/(xCount - 1);
    }

	/**
	 * 
	 * @param key 特定图标的标识
	 * @param d drawable下的资源
	 */
	public void loadBitmaps(int key,Drawable d){
		Bitmap bitmap = Bitmap.createBitmap(iconSize,iconSize,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, iconSize, iconSize);
		d.draw(canvas);
		icons[key]=bitmap;
	}
	
//	long lastTime = 0; //上次连通时间
//	int lineCount = 0; //连击数
//	public static int maxCount = 0; //最大连击数
	Handler handler = new Handler();
	Bitmap lineBm;  //连线的位图
	Bitmap leftTopCornerBm; //左上角转角的位图
	Bitmap leftBottomBm; //左下角转角
	Bitmap rightTopBm; //右上角转角
	Bitmap rightBottomBm; //右下角转角
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		/**
		 * 绘制棋盘的所有图标 当这个坐标内的值大于0时绘制
		 */
				for(int x=1;x<map.length;x+=1){
					for(int y=0;y<map[x].length;y+=1){
						if(map[x][y]>0){
//							Log.i("map", "map > 0");
							final PointF p = indextoScreen(x, y);
							final int mapXy = map[x][y];
						
							canvas.drawBitmap(icons[mapXy], p.x,p.y,null);
							
						}
					}
				}
		
		/**
		 * 绘制选中图标，当选中时图标放大显示
		 */
		for(Point position:selected){
			PointF p = indextoScreen(position.x, position.y);
			if(map[position.x][position.y] >= 1){
				canvas.drawBitmap(icons[map[position.x][position.y]],
						null,
						new Rect((int)(p.x-15), (int)(p.y-15), (int)(p.x + iconSize + 15), (int)(p.y + iconSize + 15)), null);
			}
		}
		
		/**
		 * 绘制连通路径，然后将路径以及两个图标清除
		 */
		if (path != null && path.length >= 2) {
			//经典模式，每连一个加10分，并显示
			customIntegral += 10;
			customIntegralView.setText(String.valueOf(customIntegral));
			
			if(!isFind){
				String lastOrientation = null;
				
				//不是使用自动提醒工具
				for (int i = 0; i < path.length - 1; i++) {
					//画连线
					Paint paint = new Paint();
					paint.setColor(Color.WHITE);
					paint.setStyle(Paint.Style.STROKE);
					paint.setStrokeWidth(7);
					PointF p1 = indextoScreen(path[i].x, path[i].y);
					PointF p2 = indextoScreen(path[i + 1].x, path[i + 1].y);
					
					canvas.drawLine(p1.x + iconSize / 2, p1.y + iconSize / 2,
						p2.x + iconSize / 2, p2.y + iconSize / 2, paint);
					Log.i("autoClear", "drawLine");
				}
			}
			
			//第二种模式，每消一对加一刻时间
			if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){
				leftTime += 8;
				timerListener.onTimer(leftTime);
			}
			//end
			
			if(isFind){
				//使用自动提醒工具
				Point p = path[0];
				selected.add(p);
				Point p2 = path[path.length - 1];
				selected.add(p2);
				path = null;
			}else{
				//不是使用自动提醒工具
				Point p = path[0];
				map[p.x][p.y] = 0;
				p = path[path.length - 1];
				map[p.x][p.y] = 0;
				selected.clear();
				path = null;
			}
		
			//计算连击数 begin
			if(Constants.lastTime == 0){
				Constants.lastTime = System.currentTimeMillis();
			}else{
				long curTime = System.currentTimeMillis();
				long duration = curTime - Constants.lastTime;
				
				Constants.lastTime = curTime;
				if(duration < 2000){
					int lineCount = ++Constants.lineCount;
					
				/*	new Thread(new Runnable() {
						
						@Override
						public void run() {
							if(tempToast != null){
								tempToast.cancel();
							}
						}
					}).start();*/
					
					toast = Toast.makeText(this.getContext(), getResources().getString(R.string.lianji)+"："+lineCount, 0);
//					tempToast = toast;
					
					//连击数计算总得分，并显示出来
					customIntegral += lineCount * 30;
					customIntegralView.setText(String.valueOf(customIntegral));
					
					toast.show();
					
				/*	for(Runnable  run : runnableList){
						Log.i("toast", "removeCallbacks");
						handler.removeCallbacks(run);
					}
					runnableList.clear();
					
					Log.i("toast", "show==="+toast.toString());
					Runnable r = new Runnable() {
						
						@Override
						public void run() {
							Log.i("toast", "cancel==="+toast.toString());
							toast.cancel();
						}
					};
					runnableList.add(r);
					
					 handler.postDelayed(r, 1000);*/
					 
					if(Constants.lineCount > Constants.maxCount){
						Constants.maxCount = Constants.lineCount;
					}
				}else{
					Constants.lineCount = 0;
				}
			}
			//end
			
		}
		isFind = false;
	}
	public static Toast toast;
//	private List<Toast> runnableList = new ArrayList<Toast>();
	
	
	/**
	 * 
	 * @param path
	 */
	public void drawLine(Point[] path) {
		Log.i("autoClear", "path.length ="+path.length);
		this.path = path;
		this.invalidate();
	}
	
	/**
	 * 道具提示连接
	 * 
	 */
	public void tip(Point[] path) {
		//把找到的图标放大
		this.path = path;
		isFind = true;
		this.invalidate();
		
		//把找到的图标还原
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				selected.clear();
				invalidate();
				
			}
		}, 1000);
	}
	private static boolean isFind; //是否道具提示连接
	/**
	 * 工具方法
	 * @param x 数组中的横坐标
	 * @param y 数组中的纵坐标
	 * @return 将图标在数组中的坐标转成在屏幕上的真实坐标
	 */
	public PointF indextoScreen(int x,int y){
		return new PointF((x - 0.5f) * iconSize , (float)y * iconSize );
	}
	/**
	 * 工具方法
	 * @param x 屏幕中的横坐标
	 * @param y 屏幕中的纵坐标
	 * @return 将图标在屏幕中的坐标转成在数组上的虚拟坐标
	 */
	public Point screenToindex(int x,int y){
		int ix = (x+iconSize/2) / iconSize;
		int iy = y / iconSize;
//		Log.i("screenToindex", "iconSize ="+iconSize +", ix ="+ix+", iy ="+iy);
		if(ix < xCount && iy <yCount){
			return new Point( ix,iy);
		}else{
			return new Point(0,0);
		}
	}
}
