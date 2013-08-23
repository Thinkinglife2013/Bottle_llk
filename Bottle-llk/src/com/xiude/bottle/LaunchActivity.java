package com.xiude.bottle;

import com.xiude.bottle.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LaunchActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_view);
		
				
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(LaunchActivity.this, FirstActivity.class));
				finish();
			}
		}, 1000);
		
	}
}
