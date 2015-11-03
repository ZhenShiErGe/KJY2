package com.xyz.kjy.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.MyDatabaseHelper;
import com.xyz.kjy.net.HttpClientCenter;
import com.xyz.kjy.utils.MySharedPreferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerInfoActivity extends FragmentActivity {
	private TextView tvStoreName;
	private TextView tvStoreBoss;
	private TextView tvProduction;
	private TextView tvUnitPrice;
	private TextView tvAddress;
	private Button btnPhone;
	private Button btnPutOff;
	
	private String storeName;
//	private ImageView backToMainActivity;
	private LinearLayout backToMainActivity;
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
		btnPutOff=(Button) findViewById(R.id.btn_putoff);
		backToMainActivity= (LinearLayout) findViewById(R.id.customerinfo_back_main);
		
		storeName=intent.getStringExtra(MyDatabaseHelper.STORENAME);
		
		tvStoreName.setText(storeName);
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
		
		btnPutOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
//				if(HttpClientCenter.getCookie().size()!=0)
//					client.setCookieStore(HttpClientCenter.getCookieStore());
//				client.get(Constants.DispatchInfoURI, new JsonHttpResponseHandler(){
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							JSONObject response) {
//						boolean result=false;
//						try{
//							result=response.getBoolean("isSuccess");
//						}catch(JSONException e){
//							Log.e("TAG",e.getMessage());
//							Toast.makeText(CustomerInfoActivity.this, "获取配送信息失败", Toast.LENGTH_SHORT).show();
//						}
//						if(result){
//							try{
//								String jsonString=response.getString("content");
//								JSONObject json=new JSONObject(jsonString);
//								String jsonString1=json.getString("dispatchInfo");
//								if(!"".equals(jsonString1)){
//									Intent intent=new Intent(CustomerInfoActivity.this,PutOffActivity.class);
//									intent.putExtra(MyDatabaseHelper.STORENAME,storeName);
//									startActivity(intent);
//									CustomerInfoActivity.this.overridePendingTransition(R.anim.push_left_in,
//											R.anim.push_left_out);
//								}else{
//									Toast.makeText(CustomerInfoActivity.this, "请先开始一次配送", Toast.LENGTH_SHORT).show();
//								}
//							}catch(JSONException e){
//								Log.e("TAG",e.getMessage());
//								Toast.makeText(CustomerInfoActivity.this, "获取配送信息失败", Toast.LENGTH_SHORT).show();
//							}
//						}
//						else {
//							try{
//								String errorMesg=response.getString("errorMesg");
//								if("未登陆，请先登陆".equals(errorMesg)){
//									MySharedPreferences.putBoolean(CustomerInfoActivity.this,Constants.UserIsLogin, false);
//									Intent intent=new Intent(CustomerInfoActivity.this,LoginActivity.class);
//									startActivity(intent);
//								}else{
//									Toast.makeText(CustomerInfoActivity.this, errorMesg, Toast.LENGTH_SHORT).show();
//								}
//							}catch(JSONException e){
//								Log.e("TAG",e.getMessage());
//								Toast.makeText(CustomerInfoActivity.this, "获取配送信息失败", Toast.LENGTH_SHORT).show();
//							}
//						}
//					}
//					
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							Throwable throwable, JSONObject errorResponse) {
//						Toast.makeText(CustomerInfoActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
//					}
//				});
//				
				boolean flag=MySharedPreferences.getBoolean(CustomerInfoActivity.this,Constants.DispatchIsDoing, false);
				if(flag){
					Intent intent=new Intent(CustomerInfoActivity.this,PutOffActivity.class);
					intent.putExtra(MyDatabaseHelper.STORENAME,storeName);
					startActivity(intent);
					CustomerInfoActivity.this.overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				}else{
					Toast.makeText(CustomerInfoActivity.this, "请先开始一次配送", Toast.LENGTH_SHORT).show();
				}
					
			}
		});
	}
}
