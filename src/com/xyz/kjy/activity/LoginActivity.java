package com.xyz.kjy.activity;

import org.apache.http.Header;

import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.utils.MySharedPreferences;
import com.xyz.kjy.net.HttpClientCenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity  {

	private Button btn_login;
	private EditText et_username, et_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		init();
	}
	private void init() {
		btn_login = (Button) findViewById(R.id.btn_login);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		
		et_username.addTextChangedListener(new TextChange());
		et_password.addTextChangedListener(new TextChange());
		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getLogin();
			}
		});
		
	}

	private void getLogin() {
		final String userName = et_username.getText().toString().trim();
		final String password = et_password.getText().toString().trim();
		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
			//需要发送的参数
			RequestParams params=new RequestParams();
			params.put("userName", userName);
			params.put("password", password);
			//显示正在登录处理对话窗
			final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage(Constants.BeingLogin);
			progressDialog.show();
			//开始发送请求
			AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
			HttpClientCenter.clearCookie();
			HttpClientCenter.saveCookie(client);
			client.post(Constants.LoginURI, params, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
					boolean result=false;
					try{
						result=response.getBoolean("isSuccess");
					}catch(JSONException e){
						Log.e("TAG","内部错误："+e.getMessage());
						Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					}
					if(result){
						String cookie=getCookieFromHeaders(headers);
						MySharedPreferences.putString(LoginActivity.this,Constants.Cookie ,cookie);
		        		MySharedPreferences.putString(LoginActivity.this,Constants.UserName, userName);
		     			MySharedPreferences.putString(LoginActivity.this,Constants.UserPass, password);
//		     			MySharedPreferences.putString(LoginActivity.this,Constants.UserPass, DES.md5Pwd(password));
		     			MySharedPreferences.putBoolean(LoginActivity.this,Constants.UserIsLogin,true);
		     			Intent intent =new Intent(LoginActivity.this,MainActivity.class);
		     			startActivity(intent);
		     			progressDialog.dismiss();
		     			
					}
					else {
						progressDialog.dismiss();
						try{
							String errorMesg=response.getString("errorMesg");
							Toast.makeText(LoginActivity.this, "登录失败:"+errorMesg, Toast.LENGTH_SHORT).show();
						}catch(JSONException e){
							Log.e("TAG","内部错误："+e.getMessage());
							Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
						}
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					progressDialog.dismiss();
					Log.e("TAG",throwable.getMessage());
					Toast.makeText(LoginActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	// EditText监听器
	class TextChange implements TextWatcher {
		@Override
		public void afterTextChanged(Editable arg0) {
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}
		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			boolean Sign2 = et_username.getText().length() > 0;
			boolean Sign3 = et_password.getText().length() >= 6;
			if (Sign2 & Sign3) {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_login.setEnabled(true);
			} else {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_login.setTextColor(0xFFD0EFC6);
				btn_login.setEnabled(false);
			}
		}
	}
	/**
	 * 从返回的报文头中获取后台要正需要的cookie
	 * 具体格式为 __VCAP_ID__=6b946ac2d99041d28e0c6ca07925380a2d9c5471436f462dbc68b4315d145803;	JSESSIONID=F0B63DE37C2B2BDFD7D6C413C4CF6780;	
	 * @param headers
	 * @return
	 */
	private String getCookieFromHeaders(Header[] headers){
		StringBuffer cookie=new StringBuffer();
		for(Header header:headers){
			if("Set-Cookie".equals(header.getName())){
				int temp=header.getValue().indexOf(";");
				cookie.append(header.getValue().substring(0, temp));
				cookie.append("; ");
			}
		}
		return cookie.toString();
	}

}

/**
 * 使用volley操作的代码
 */
//JsonObjectPostRequest jsonObjectPostRequest =
//new JsonObjectPostRequest(Constants.LoginURI, new Response.Listener<JSONObject>() {
//
//	@Override
//	public void onResponse(JSONObject response) {
//		// TODO Auto-generated method stub
//		boolean result=false;
//		try{
//			result=response.getBoolean("isSuccess");
//		}catch(JSONException e){
//			Log.e("TAG",e.getMessage());
//			progressDialog.dismiss();
//			Toast.makeText(LoginActivity.this, "登录失败,内部错误", Toast.LENGTH_SHORT).show();
//		}
//		if(result){
//			try{
//				String cookie=response.getString("Cookie");
//				MySharedPreferences.putString(LoginActivity.this,Constants.Cookie ,cookie);
//			}catch(JSONException e){
//				Log.e("TAG",e.getMessage());
//				progressDialog.dismiss();
//				Toast.makeText(LoginActivity.this, "登录失败,内部错误", Toast.LENGTH_SHORT).show();
//			}
//    		MySharedPreferences.putString(LoginActivity.this,Constants.UserName, userName);
// 			MySharedPreferences.putString(LoginActivity.this,Constants.UserPass, password);
//// 			MySharedPreferences.putString(LoginActivity.this,Constants.UserPass, DES.md5Pwd(password));
// 			MySharedPreferences.putBoolean(LoginActivity.this,Constants.UserIsLogin,true);
// 			progressDialog.dismiss();
// 			Intent intent =new Intent(LoginActivity.this,MainActivity.class);
// 			startActivity(intent);
//		}
//		else {
//			progressDialog.dismiss();
//			try{
//				String errorMesg=response.getString("errorMesg");
//				Toast.makeText(LoginActivity.this, "登录失败:"+errorMesg, Toast.LENGTH_SHORT).show();
//			}catch(JSONException e){
//				Log.e("TAG",e.getMessage());
//				Toast.makeText(LoginActivity.this, "登录失败,内部错误", Toast.LENGTH_SHORT).show();
//			}
//			
//		}
//	}
//},new Response.ErrorListener() {
//     @Override
//     public void onErrorResponse(VolleyError volleyError) {                   
//    	 progressDialog.dismiss();
//         Toast.makeText(LoginActivity.this, "网络不可用，登录失败！", Toast.LENGTH_SHORT).show();
//     }
// }, params);
//////对于每次请求，加上本地存储的cookie用于验证身份
////String localCookie = MySharedPreferences.getString(LoginActivity.this,Constants.Cookie, "");
////if(!localCookie.equals("")){
//// jsonObjectPostRequest.setSendCookie(localCookie);//向服务器发起post请求时加上cookie字段
////}
//RequestCenter.getRequestQueue().add(jsonObjectPostRequest);
