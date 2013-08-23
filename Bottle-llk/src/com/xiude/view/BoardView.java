package com.xiude.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.xiude.bottle.Constants;
import com.xiude.bottle.SelectModeActivity;
import com.xiudekeji.android.R;

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
	protected int iconCounts=25;
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
		
		//初始化连线图片
		lineBm = BitmapFactory.decodeResource(getResources(), R.drawable.line);
		
		//初始化左上角方向转角图片
		leftTopCornerBm = BitmapFactory.decodeResource(getResources(), R.drawable.corner);
//		leftTopCornerBm = Bitmap.createBitmap(leftTopCornerBm, 0, 0, leftTopCornerBm.getWidth()/2, leftTopCornerBm.getHeight()); 
		
        Matrix matrix=new Matrix(); 
        
        //初始化左下角方向转角图片
	    // 向左旋转90度，参数为正则向右旋转  
	    matrix.postRotate(-90); 
        leftBottomBm = Bitmap.createBitmap(leftTopCornerBm,0,0,leftTopCornerBm.getWidth(), 
        		leftTopCornerBm.getHeight(), matrix,true); 
        
      //初始化右上角方向转角图片
        Matrix matrix2=new Matrix(); 
	     // 向左旋转90度，参数为正则向右旋转  
	     matrix2.postRotate(90); 
	     rightTopBm = Bitmap.createBitmap(leftTopCornerBm,0,0,leftTopCornerBm.getWidth(), 
      		leftTopCornerBm.getHeight(), matrix2,true); 
	     
	     //初始化右下角方向转角图片
       Matrix matrix3 = new Matrix(); 
	     // 向左旋转90度，参数为正则向右旋转  
	     matrix3.postRotate(180); 
	     rightBottomBm = Bitmap.createBitmap(leftTopCornerBm,0,0,leftTopCornerBm.getWidth(), 
     		leftTopCornerBm.getHeight(), matrix3,true); 
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
        iconSize = dm.widthPixels/(xCount);
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
		
//		long beginTime = System.currentTimeMillis();
		
		/**
		 * 绘制棋盘的所有图标 当这个坐标内的值大于0时绘制
		 */
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
				for(int x=0;x<map.length;x+=1){
					for(int y=0;y<map[x].length;y+=1){
						if(map[x][y]>0){
//							Log.i("map", "map > 0");
							final Point p = indextoScreen(x, y);
							final int mapXy = map[x][y];
							
						
							canvas.drawBitmap(icons[mapXy], p.x,p.y,null);
							
						}
					}
				}
