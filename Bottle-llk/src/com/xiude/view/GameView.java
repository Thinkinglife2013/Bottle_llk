package com.xiude.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiude.bottle.BaseActivity;
import com.xiude.bottle.FirstActivity;
import com.xiude.bottle.R;
import com.xiude.bottle.FirstActivity;
import com.xiude.bottle.SoundPlay;

public class GameView extends BoardView {

	private static final int REFRESH_VIEW = 1;

	public static final int WIN = 1;
	public static final int LOSE = 2;
	public static final int PAUSE = 3;
	public static final int PLAY = 4;
	public static final int QUIT = 5;

	private int Help = 1;
	private int Refresh = 1;
	private int Find = 1;
	/**
	 * 第一关为100秒钟的时间
	 */
	private int totalTime = 100;
	
	
	public static SoundPlay soundPlay;
//	public MediaPlayer player;
	
	public RefreshTime refreshTime;
	private RefreshHandler refreshHandler = new RefreshHandler();
	/**
	 * 用来停止计时器的线程
	 */
	private boolean isStop;
	
	
	private OnStateListener stateListener = null;
	private OnToolsChangeListener toolsChangedListener = null;

	private List<Point> path = new ArrayList<Point>();
	Context context;
	
	public GameView(Context context, AttributeSet atts) {
		super(context, atts);
		this.context = context;
		
//		if(!BaseActivity.isAllBgMusicClickPause){
//			player = MediaPlayer.create(context, R.raw.back2new); 
//			player.setLooping(true);//设置循环播放
//		}
	}

	public static final int ID_SOUND_CHOOSE = 0;
	public static final int ID_SOUND_DISAPEAR = 1;
	public static final int ID_SOUND_WIN = 4;
	public static final int ID_SOUND_LOSE = 5;
	public static final int ID_SOUND_REFRESH = 6;
	public static final int ID_SOUND_TIP = 7;
	public static final int ID_SOUND_ERROR = 8;
	public static final int ID_SOUND_READYGO = 9;
	
	private SeekBar progress;
	public void setProgress(SeekBar pro){
//		pro.setMax(totalTime);
		this.progress = pro;
		
	}
	
	public OnTimerListener getTimerListener() {
		return timerListener;
	}

	public void setTimerListener(OnTimerListener timerListener) {
		this.timerListener = timerListener;
	}

	public void startPlay(int guan){
		if(FirstActivity.gameMode == 1){
			//第一种模式，每关道具不累计，初始为1
			Help = 1;
			Refresh = 1;
			Find = 1;
		}
		isStop = false;
		toolsChangedListener.onRefreshChanged(Refresh);
		toolsChangedListener.onTipChanged(Help);
		toolsChangedListener.onFindChanged(Find);
		
		Message msg = Message.obtain();
		msg.what = 2;
		msg.arg1 = guan;
		handler.sendMessage(msg);
		if(guan == 1){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 300;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 120;
			}
			initOneMap();
		}else if(guan == 2){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 300;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 150;
			}
			initTwoMap();
		}else if(guan == 3){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 400;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 180;
			}
			initThreeMap();
		}else if(guan == 4){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 500;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 230;
			}
			initFourMap();
		}else if(guan == 5){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 500;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 230;
			}
			initFiveMap();
		}else if(guan == 6){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 500;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 230;
			}
			initSixMap();
		}else if(guan == 7){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 600;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 270;
			}
			initSevenMap();
		}else if(guan == 8){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initEightMap();
		}else if(guan == 9){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initNightMap();
		}else if(guan == 10){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTenMap();
		}else if(guan == 11){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initElevenMap();
		}else if(guan == 12){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwelveMap();
		}else if(guan == 13){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initThirteenMap();
		}else if(guan == 14){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initFourteenMap();
		}else if(guan == 15){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initFifteenMap();
		}else if(guan == 16){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initSixteenMap();
		}else if(guan == 17){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initSeventeenMap();
		}else if(guan == 18){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initEighteenMap();
		}else if(guan == 19){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initNineteenMap();
		}else if(guan == 20){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyMap();
		}else if(guan == 21){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyOneMap();
		}else if(guan == 22){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyTwoMap();
		}else if(guan == 23){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyThreeMap();
		}else if(guan == 24){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyFourMap();
		}else if(guan == 25){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyFiveMap();
		}else if(guan == 26){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentySixMap();
		}else if(guan == 27){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentySevenMap();
		}else if(guan == 28){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyEightMap();
		}else if(guan == 29){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initTwentyNineMap();
		}else if(guan == 30){
			if(FirstActivity.gameMode == 1){//第一種模式
				totalTime = 800;
			}else if(FirstActivity.gameMode == 2 || FirstActivity.gameMode == 3){//第二種模式
				totalTime = 350;
			}
			initThirtyMap();
		}
		
		progress.setMax(totalTime);
		leftTime = totalTime;
		setRefeshTimeCount(0);
		
