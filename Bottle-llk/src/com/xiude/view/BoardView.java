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
	 * ʱ�������
	 */
	public OnTimerListener timerListener = null;
	
	/**
	 * ��Ϸʣ��ʱ��
	 */
	public int leftTime;
	
	/**
	 * xCount x�᷽���ͼ����+1
	 */
	protected  static final int xCount =10;
	/**
	 * yCount y�᷽���ͼ����+1
	 */
	protected static final int  yCount =12;
	/**
	 * map ��������Ϸ����
	 */
	protected int[][] map = new int[xCount][yCount];
	/**
	 * iconSize ͼ���С
	 */
	protected int iconSize;
	/**
	 * iconCounts ͼ�����Ŀ
	 */
	protected int iconCounts=25;
	/**
	 * icons ���е�ͼƬ
	 */
	protected Bitmap[] icons = new Bitmap[iconCounts];
	
	/**
	 * path ������ͨ���·��
	 */
	private Point[] path = null;
	/**
	 * selected ѡ�е�ͼ��
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
		
		//��ʼ������ͼƬ
		lineBm = BitmapFactory.decodeResource(getResources(), R.drawable.line);
		
		//��ʼ�����ϽǷ���ת��ͼƬ
		leftTopCornerBm = BitmapFactory.decodeResource(getResources(), R.drawable.corner);
//		leftTopCornerBm = Bitmap.createBitmap(leftTopCornerBm, 0, 0, leftTopCornerBm.getWidth()/2, leftTopCornerBm.getHeight()); 
		
        Matrix matrix=new Matrix(); 
        
        //��ʼ�����½Ƿ���ת��ͼƬ
	    // ������ת90�ȣ�����Ϊ����������ת  
	    matrix.postRotate(-90); 
        leftBottomBm = Bitmap.createBitmap(leftTopCornerBm,0,0,leftTopCornerBm.getWidth(), 
        		leftTopCornerBm.getHeight(), matrix,true); 
        
      //��ʼ�����ϽǷ���ת��ͼƬ
        Matrix matrix2=new Matrix(); 
	     // ������ת90�ȣ�����Ϊ����������ת  
	     matrix2.postRotate(90); 
	     rightTopBm = Bitmap.createBitmap(leftTopCornerBm,0,0,leftTopCornerBm.getWidth(), 
      		leftTopCornerBm.getHeight(), matrix2,true); 
	     
	     //��ʼ�����½Ƿ���ת��ͼƬ
       Matrix matrix3 = new Matrix(); 
	     // ������ת90�ȣ�����Ϊ����������ת  
	     matrix3.postRotate(180); 
	     rightBottomBm = Bitmap.createBitmap(leftTopCornerBm,0,0,leftTopCornerBm.getWidth(), 
     		leftTopCornerBm.getHeight(), matrix3,true); 
	}
	
	/**
	 * 
	 * ����ͼ��ĳ���
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
	 * @param key �ض�ͼ��ı�ʶ
	 * @param d drawable�µ���Դ
	 */
	public void loadBitmaps(int key,Drawable d){
		Bitmap bitmap = Bitmap.createBitmap(iconSize,iconSize,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, iconSize, iconSize);
		d.draw(canvas);
		icons[key]=bitmap;
	}
	
//	long lastTime = 0; //�ϴ���ͨʱ��
//	int lineCount = 0; //������
//	public static int maxCount = 0; //���������
	Handler handler = new Handler();
	Bitmap lineBm;  //���ߵ�λͼ
	Bitmap leftTopCornerBm; //���Ͻ�ת�ǵ�λͼ
	Bitmap leftBottomBm; //���½�ת��
	Bitmap rightTopBm; //���Ͻ�ת��
	Bitmap rightBottomBm; //���½�ת��
	
	@Override
	protected void onDraw(Canvas canvas) {
		
//		long beginTime = System.currentTimeMillis();
		
		/**
		 * �������̵�����ͼ�� ����������ڵ�ֵ����0ʱ����
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
		 * ����ѡ��ͼ�꣬��ѡ��ʱͼ��Ŵ���ʾ
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
		 * ������ͨ·����Ȼ��·���Լ�����ͼ�����
		 */
		if (path != null && path.length >= 2) {
			if(!isFind){
				String lastOrientation = null;
//				long beginTime = System.currentTimeMillis();
				
				//����ʹ���Զ����ѹ���
				for (int i = 0; i < path.length - 1; i++) {
					//������
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
					
					
					 // ����������  
			         Matrix matrix=new Matrix(); 
			         
			         float scaleH = (float)length/lineBm.getWidth();
			         // ����ԭͼ  
			         matrix.postScale(scaleH, 1f); 
			         
			         if(p1.x == p2.x){
				         // ������ת45�ȣ�����Ϊ����������ת  
				         matrix.postRotate(-90); 
			         }
			        
			         Bitmap dstbmp = Bitmap.createBitmap(lineBm,0,0,lineBm.getWidth(), 
			        		 lineBm.getHeight(), matrix,true); 
			         
			         if(p1.y > p2.y){//������
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
			        	 
			         }else if(p1.y < p2.y){//������
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
			        	 
			         }else if(p1.x > p2.x){//������
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
			        	
			         }else if(p1.x < p2.x){//������
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
			
			//�ڶ���ģʽ��ÿ��һ�Լ�һ��ʱ��
			if(SelectModeActivity.gameMode == 2 || SelectModeActivity.gameMode == 3){
				leftTime += 8;
				timerListener.onTimer(leftTime);
			}
			//end
			
			if(isFind){
				//ʹ���Զ����ѹ���
				Point p = path[0];
				selected.add(p);
				Point p2 = path[path.length - 1];
				selected.add(p2);
				path = null;
			}else{
				//����ʹ���Զ����ѹ���
				Point p = path[0];
				map[p.x][p.y] = 0;
				p = path[path.length - 1];
				map[p.x][p.y] = 0;
				selected.clear();
				path = null;
			}
		
			//���������� begin
			if(Constants.lastTime == 0){
				Constants.lastTime = System.currentTimeMillis();
			}else{
				long curTime = System.currentTimeMillis();
				long duration = curTime - Constants.lastTime;
				
				Constants.lastTime = curTime;
				if(duration < 2000){
					final Toast toast = Toast.makeText(this.getContext(), "��������"+(++Constants.lineCount), 0);
					
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
	 * ������ʾ����
	 * 
	 */
	public void tip(Point[] path) {
		//���ҵ���ͼ��Ŵ�
		this.path = path;
		isFind = true;
		this.invalidate();
		
		//���ҵ���ͼ�껹ԭ
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				selected.clear();
				invalidate();
				
			}
		}, 1000);
	}
	private static boolean isFind; //�Ƿ������ʾ����
	/**
	 * ���߷���
	 * @param x �����еĺ�����
	 * @param y �����е�������
	 * @return ��ͼ���������е�����ת������Ļ�ϵ���ʵ����
	 */
	public Point indextoScreen(int x,int y){
		return new Point(x* iconSize , y * iconSize );
	}
	/**
	 * ���߷���
	 * @param x ��Ļ�еĺ�����
	 * @param y ��Ļ�е�������
	 * @return ��ͼ������Ļ�е�����ת���������ϵ���������
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
