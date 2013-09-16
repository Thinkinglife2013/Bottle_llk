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
	protected int iconCounts=28;
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
	
	/**
	 * customIntegral ����ģʽ�Ļ���
	 */
	private int customIntegral;
	
	private TextView customIntegralView; //��һ�־���ģʽ�Ļ��֣����Ͻǣ�
	
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
	 * ����ͼ��ĳ���
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
		
		/**
		 * �������̵�����ͼ�� ����������ڵ�ֵ����0ʱ����
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
		 * ����ѡ��ͼ�꣬��ѡ��ʱͼ��Ŵ���ʾ
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
		 * ������ͨ·����Ȼ��·���Լ�����ͼ�����
		 */
		if (path != null && path.length >= 2) {
			//����ģʽ��ÿ��һ����10�֣�����ʾ
			customIntegral += 10;
			customIntegralView.setText(String.valueOf(customIntegral));
			
			if(!isFind){
				String lastOrientation = null;
				
				//����ʹ���Զ����ѹ���
				for (int i = 0; i < path.length - 1; i++) {
					//������
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
			
			//�ڶ���ģʽ��ÿ��һ�Լ�һ��ʱ��
			if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){
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
					int lineCount = ++Constants.lineCount;
					
				/*	new Thread(new Runnable() {
						
						@Override
						public void run() {
							if(tempToast != null){
								tempToast.cancel();
							}
						}
					}).start();*/
					
					toast = Toast.makeText(this.getContext(), getResources().getString(R.string.lianji)+"��"+lineCount, 0);
//					tempToast = toast;
					
					//�����������ܵ÷֣�����ʾ����
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
	public PointF indextoScreen(int x,int y){
		return new PointF((x - 0.5f) * iconSize , (float)y * iconSize );
	}
	/**
	 * ���߷���
	 * @param x ��Ļ�еĺ�����
	 * @param y ��Ļ�е�������
	 * @return ��ͼ������Ļ�е�����ת���������ϵ���������
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
