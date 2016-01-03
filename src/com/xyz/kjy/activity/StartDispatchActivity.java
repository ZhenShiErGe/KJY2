package com.xyz.kjy.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.net.HttpClientCenter;
import com.xyz.kjy.utils.MySharedPreferences;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StartDispatchActivity extends FragmentActivity {
	private TextView tvDispatchPerson;
	private Spinner tvDispatchCar;
	private Button startDispatch;
//	private ImageView backToMainActivity;
	private LinearLayout backToMainActivity;
	
	private List<CharSequence> dataCarNum=null;
	private ArrayAdapter<CharSequence> adapterCarNum=null;
	//点击确认即可添加新的配送信息（注意同一个时间一个用户只能有一个正在配送的记录）
	//添加成功后返回到主页面
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startdispatch);
		//在所有activity中添加该行代码，用于需要时退出应用程序
		SystemApplication.getInstance().addActivity(this);
				
		tvDispatchPerson=(TextView) findViewById(R.id.txt_dispatchPerson);
		
		startDispatch=(Button) findViewById(R.id.btn_dispatchStart);
		backToMainActivity=(LinearLayout) findViewById(R.id.startdispatch_back_main);
		
		//设置车辆下拉框有关信息
		this.dataCarNum=new ArrayList<CharSequence>();
		String carNumString=this.getIntent().getStringExtra("carNums");
		
		List<String> carNums=JSONArray.parseArray(carNumString, String.class);
		for(String carNum:carNums)
			this.dataCarNum.add(carNum);
		this.adapterCarNum=new ArrayAdapter<CharSequence>
		(StartDispatchActivity.this, android.R.layout.simple_spinner_item,this.dataCarNum);
		this.adapterCarNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tvDispatchCar=(Spinner) findViewById(R.id.txt_dispatchCar);
		this.tvDispatchCar.setPrompt("请选择车辆");
		this.tvDispatchCar.setAdapter(this.adapterCarNum);
		
		
		backToMainActivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				StartDispatchActivity.this.finish();
				StartDispatchActivity.this.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});
		
		tvDispatchPerson.setText(MySharedPreferences.getString(StartDispatchActivity.this,
				Constants.UserName, ""));
		startDispatch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				//需要发送的参数
				RequestParams params=new RequestParams();
				params.put("carNum",(String)tvDispatchCar.getSelectedItem());
				
				Log.i("TAG", (String)tvDispatchCar.getSelectedItem());
				//显示正在登录处理对话窗
				final ProgressDialog progressDialog=new ProgressDialog(StartDispatchActivity.this);
				progressDialog.setMessage(Constants.BeingStartDispatch);
				progressDialog.show();
				
				AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
				if(HttpClientCenter.getCookie().size()!=0)
					client.setCookieStore(HttpClientCenter.getCookieStore());
				client.post(Constants.DispatchStartURI,params,new JsonHttpResponseHandler(){
					public void onSuccess(int statusCode,Header[] headers, org.json.JSONObject response) {
						boolean result=false;
						try{
							result=response.getBoolean("isSuccess");
						}catch(JSONException e){
							Log.e("TAG","内部错误："+e.getMessage());
							Toast.makeText(StartDispatchActivity.this, "创建配送记录失败", Toast.LENGTH_SHORT).show();
						}
						Log.i("TAG",result+"");
						if(result){
			     			Intent intent =new Intent(StartDispatchActivity.this,MainActivity.class);
			     			startActivity(intent);
			     			progressDialog.dismiss();
						}
						else {
							progressDialog.dismiss();
							try{
								String errorMesg=response.getString("errorMesg");
								if("未登陆，请先登陆".equals(errorMesg)){
									MySharedPreferences.putBoolean(StartDispatchActivity.this, Constants.UserIsLogin, false);
									Intent intent=new Intent(StartDispatchActivity.this,LoginActivity.class);
									startActivity(intent);
								}else{
									Toast.makeText(StartDispatchActivity.this, errorMesg, Toast.LENGTH_SHORT).show();
								}
							}catch(JSONException e){
								Log.e("TAG","内部错误："+e.getMessage());
								Toast.makeText(StartDispatchActivity.this, "创建配送记录失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						progressDialog.dismiss();
//						Log.e("TAG",throwable.getMessage());
						Toast.makeText(StartDispatchActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
	}
}