//		if(!BaseActivity.isAllBgMusicClickPause){
//			player.start();
//		}
		
		refreshTime = new RefreshTime();
		refreshTime.start();
		GameView.this.invalidate();
	}
	
	public void startNextPlay(int guan){
		//下一关为上一关减去10秒的时间
//		totalTime-=10;
		if(FirstActivity.gameMode != 3){
			SharedPreferences levelPreference = context.getSharedPreferences("level", 0);
			int level = 1;
			if(FirstActivity.gameMode == 1){
				level = levelPreference.getInt("custom_level", 1);
			}else if(FirstActivity.gameMode == 2){
				level = levelPreference.getInt("challenge_level", 1);
			}
			
			if(level <= guan){
				if(FirstActivity.gameMode == 1){
					levelPreference.edit().putInt("custom_level", guan).commit();
				}else if(FirstActivity.gameMode == 2){
					levelPreference.edit().putInt("challenge_level", guan).commit();
				}
			}
		}
		startPlay(guan);
	}
	
	public static void initSound(Context context){
		 soundPlay = new SoundPlay();
	        soundPlay.initSounds(context);
	        soundPlay.loadSfx(context, R.raw.choose, ID_SOUND_CHOOSE);
	        soundPlay.loadSfx(context, R.raw.disappear1, ID_SOUND_DISAPEAR);
	        soundPlay.loadSfx(context, R.raw.win, ID_SOUND_WIN);
	        soundPlay.loadSfx(context, R.raw.lose, ID_SOUND_LOSE);
	        soundPlay.loadSfx(context, R.raw.item1, ID_SOUND_REFRESH);
	        soundPlay.loadSfx(context, R.raw.item2, ID_SOUND_TIP);
	        soundPlay.loadSfx(context, R.raw.alarm, ID_SOUND_ERROR);
	        soundPlay.loadSfx(context, R.raw.ready_go, ID_SOUND_READYGO);
	}
	
	public static void destorySound(){
		 soundPlay = null;
	}

	public void setOnTimerListener(OnTimerListener timerListener){
		this.timerListener = timerListener;
	}
	
	public void setOnStateListener(OnStateListener stateListener){
		this.stateListener = stateListener;
	}
	
	public void setOnToolsChangedListener(OnToolsChangeListener toolsChangedListener){
		this.toolsChangedListener = toolsChangedListener;
	}
	
	public void stopTimer(){
		isStop = true;
	}
	
	public void startTimer(){
		isStop = false;
	}
	
	public boolean isStop(){
		return isStop;
	}
	
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == REFRESH_VIEW) {
				GameView.this.invalidate();
				if (win()) {
					setMode(WIN);
					if(soundPlay != null)
						soundPlay.play(ID_SOUND_WIN, 0);
					isStop = true;
				} else if (die()) {
					change();
				}
			}
		}

		public void sleep(int delayTime) {
			this.removeMessages(0);
			Message message = new Message();
			message.what = REFRESH_VIEW;
			sendMessageDelayed(message, delayTime);
		}
	}

	//刷新时间进度条的次数
	private int refeshTimeCount;
	
	public class RefreshTime extends Thread {

		public void run() {
//			refeshTimeCount = 0;
			while (leftTime >= 0 && !isStop) {
				timerListener.onTimer(leftTime);
				leftTime--;
				try {
					Thread.sleep(100);
					setRefeshTimeCount(getRefeshTimeCount() + 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!isStop){
				setMode(LOSE);
				if(soundPlay != null)
					soundPlay.play(ID_SOUND_LOSE, 0);
			}
			
		}
	}
	
	public int getTotalTime(){
		return totalTime;
	}
	
	public int getTipNum(){
		return Help;
	}
	
	public int getRefreshNum(){
		return Refresh;
	}
	
	public int getFindNum(){
		return Find;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
//		Log.i("xy", "x="+x+", y="+y);
		Point p = screenToindex(x, y);
//		Log.i("selected.size()", selected.size()+"");
		if (map[p.x][p.y] > 0) {
			if (selected.size() == 1) {
				if (link(selected.get(0), p)) {
					selected.add(p);
					drawLine(path.toArray(new Point[] {}));
					
					if(soundPlay != null)
						soundPlay.play(ID_SOUND_DISAPEAR, 0);
					refreshHandler.sleep(500);
				} else {
					selected.clear();
					selected.add(p);
					
					if(soundPlay != null)
						soundPlay.play(ID_SOUND_CHOOSE, 0);
					GameView.this.invalidate();
				}
			} else {
				selected.add(p);
				
				if(soundPlay != null)
					soundPlay.play(ID_SOUND_CHOOSE, 0);
				GameView.this.invalidate();
			}
		}
//		Log.i("selected.size()", selected.size()+"");
		return super.onTouchEvent(event);
	}
	
	/**
	 * 生成第一关的地图
	 */
	public void initOneMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(i == 1 && j == 1){
					map[i][j] = x;
				}else if(j == 2 && i == 2){
					map[i][j] = x;
				}else if(i == 3 && j == 3){
					map[i][j] = x;
				}else if(j == 10 && i == 1){
					map[i][j] = x;
				}else if(j == 9 && i == 2){
					map[i][j] = x;
				}else if(j == 8 && i == 3){
					map[i][j] = x;
				}else if(j == 1 && i == 8){
					map[i][j] = x;
				}else if(j == 2 && i == 7){
					map[i][j] = x;
				}else if(j == 3 && i == 6){
					map[i][j] = x;
				}else if(j == 10 && i == 8){
					map[i][j] = x;
				}else if(j == 9 && i == 7){
					map[i][j] = x;
				}else if(j == 8 && i == 6){
					map[i][j] = x;
				}else if(j <= 7 && j >= 4 && i == 4){
					map[i][j] = x;
				}else if(j <= 7 && j >= 4 && i == 5){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}

	/**
	 * 生成第二关的地图
	 */
	public void initTwoMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 3; j < yCount - 1; j++) {
				if(j == 3 || j == 8){
					map[i][j] = x;
				}else if((i == 4 || i == 5) && j >= 4 && j <= 7){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二关的地图
	 */
	public void initThreeMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 2; j < yCount - 1; j++) {
				if((j == 2 || j == 8) && i == 2){
					map[i][j] = x;
				}else if((i == 1 || i == 8) && j >= 2 && j <= 7){
					map[i][j] = x;
				}else if(i == 3 && (j == 3 || j == 9)){
					map[i][j] = x;
				}else if(i == 4 && (j == 4 || j == 10)){
					map[i][j] = x;
				}else if(i == 5 && (j == 4 || j == 10)){
					map[i][j] = x;
				}else if(i == 6 && (j == 3 || j == 9)){
					map[i][j] = x;
				}else if(i == 7 && (j == 2 || j == 8)){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第四关的地图
	 */
	public void initFourMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 3; j < yCount - 2; j++) {
				if(j == 3){
					map[i][j] = x;
				}else if(j == 8){
					map[i][j] = x;
				}else if((i == 1 || i == 4 || i == 5 || i == 8 ) && j <= 7 && j >= 4){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第五关的地图
	 */
	public void initFiveMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 3; j < yCount - 1; j++) {
				if(j == 3 || j == 4 || j == 7 || j == 8){
					map[i][j] = x;
				}else if((i == 1 || i == 2 || i == 7 || i == 8) && (j == 5 || j == 6 )){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第五关的地图
	 */
	public void initSixMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 2; j < yCount - 1; j++) {
				if((j == 5 || j == 6) && i != 1 && i != 8){
					map[i][j] = x;
				}else if((j == 2 || j == 9)){
					map[i][j] = x;
				}else if((i == 1 || i == 8) && j != 1 && j != 10 && j != 2 && j != 9){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第五关的地图
	 */
	public void initSevenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 2; j < yCount - 1; j++) {
				if((j == 5 || j == 6) && i != 1 && i != 8){
					map[i][j] = x;
				}else if((i == 4 || i == 5) && (j == 3 || j == 4 || j == 7 || j == 8)){
					map[i][j] = x;
				}else if((j == 2 || j == 9)){
					map[i][j] = x;
				}else if((i == 1 || i == 8) && j != 1 && j != 10 && j != 2 && j != 9){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第五关的地图
	 */
	public void initEightMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 2; j < yCount - 1; j++) {
				if(j - i == 1){
					map[i][j] = x;
				}else if(j + i == 10){
					map[i][j] = x;
				}else if((j == 2 || j == 9) && i != 1 && i != 8){
					map[i][j] = x;
				}else if((i == 1 || i == 8) && j != 1 && j != 10 && j != 2 && j != 9){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第六关的地图
	 */
	public void initNightMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 2; j < yCount - 1; j++) {
				if((j == 2 || j == 9) && i >= 3 && i <= 6){
					map[i][j] = x;
				}else if((i == 1 || i == 8) && j >= 4 && j <= 7){
					map[i][j] = x;
				}else if(j == 3 && (i == 2 || i == 7)){
					map[i][j] = x;
				}else if(j == 8 && (i == 2 || i == 7)){
					map[i][j] = x;
				}else if((j >= 3 && j <= 8) && (i == 4 || i == 5)){
					map[i][j] = x;
				}else if((i >= 2 && i <= 7) && (j == 5 || j == 6)){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第七关的地图
	 */
	public void initTenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 2; j < yCount - 1; j++) {
				if(j == 2){
					map[i][j] = x;
				}else if(j == 3){
					map[i][j] = x;
				}else if(j == 4){
					map[i][j] = x;
				}else if(j == 5){
					map[i][j] = x;
				}else if(j == 6 && (i != 1 && i != 8) ){
					map[i][j] = x;
				}else if(j == 7 && (i >= 3 && i <= 6) ){
					map[i][j] = x;
				}else if(j == 8 && (i == 4 || i == 5)){
					map[i][j] = x;
				}else{
					map[i][j] = 0;
					continue;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	
	/**
	 * 生成第八关的地图
	 */
	public void initElevenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(i == 2 && j >=3 && j<=8){
					map[i][j] = 0;
					continue;
				}else if(j == 3 && i >= 2 && i<=7){
					map[i][j] = 0;
					continue;
				}else if(i == 7 && j >=3 && j<=8){
					map[i][j] = 0;
					continue;
				}else if(j == 8 && i >=2 && i<=7){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第九关的地图
	 */
	public void initTwelveMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 3; j < yCount - 2; j++) {
				if(i != 2 && i != 7 && j == 3){
					map[i][j] = 0;
					continue;
				}else if(j == 4 && (i == 4 || i == 5)){
					map[i][j] = 0;
					continue;
				}else if((i == 1 || i == 8) && j == 7){
					map[i][j] = 0;
					continue;
				}else if(j == 8 && (i == 1 || i == 8 || i == 2 || i == 7)){
					map[i][j] = 0;
					continue;
				}else if(j == 9 && (i == 1 || i == 8 || i == 2 || i == 7 || i == 3 || i == 6)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	
	/**
	 * 生成第八关的地图
	 */
	public void initThirteenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(i == 4 || i == 5){
					map[i][j] = 0;
					continue;
				}else if((j == 5 || j == 6) && i != 4 && i != 5){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第八关的地图
	 */
	public void initFourteenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if((i == 5 || i == 6) && j != 9 && j != 10){
					map[i][j] = 0;
					continue;
				}else if((i == 3 || i == 4) && (j == 8 || j == 9 || j == 10)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第九关的地图
	 */
	public void initFifteenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(j == 3){
					map[i][j] = 0;
					continue;
				}else if(j == 8){
					map[i][j] = 0;
					continue;
				}else if((i == 1 || i == 4 || i == 5 || i == 8 ) && j <= 7 && j >= 4){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	
	/**
	 * 生成第八关的地图
	 */
	public void initSixteenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(j == 5 || j == 6){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第八关的地图
	 */
	public void initSeventeenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if((i == 2 || i== 3) && j != 10){
					map[i][j] = 0;
					continue;
				}else if((i == 4 || i== 5) && (j == 8 || j== 9)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第八关的地图
	 */
	public void initEighteenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(i + j == 11){
					map[i][j] = 0;
					continue;
				}else if(i == 6 && j == 1){
					map[i][j] = 0;
					continue;
				}else if(i == 7 && j == 2){
					map[i][j] = 0;
					continue;
				}else if((i == 4 && j < 7) || (i == 5 && j < 6)){
					map[i][j] = 0;
					continue;
				}else if(i == 6 && j == 2){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第八关的地图
	 */
	public void initNineteenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(i + j == 11){
					map[i][j] = 0;
					continue;
				}else if(i == 6 && j == 1){
					map[i][j] = 0;
					continue;
				}else if(i == 7 && j == 2){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第八关的地图
	 */
	public void initTwentyMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(i >= 3 && i <= 6 && j >= 4 && j <= 6){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第八关的地图
	 */
	public void initTwentyOneMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if((i == 1 && j == 1) || (i == 2 && j == 1) || (i == 2 && j == 2) || (i == 1 && j == 2)){
					map[i][j] = 0;
					continue;
				}else if((i == 7 && j == 1) || (i == 8 && j == 1) || (i == 7 && j == 2) || (i == 8 && j == 2)){
					map[i][j] = 0;
					continue;
				}else if((i == 1 && j == 9) || (i == 2 && j == 9) || (i == 1 && j == 10) || (i == 2 && j == 10)){
					map[i][j] = 0;
					continue;
				}else if((i == 7 && j == 9) || (i == 8 && j == 9) || (i == 7 && j == 10) || (i == 8 && j == 10)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第八关的地图
	 */
	public void initTwentyTwoMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if((i == 4 && j == 1) || (i == 5 && j == 1) || (i == 1 && j == 5) || (i == 1 && j == 6)){
					map[i][j] = 0;
					continue;
				}else if((i == 4 && j == 10) || (i == 5 && j == 10) || (i == 8 && j == 5) || (i == 8 && j == 6)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二十一关的地图
	 */
	public void initTwentyThreeMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(i == j){
					map[i][j] = 0;
					continue;
				}else if(i + j == 9){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二十一关的地图
	 */
	public void initTwentyFourMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(j == 3 || j == 8){
					map[i][j] = 0;
					continue;
				}else if((i == 4 || i == 5) && j >= 4 && j <= 7){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二十三关的地图
	 */
	public void initTwentyFiveMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if((i == 1 || i == 8) && j == 1){
					map[i][j] = 0;
					continue;
				}else if((i >= 2 && i <= 7) && j == 2){
					map[i][j] = 0;
					continue;
				}else if((i >= 2 && i <= 7) && j == 9){
					map[i][j] = 0;
					continue;
				}else if((i == 1 || i == 8) && j == 10){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二十六关的地图
	 */
	public void initTwentySixMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(j == 5){
					map[i][j] = 0;
					continue;
				}else if(j == 4 && (i == 4 || i == 5)){
					map[i][j] = 0;
					continue;
				}else if(j == 6 && (i == 4 || i == 5)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二十三关的地图
	 */
	public void initTwentySevenMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if((i == 2 || i == 7) && j != 4 && j != 5 && j != 6 && j != 7){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二十三关的地图
	 */
	public void initTwentyEightMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if(((i == 3 || i == 6) && j == 1) || (i >= 3 && i <= 6 && j == 2)){
					map[i][j] = 0;
					continue;
				}else if(((i == 3 || i == 6) && j == 10) || (i >= 3 && i <= 6 && j == 9)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	/**
	 * 生成第二十九关的地图
	 */
	public void initTwentyNineMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				if((i == 3 && j == 4) || (i == 4 && j == 5) || (i == 5 && j == 6)
						|| (i == 6 && j == 7)){
					map[i][j] = 0;
					continue;
				}else if((i == 6 && j == 4) || (i == 5 && j == 5) || (i == 4 && j == 6)
						|| (i == 3 && j == 7)){
					map[i][j] = 0;
					continue;
				}else{
					map[i][j] = x;
				}
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}
	
	
	/**
	 * 生成第三十关的地图
	 */
	public void initThirtyMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				map[i][j] = x;
				
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}

	private void change() {
		Random random = new Random();
		int tmpV, tmpX, tmpY;
		for (int x = 1; x < xCount - 1; x++) {
			for (int y = 1; y < yCount - 1; y++) {
				tmpX = 1 + random.nextInt(xCount - 2);
				tmpY = 1 + random.nextInt(yCount - 2);
				if(map[x][y] == 0){
					continue;
				}
				tmpV = map[x][y];
				
				if(map[tmpX][tmpY] == 0){
					continue;
				}
				map[x][y] = map[tmpX][tmpY];
				map[tmpX][tmpY] = tmpV;
			}
		}
		if (die()) {
			change();
		}
		GameView.this.invalidate();
	}

	public void setMode(int stateMode) {
		this.stateListener.OnStateChanged(stateMode);
	}

	private boolean die() {
		for (int y = 1; y < yCount - 1; y++) {
			for (int x = 1; x < xCount - 1; x++) {
				if (map[x][y] != 0) {
					for (int j = y; j < yCount - 1; j++) {
						if (j == y) {
							for (int i = x + 1; i < xCount - 1; i++) {
								if (map[i][j] == map[x][y]
										&& link(new Point(x, y),
												new Point(i, j))) {
									return false;
								}
							}
						} else {
							for (int i = 1; i < xCount - 1; i++) {
								if (map[i][j] == map[x][y]
										&& link(new Point(x, y),
												new Point(i, j))) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	List<Point> p1E = new ArrayList<Point>();
	List<Point> p2E = new ArrayList<Point>();

	private boolean link(Point p1, Point p2) {
		if (p1.equals(p2)) {
			return false;
		}
		path.clear();
		if (map[p1.x][p1.y] == map[p2.x][p2.y]) {
			if (linkD(p1, p2)) {
				path.add(p1);
				path.add(p2);
				return true;
			}

			Point p = new Point(p1.x, p2.y);
			if (map[p.x][p.y] == 0) {
				if (linkD(p1, p) && linkD(p, p2)) {
					path.add(p1);
					path.add(p);
					path.add(p2);
					return true;
				}
			}
			p = new Point(p2.x, p1.y);
			if (map[p.x][p.y] == 0) {
				if (linkD(p1, p) && linkD(p, p2)) {
					path.add(p1);
					path.add(p);
					path.add(p2);
					return true;
				}
			}
			expandX(p1, p1E);
			expandX(p2, p2E);

			for (Point pt1 : p1E) {
				for (Point pt2 : p2E) {
					if (pt1.x == pt2.x) {
						if (linkD(pt1, pt2)) {
							path.add(p1);
							path.add(pt1);
							path.add(pt2);
							path.add(p2);
							return true;
						}
					}
				}
			}

			expandY(p1, p1E);
			expandY(p2, p2E);
			for (Point pt1 : p1E) {
				for (Point pt2 : p2E) {
					if (pt1.y == pt2.y) {
						if (linkD(pt1, pt2)) {
							path.add(p1);
							path.add(pt1);
							path.add(pt2);
							path.add(p2);
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}

	private boolean linkD(Point p1, Point p2) {
		if (p1.x == p2.x) {
			int y1 = Math.min(p1.y, p2.y);
			int y2 = Math.max(p1.y, p2.y);
			boolean flag = true;
			for (int y = y1 + 1; y < y2; y++) {
				if (map[p1.x][y] != 0) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return true;
			}
		}
		if (p1.y == p2.y) {
			int x1 = Math.min(p1.x, p2.x);
			int x2 = Math.max(p1.x, p2.x);
			boolean flag = true;
			for (int x = x1 + 1; x < x2; x++) {
				if (map[x][p1.y] != 0) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return true;
			}
		}
		return false;
	}

	private void expandX(Point p, List<Point> l) {
		l.clear();
		for (int x = p.x + 1; x < xCount; x++) {
			if (map[x][p.y] != 0) {
				break;
			}
			l.add(new Point(x, p.y));
		}
		for (int x = p.x - 1; x >= 0; x--) {
			if (map[x][p.y] != 0) {
				break;
			}
			l.add(new Point(x, p.y));
		}
	}

	private void expandY(Point p, List<Point> l) {
		l.clear();
		for (int y = p.y + 1; y < yCount; y++) {
			if (map[p.x][y] != 0) {
				break;
			}
			l.add(new Point(p.x, y));
		}
		for (int y = p.y - 1; y >= 0; y--) {
			if (map[p.x][y] != 0) {
				break;
			}
			l.add(new Point(p.x, y));
		}
	}

	private boolean win() {
		for (int x = 0; x < xCount; x++) {
			for (int y = 0; y < yCount; y++) {
				if (map[x][y] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 道具自动连
	 */
	public void autoClear() {
		if (Help == 0) {
			if(soundPlay != null)
				soundPlay.play(ID_SOUND_ERROR, 0);
		}else{
			if(soundPlay != null)
				soundPlay.play(ID_SOUND_TIP, 0);
			Help--;
			toolsChangedListener.onTipChanged(Help);
			drawLine(path.toArray(new Point[] {}));
			refreshHandler.sleep(100);
		}
	}
	
	/**
	 * 道具重排
	 */
	public void refreshChange(){
		if(Refresh == 0){
			if(soundPlay != null)
				soundPlay.play(ID_SOUND_ERROR, 0);
			return;
		}else{
//			soundPlay.play(ID_SOUND_REFRESH, 0);
			if(soundPlay != null)
				soundPlay.play(ID_SOUND_TIP, 0);
			Refresh--;
			toolsChangedListener.onRefreshChanged(Refresh);
			change();
		}
	}
	
	/**
	 * 道具自动找
	 */
	public void autoFind() {
		if (Find == 0) {
			if(soundPlay != null)
				soundPlay.play(ID_SOUND_ERROR, 0);
		}else{
			if(soundPlay != null)
				soundPlay.play(ID_SOUND_TIP, 0);
			Find--;
			toolsChangedListener.onFindChanged(Find);
			tip(path.toArray(new Point[]{}));
			refreshHandler.sleep(100);
		}
	}

	public Handler handler;
	public void setHanlder(Handler handler) {
		this.handler = handler;
		
	}

	public int getRefeshTimeCount() {
		return refeshTimeCount;
	}

	public void setRefeshTimeCount(int refeshTimeCount) {
		this.refeshTimeCount = refeshTimeCount;
	}
}
