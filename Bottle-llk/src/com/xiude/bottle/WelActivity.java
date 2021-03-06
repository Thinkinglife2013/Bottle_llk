package com.xiude.bottle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiude.util.FailDialog;
import com.xiude.util.WinDialog;
import com.xiude.view.BoardView;
import com.xiude.view.GameView;
import com.xiude.view.OnStateListener;
import com.xiude.view.OnTimerListener;
import com.xiude.view.OnToolsChangeListener;

public class WelActivity extends Activity implements OnClickListener,
		OnTimerListener, OnStateListener, OnToolsChangeListener {

	// private ImageButton btnPlay;
	private ImageButton btnRefresh;

	private ImageButton btnTip;
	private ImageButton btnFind;
	private ImageButton btnPause;
	// private ImageView imgTitle;
	private GameView gameView;
	private SeekBar progress;
	// private MyDialog dialog;
	// private ImageView clock;
	private ImageView textRefreshNum;
	private ImageView textTipNum;
	private ImageView textFindNum;
	private int guan; // 当前第几关
	private int curTopIntegral; // 当前总积分
	private TextView customIntegralView; // 第一种经典模式的积分（右上角）

	// 播放游戏前音乐的player
	public static MediaPlayer player;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			// 胜利
			case 0: {
//				BoardView.toast.cancel();
				
				int maxCount = Constants.maxCount; // 最大连击数
				Log.i("progress", progress.getMax() + "");

				// 注意progress是剩余时间
				int integral = progress.getProgress() / 10 * 3;
				integral += (maxCount * 10); // 当前这一关的积分

				WinDialog dialog = new WinDialog(WelActivity.this, gameView,
						gameView.getRefeshTimeCount() * 100 / 1000, integral,
						guan, maxCount, progress);
				LayoutParams lay = dialog.getWindow().getAttributes();
				setParams(lay);
				dialog.show();

				if (FirstActivity.gameMode != 3) { // 第一二种模式，保存最高关数

					SharedPreferences levelPreference = getSharedPreferences(
							"level", 0);
					int level = 1;
					if (FirstActivity.gameMode == 1) {
						level = levelPreference.getInt("custom_level", 1);
					} else if (FirstActivity.gameMode == 2) {
						level = levelPreference.getInt("challenge_level", 1);
					}
					if (level <= (guan + 1)) {
						if (FirstActivity.gameMode == 1) {
							levelPreference.edit()
									.putInt("custom_level", guan + 1).commit();
						} else if (FirstActivity.gameMode == 2) {
							levelPreference.edit()
									.putInt("challenge_level", guan + 1)
									.commit();
						}
					}
				} else {// 第三种模式，判断是否保存最高关数和积分
					SharedPreferences infinitePreference = getSharedPreferences(
							"infinite", 0);
					int topLevel = infinitePreference.getInt("top_level", 1);
					if (guan + 1 > topLevel) {
						infinitePreference.edit().putInt("top_level", guan + 1)
								.commit();

						// 历史总积分
						int topIntegral = infinitePreference.getInt(
								"top_integral", 0);

						// 当前总积分
						curTopIntegral += integral;
						if (curTopIntegral > topIntegral) {
							infinitePreference.edit()
									.putInt("top_integral", curTopIntegral)
									.commit();
						}
					}
				}

				break;
			}
			// 失败
			case 1: {
				BoardView.toast.cancel();
				
				FailDialog dialog = new FailDialog(WelActivity.this, gameView,
						guan, -1);
				LayoutParams lay = dialog.getWindow().getAttributes();
				setParams(lay);
				dialog.show();

			}
			case 2:
				// fatherLayout.setVisibility(View.VISIBLE);
				int guanNum = msg.arg1;
				guan = guanNum;

				// 经典模式，初始得分为0
				gameView.setCustomIntegral(0);
				gameView.getCustomIntegralView().setText("0");

				// 如果当前为第一关，积分置为0
				if (guanNum == 1) {
					curTopIntegral = 0;
				}
				
				if(guanNum > 0)
					setLevel(guanNum + "", levelOneView, levelTwoView);
			}
		}
	};

	ImageView levelOneView;
	ImageView levelTwoView;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		BgMediaPlayer.pauseMedia(); // 暂停背景音乐的播放

		customIntegralView = (TextView) findViewById(R.id.custom_integral);

		btnRefresh = (ImageButton) findViewById(R.id.refresh_btn);
		btnRefresh.setVisibility(View.INVISIBLE);

		btnTip = (ImageButton) findViewById(R.id.tip_btn);
		btnTip.setVisibility(View.INVISIBLE);

		gameView = (GameView) findViewById(R.id.game_view);

		// 用以显示得分
		gameView.setCustomIntegralView(customIntegralView);
		customIntegralView.setVisibility(View.INVISIBLE);

		progress = (SeekBar) findViewById(R.id.timer);
		textRefreshNum = (ImageView) findViewById(R.id.text_refresh_num);
		textRefreshNum.setVisibility(View.INVISIBLE);

		textTipNum = (ImageView) findViewById(R.id.text_tip_num);
		textTipNum.setVisibility(View.INVISIBLE);

		btnPause = (ImageButton) findViewById(R.id.pause_btn);
		btnPause.setVisibility(View.INVISIBLE);

		btnFind = (ImageButton) findViewById(R.id.find_btn);
		btnFind.setVisibility(View.INVISIBLE);

		textFindNum = (ImageView) findViewById(R.id.text_find_num);
		textFindNum.setVisibility(View.INVISIBLE);

		levelOneView = (ImageView) findViewById(R.id.num_one);
		levelTwoView = (ImageView) findViewById(R.id.num_two);
		final ImageView readyView = (ImageView) findViewById(R.id.ready_go);

		Intent i = getIntent();
		int guanNum = i.getIntExtra("num", 1);
		guan = guanNum;
		setLevel(String.valueOf(guan), levelOneView, levelTwoView);

		// 设最大时间
		// progress.setMax(gameView.getTotalTime());
		gameView.setProgress(progress);
		progress.setVisibility(View.GONE);
		gameView.setHanlder(handler);

		btnPause.setOnClickListener(this);
		btnFind.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		btnTip.setOnClickListener(this);
		gameView.setOnTimerListener(this);
		gameView.setOnStateListener(this);
		gameView.setOnToolsChangedListener(this);

		// 判断音乐是否暂停
		if (!BaseActivity.isGameBgPause) {
			GameView.initSound(this);

		}

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (GameView.soundPlay != null)
					GameView.soundPlay.play(GameView.ID_SOUND_READYGO, 0);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Animation scale = AnimationUtils.loadAnimation(
								WelActivity.this, R.anim.scale_anim);
						readyView.startAnimation(scale);
						readyView.setVisibility(View.VISIBLE);
						scale.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								
								readyView.setVisibility(View.GONE);
								// 开始游戏
								initView();
								gameView.startPlay(guan);
								if (!BaseActivity.isAllBgMusicClickPause) {
									player = MediaPlayer.create(
											WelActivity.this,
											R.raw.back2new);
									player.setLooping(true);// 设置循环播放
									player.start();
								}
							/*	Animation scaleOut = AnimationUtils
										.loadAnimation(WelActivity.this,
												R.anim.scale_anim_out);
								readyView.startAnimation(scaleOut);
								scaleOut.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {
									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {
									}

									@Override
									public void onAnimationEnd(
											Animation animation) {
										readyView.setVisibility(View.GONE);
										// 开始游戏
										initView();
										gameView.startPlay(guan);
										if (!BaseActivity.isAllBgMusicClickPause) {
											player = MediaPlayer.create(
													WelActivity.this,
													R.raw.back2new);
											player.setLooping(true);// 设置循环播放
											player.start();
										}
									}
								});*/
							}
						});

					}
				});
			}
		}).start();

		/*
		 * player = MediaPlayer.create(this, R.raw.bg);
		 * player.setLooping(true);//设置循环播放 player.start();
		 */

	}

	/**
	 * 设置每关的关数
	 * 
	 * @param guanNum
	 * @param firstGuan
	 * @param tenGuan
	 */
	public void setLevel(String guanNum, ImageView firstGuan, ImageView tenGuan) {
		Bitmap bmSrc = BitmapFactory.decodeResource(getResources(),
				R.drawable.number);

		String[] nums;
		if (guanNum.length() == 1) {
			nums = new String[] { guanNum };
		} else {
			nums = new String[2];
			nums[0] = guanNum.charAt(0) + "";
			nums[1] = guanNum.charAt(1) + "";
		}

		if (nums.length == 1) {
			Bitmap firstBmDst = Bitmap.createBitmap(bmSrc, 9 * bmSrc.getWidth() / 10, 0,
					bmSrc.getWidth() / 10, bmSrc.getHeight());
			firstGuan.setImageBitmap(firstBmDst);

			int num = Integer.parseInt(nums[0]);
			Bitmap tenBmDst = Bitmap.createBitmap(bmSrc, (num - 1) * bmSrc.getWidth()
					/ 10, 0, bmSrc.getWidth() / 10, bmSrc.getHeight());
			tenGuan.setImageBitmap(tenBmDst);
		} else {

			try {
				int num = Integer.parseInt(nums[0]);
				Bitmap bmDst = Bitmap.createBitmap(bmSrc,
						(num - 1) * bmSrc.getWidth() / 10, 0, bmSrc.getWidth() / 10,
						bmSrc.getHeight());
				firstGuan.setImageBitmap(bmDst);

				int num2 = Integer.parseInt(nums[1]);
				Bitmap bmDst2 = Bitmap.createBitmap(bmSrc,
						num2 - 1 == -1 ? 9 * bmSrc.getWidth() / 10
								: (num2 - 1) * bmSrc.getWidth() / 10, 0, bmSrc.getWidth() / 10,
						bmSrc.getHeight());
				tenGuan.setImageBitmap(bmDst2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setParams(LayoutParams lay) {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.height = dm.heightPixels;
		lay.width = dm.widthPixels;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("welActivity", "onRestart");
		restartGame();
	}

	@Override
	protected void onResume() {
		Log.i("welActivity", "onResume");
		super.onResume();
	}

	private void restartGame() {
		gameView.setVisibility(View.VISIBLE);

		if (player != null) {
			if (!player.isPlaying() && !BaseActivity.isAllBgMusicClickPause) {
				player.start();
			}
		}

		if (gameView.isStop()) {
			gameView.startTimer();
			gameView.refreshTime = gameView.new RefreshTime();
			gameView.refreshTime.start();
		}
	}

	@Override
	protected void onPause() {
		Log.i("welActivity", "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i("welActivity", "onStop");
		super.onStop();
		gameView.setMode(GameView.PAUSE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		BoardView.toast.cancel();
		
		Log.i("welActivity", "destory");
		gameView.setMode(GameView.QUIT);
		gameView.destorySound();
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		Animation scaleOut = AnimationUtils.loadAnimation(this,
				R.anim.scale_anim_out);
		Animation transIn = AnimationUtils.loadAnimation(this, R.anim.trans_in);

		// btnPlay.startAnimation(scaleOut);
		// btnPlay.setVisibility(View.GONE);
		// imgTitle.setVisibility(View.GONE);
		customIntegralView.setVisibility(View.VISIBLE);
		gameView.setVisibility(View.VISIBLE);

		btnRefresh.setVisibility(View.VISIBLE);
		btnTip.setVisibility(View.VISIBLE);
		progress.setVisibility(View.VISIBLE);
		// clock.setVisibility(View.VISIBLE);
		textRefreshNum.setVisibility(View.VISIBLE);
		textTipNum.setVisibility(View.VISIBLE);
		textFindNum.setVisibility(View.VISIBLE);
		btnFind.setVisibility(View.VISIBLE);
		btnPause.setVisibility(View.VISIBLE);

		btnRefresh.startAnimation(transIn);
		btnTip.startAnimation(transIn);
		gameView.startAnimation(transIn);
		btnFind.startAnimation(transIn);
		btnPause.startAnimation(transIn);
		// player.pause();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pause_btn:
			PauseDialog dialog = new PauseDialog(WelActivity.this, gameView,
					guan);
			LayoutParams lay = dialog.getWindow().getAttributes();
			setParams(lay);
			dialog.show();
			gameView.setVisibility(View.GONE);
			gameView.setMode(GameView.PAUSE);
			break;
		case R.id.refresh_btn:
			Animation shake01 = AnimationUtils
					.loadAnimation(this, R.anim.shake);
			btnRefresh.startAnimation(shake01);
			gameView.refreshChange();
			break;
		case R.id.tip_btn:
			Animation shake02 = AnimationUtils
					.loadAnimation(this, R.anim.shake);
			btnTip.startAnimation(shake02);
			gameView.autoClear();
			break;
		case R.id.find_btn:
			Animation shake03 = AnimationUtils
					.loadAnimation(this, R.anim.shake);
			btnFind.startAnimation(shake03);
			gameView.autoFind();
			break;
		}
	}

	@Override
	public void onTimer(int leftTime) {
		// Log.i("onTimer", leftTime+"");
		synchronized (progress) {
			progress.setProgress(leftTime);
		}
	}

	public static boolean isWinOrLose = false; //为了控制同时弹出胜利和失败框的情况

	@Override
	public void OnStateChanged(int StateMode) {

		switch (StateMode) {

		case GameView.WIN:
			//为了避免同时弹框，加上锁
			synchronized (ACCESSIBILITY_SERVICE) {
				if (!isWinOrLose) {
					isWinOrLose = true;
					handler.sendEmptyMessage(0);
				}
			}
			break;
		case GameView.LOSE:
			//为了避免同时弹框，加上锁
			synchronized (ACCESSIBILITY_SERVICE) {
				if (!isWinOrLose) {
					isWinOrLose = true;
					handler.sendEmptyMessage(1);
				}
			}
			break;
		case GameView.PAUSE:
			// player.stop();
			if (player != null && !BaseActivity.isAllBgMusicClickPause) {
				player.pause();
			}
			gameView.stopTimer();
			break;
		case GameView.QUIT:
			// player.release();
			if (player != null) {
				player.release();
			}
			gameView.stopTimer();
			break;
		}
	}

	@Override
	public void onRefreshChanged(int count) {
		// textRefreshNum.setText(""+gameView.getRefreshNum());
		int num = gameView.getRefreshNum();
		if (num == 0) {
			textRefreshNum.setBackgroundResource(R.drawable.tool0);
		} else if (num == 1) {
			textRefreshNum.setBackgroundResource(R.drawable.tool1);
		}
	}

	@Override
	public void onTipChanged(int count) {
		// textTipNum.setText(""+gameView.getTipNum());
		int num = gameView.getTipNum();
		if (num == 0) {
			textTipNum.setBackgroundResource(R.drawable.tool0);
		} else if (num == 1) {
			textTipNum.setBackgroundResource(R.drawable.tool1);
		}
	}

	@Override
	public void onFindChanged(int count) {
		int num = gameView.getFindNum();
		if (num == 0) {
			textFindNum.setBackgroundResource(R.drawable.tool0);
		} else if (num == 1) {
			textFindNum.setBackgroundResource(R.drawable.tool1);
		}
		// textFindNum.setText(""+gameView.getFindNum());
	}

	public void quit() {
		this.finish();
	}
}