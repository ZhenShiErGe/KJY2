package com.xyz.kjy.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.MyDatabaseHelper;
import com.xyz.kjy.net.HttpClientCenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PayActivity extends Activity {
	
	private TextView payHint;
	private Button payok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pay);
		super.onCreate(savedInstanceState);
		//在所有activity中添加该行代码，用于需要时退出应用程序
		SystemApplication.getInstance().addActivity(this);
		
		payHint=(TextView) findViewById(R.id.txt_payhint);
		payok=(Button) findViewById(R.id.btnpayok);
		
		int type=this.getIntent().getIntExtra("payType",1);
		int num=this.getIntent().getIntExtra("payNum",0);
		String storeName=this.getIntent().getStringExtra("storeName");
		if(type==1){
			payHint.setText("请向商家  "+storeName+" 收取  "+(num/100.0)+"元   现金");
		}
		else if(type==2){
			payHint.setText("请向商家  "+storeName+" 收取  "+(num/100.0)+"元  签单");
		}
		
		payok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent(PayActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
	}
}