//			}
//		}, 200);
		
		/**
		 * 绘制选中图标，当选中时图标放大显示
		 */
		for(Point position:selected){
			Point p = indextoScreen(position.x, position.y);
			if(map[position.x][position.y] >= 1){
				canvas.drawBitmap(icons[map[position.x][position.y]],
						null,
						new Rect(p.x-5, p.y-5, p.x + iconSize + 5, p.y + iconSize + 5), null);
			}
		}
		
		/**
		 * 绘制连通路径，然后将路径以及两个图标清除
		 */
		if (path != null && path.length >= 2) {
			if(!isFind){
				String lastOrientation = null;
//				long beginTime = System.currentTimeMillis();
				
				//不是使用自动提醒工具
				for (int i = 0; i < path.length - 1; i++) {
					//画连线
//					Paint paint = new Paint();
//					paint.setColor(Color.TRANSPARENT);
//					paint.setStyle(Paint.Style.FILL);
//					paint.setStrokeWidth(3);
					Point p1 = indextoScreen(path[i].x, path[i].y);
					Point p2 = indextoScreen(path[i + 1].x, path[i + 1].y);
					
//					canvas.drawLine(p1.x + iconSize / 2, p1.y + iconSize / 2,
//						p2.x + iconSize / 2, p2.y + iconSize / 2, paint);
					
					int length;
					if(p1.x == p2.x){
						length = (p2.y) - (p1.y);
					}else{
						length = (p2.x) - (p1.x);
					}
					
					
					 // 定义矩阵对象  
			         Matrix matrix=new Matrix(); 
			         
			         float scaleH = (float)length/lineBm.getWidth();
			         // 缩放原图  
			         matrix.postScale(scaleH, 1f); 
			         
			         if(p1.x == p2.x){
				         // 向左旋转45度，参数为正则向右旋转  
				         matrix.postRotate(-90); 
			         }
			        
			         Bitmap dstbmp = Bitmap.createBitmap(lineBm,0,0,lineBm.getWidth(), 
			        		 lineBm.getHeight(), matrix,true); 
			         
			         if(p1.y > p2.y){//向上连
			        	 canvas.drawBitmap(dstbmp, p1.x, p2.y + iconSize / 2, null);
			        	 if(path.length == 3 || path.length == 4){
			        		 if(i == 0){
			        			 lastOrientation = "up";
			        		 }else if(i == 1){
			        			 if("up".equals(lastOrientation)){
			        			 }else if("down".equals(lastOrientation)){
			        			 }else if("left".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightTopBm, p1.x, p1.y , null);
			        			 }else if("right".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftTopCornerBm, p1.x, p1.y , null);
			        			 }
			        			 
			        			 if(path.length == 4){
			        				 lastOrientation = "up";
				        		 }
			        		 }else if(i == 2){
			        			 if("up".equals(lastOrientation)){
			        			 }else if("down".equals(lastOrientation)){
			        			 }else if("left".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightTopBm, p1.x, p1.y , null);
			        			 }else if("right".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftTopCornerBm, p1.x, p1.y , null);
			        			 }
			        		 }
			        		 
			        	 }
			        	 
			         }else if(p1.y < p2.y){//向下连
			        	 canvas.drawBitmap(dstbmp, p2.x, p1.y + iconSize / 2, null);
			        	 if(path.length == 3 || path.length == 4){
			        		 if(i == 0){
			        			 lastOrientation = "down";
			        		 }else if(i == 1){
			        			 if("up".equals(lastOrientation)){
			        			 }else if("down".equals(lastOrientation)){
			        			 }else if("left".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightBottomBm, p1.x , p1.y, null);
			        			 }else if("right".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftBottomBm, p1.x , p1.y, null);
			        			 }
			        			 
			        			 if(path.length == 4){
			        				 lastOrientation = "down";
				        		 }
			        		 }else if(i == 2){
			        			 if("up".equals(lastOrientation)){
			        			 }else if("down".equals(lastOrientation)){
			        			 }else if("left".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightBottomBm, p1.x , p1.y, null);
			        			 }else if("right".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftBottomBm, p1.x , p1.y, null);
			        			 }
			        		 }
			        		
			        	 }
			        	 
			         }else if(p1.x > p2.x){//向左连
			        	 canvas.drawBitmap(dstbmp, p2.x + iconSize / 2, p1.y, null);
			        	 if(path.length == 3 || path.length == 4){
			        		 if(i == 0){
			        			 lastOrientation = "left";
			        		 }else if(i == 1){
			        			 if("up".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftBottomBm, p1.x, p1.y , null);
			        			 }else if("down".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftTopCornerBm, p1.x , p1.y , null);
			        			 }else if("left".equals(lastOrientation)){
			        			 }else if("right".equals(lastOrientation)){
			        			 }
			        			 if(path.length == 4){
			        				 lastOrientation = "left";
				        		 }
			        		 }else if(i == 2){
			        			 if("up".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftBottomBm, p1.x, p1.y , null);
			        			 }else if("down".equals(lastOrientation)){
			        				 canvas.drawBitmap(leftTopCornerBm, p1.x , p1.y , null);
			        			 }else if("left".equals(lastOrientation)){
			        			 }else if("right".equals(lastOrientation)){
			        			 }
			        		 }
			        	 }
			        	
			         }else if(p1.x < p2.x){//向右连
			        	 canvas.drawBitmap(dstbmp, p1.x + iconSize / 2, p1.y, null);
			        	 if(path.length == 3 || path.length == 4){
			        		 if(i == 0){
			        			 lastOrientation = "right";
			        		 }else if(i == 1){
			        			 if("up".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightBottomBm, p1.x, p1.y , null);
			        			 }else if("down".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightTopBm, p1.x , p1.y , null);
			        			 }else if("left".equals(lastOrientation)){
			        			 }else if("right".equals(lastOrientation)){
			        			 }
			        			 
			        			 if(path.length == 4){
			        				 lastOrientation = "right";
				        		 }
			        		 }else if(i == 2){
			        			 if("up".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightBottomBm, p1.x, p1.y , null);
			        			 }else if("down".equals(lastOrientation)){
			        				 canvas.drawBitmap(rightTopBm, p1.x , p1.y , null);
			        			 }else if("left".equals(lastOrientation)){
			        			 }else if("right".equals(lastOrientation)){
			        			 }
			        		 }
			        		 
			        	 }
			        	 
			         }
					
				}
//				long duration = System.currentTimeMillis() - beginTime;
//				Log.i("time", ""+duration);
			}
			
			//第二种模式，每消一对加一刻时间
			if(SelectModeActivity.gameMode == 2 || SelectModeActivity.gameMode == 3){
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
					final Toast toast = Toast.makeText(this.getContext(), "连击数："+(++Constants.lineCount), 0);
					
//					Runnable r = new Runnable() {
//						
//						@Override
//						public void run() {
//							toast.cancel();
//						}
//					};
//					handler.removeCallbacks(r);
					toast.show();
//					 handler.postDelayed(r, 1000);
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
	
	
	/**
	 * 
	 * @param path
	 */
	public void drawLine(Point[] path) {
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
	public Point indextoScreen(int x,int y){
		return new Point(x* iconSize , y * iconSize );
	}
	/**
	 * 工具方法
	 * @param x 屏幕中的横坐标
	 * @param y 屏幕中的纵坐标
	 * @return 将图标在屏幕中的坐标转成在数组上的虚拟坐标
	 */
	public Point screenToindex(int x,int y){
		int ix = x/ iconSize;
		int iy = y / iconSize;
		if(ix < xCount && iy <yCount){
			return new Point( ix,iy);
		}else{
			return new Point(0,0);
		}
	}
}
