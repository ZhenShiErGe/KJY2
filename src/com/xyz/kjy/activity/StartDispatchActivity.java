package com.xyz.kjy.activity;

import org.apache.http.Header;
import org.json.JSONException;

import com.example.kjy.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.net.HttpClientCenter;
import com.xyz.kjy.utils.MySharedPreferences;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartDispatchActivity extends FragmentActivity {
	private TextView tvDispatchPerson;
	private TextView tvDispatchCar;
	private Button startDispatch;
	private ImageView backToMainActivity;
	//点击确认即可添加新的配送信息（注意同一个时间一个用户只能有一个正在配送的记录）
	//添加成功后返回到主页面
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startdispatch);
		
		tvDispatchPerson=(TextView) findViewById(R.id.txt_dispatchPerson);
		tvDispatchCar=(TextView) findViewById(R.id.txt_dispatchCar);
		startDispatch=(Button) findViewById(R.id.btn_dispatchStart);
		backToMainActivity=(ImageView) findViewById(R.id.startdispatch_back_main);
		
		tvDispatchPerson.setText(MySharedPreferences.getString(StartDispatchActivity.this,
				Constants.UserName, ""));
		backToMainActivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(StartDispatchActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		startDispatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				//需要发送的参数
				RequestParams params=new RequestParams();
				params.put("carNum", tvDispatchCar.getText().toString().trim());
				//显示正在登录处理对话窗
				final ProgressDialog progressDialog=new ProgressDialog(StartDispatchActivity.this);
				progressDialog.setMessage(Constants.StartingDispatch);
				progressDialog.show();
				
				SyncHttpClient client=HttpClientCenter.getSyncHttpClient();
				if(HttpClientCenter.getCookie().size()!=0)
					client.setCookieStore(HttpClientCenter.getCookieStore());
				client.post(Constants.CustomersURI,params,new JsonHttpResponseHandler(){
					public void onSuccess(int statusCode,Header[] headers, org.json.JSONObject response) {
						boolean result=false;
						try{
							result=response.getBoolean("isSuccess");
						}catch(JSONException e){
							Log.e("TAG","内部错误："+e.getMessage());
							Toast.makeText(StartDispatchActivity.this, "创建配送记录失败", Toast.LENGTH_SHORT).show();
						}
						if(result){
			     			Intent intent =new Intent(StartDispatchActivity.this,MainActivity.class);
			     			startActivity(intent);
						}
						else {
							progressDialog.dismiss();
							try{
								String errorMesg=response.getString("errorMesg");
								Toast.makeText(StartDispatchActivity.this, "创建配送记录失败", Toast.LENGTH_SHORT).show();
							}catch(JSONException e){
								Log.e("TAG","内部错误："+e.getMessage());
								Toast.makeText(StartDispatchActivity.this, "创建配送记录失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						progressDialog.dismiss();
						Log.e("TAG",throwable.getMessage());
						Toast.makeText(StartDispatchActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
}
