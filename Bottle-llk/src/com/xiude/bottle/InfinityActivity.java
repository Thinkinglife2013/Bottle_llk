package com.xiude.bottle;

import com.xiude.bottle.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfinityActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infinite);
		
		ImageView back = (ImageView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});		
		
		TextView topLevel = (TextView)findViewById(R.id.toplevel);
		TextView topIntegral = (TextView)findViewById(R.id.topintegral);
		
		Intent i = getIntent();
		int integral = i.getIntExtra("topIntegral", 0);
		int level = i.getIntExtra("toplevel", 1);
		topIntegral.setText(getString(R.string.top_integral).replace("$", String.valueOf(integral)));
		topLevel.setText(getString(R.string.top_level).replace("$", String.valueOf(level)));
		
		Button play = (Button)findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InfinityActivity.this, WelActivity.class);
				i.putExtra("num", 1);
				startActivity(i);
				finish();
			}
		});

	}
}
