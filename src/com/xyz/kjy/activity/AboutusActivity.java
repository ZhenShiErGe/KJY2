package com.xyz.kjy.activity;

import com.example.kjy.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AboutusActivity extends Activity {
//	private ImageView btnback;
	private LinearLayout btnback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_aboutus);
		super.onCreate(savedInstanceState);
		//在所有activity中添加该行代码，用于需要时退出应用程序
		SystemApplication.getInstance().addActivity(this);
		
		btnback= (LinearLayout) findViewById(R.id.aboutus_back_main);
		
		btnback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				Intent intent =new Intent(AboutusActivity.this,MainActivity.class);
//				startActivity(intent);
				AboutusActivity.this.finish();
				AboutusActivity.this.overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
	}
}
