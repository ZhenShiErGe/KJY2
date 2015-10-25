package com.xyz.kjy.activity;

import com.example.kjy.R;
import com.xyz.kjy.db.MyDatabaseHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerInfoActivity extends FragmentActivity {
	private TextView tvStoreName;
	private TextView tvStoreBoss;
	private TextView tvProduction;
	private TextView tvUnitPrice;
	private TextView tvAddress;
	private Button btnPhone;
	
	private ImageView backToMainActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customerinfo);
		//在所有activity中添加该行代码，用于需要时退出应用程序
		SystemApplication.getInstance().addActivity(this);
		Intent intent=this.getIntent();
		
		tvStoreName=(TextView) findViewById(R.id.txt_storename);
		tvStoreBoss=(TextView) findViewById(R.id.txt_storeboss);
		tvProduction=(TextView) findViewById(R.id.txt_storeProduct);
		tvUnitPrice=(TextView) findViewById(R.id.txt_unitprice);
		tvAddress=(TextView) findViewById(R.id.txt_storeaddress);
		btnPhone=(Button) findViewById(R.id.btn_callstore);
		backToMainActivity=(ImageView) findViewById(R.id.customerinfo_back_main);
		
		tvStoreName.setText(intent.getStringExtra(MyDatabaseHelper.STORENAME));
		tvStoreBoss.setText(intent.getStringExtra(MyDatabaseHelper.STOREOWNERNAME));
		tvProduction.setText(intent.getStringExtra(MyDatabaseHelper.PRODUCTIONNAME));
		tvUnitPrice.setText(intent.getStringExtra(MyDatabaseHelper.UNITPRICE));
		tvAddress.setText(intent.getStringExtra(MyDatabaseHelper.ADDRESS));
		
		final String phoneNumber=intent.getStringExtra(MyDatabaseHelper.PHONE);
		
		backToMainActivity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomerInfoActivity.this.finish();
				CustomerInfoActivity.this.overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				
			}
		});
		
		btnPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				//添加确认对话框
				final Dialog dialog=new AlertDialog.Builder(CustomerInfoActivity.this)
				.setMessage("号码："+phoneNumber)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.cancel();
					}
				})
				.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNumber));
						startActivity(intent);
					}
				})
				.create();
				dialog.show();
			}
		});
	}
}
