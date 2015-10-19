package com.xyz.kjy.activity;

import org.apache.http.message.BasicNameValuePair;

import com.android.volley.Response;
import com.example.kjy.R;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.utils.MySharedPreferences;
import com.xyz.kjy.net.JsonObjectPostRequest;
import com.xyz.kjy.net.RequestCenter;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
			HashMap<String,String> params=new HashMap<String,String>();
			params.put("userName", userName);
			params.put("password", password);
			//显示正在登录处理对话窗
			final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage(Constants.BeingLogin);
			progressDialog.show();
			//发起请求
			JsonObjectPostRequest jsonObjectPostRequest =
					new JsonObjectPostRequest(Constants.LoginURI, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							boolean result=false;
							try{
								result=response.getBoolean("isSuccess");
							}catch(JSONException e){
								Log.e("TAG",e.getMessage());
								progressDialog.dismiss();
								Toast.makeText(LoginActivity.this, "登录失败,内部错误", Toast.LENGTH_SHORT).show();
							}
							if(result){
								try{
									String cookie=response.getString("Cookie");
									MySharedPreferences.putString(LoginActivity.this,Constants.Cookie ,cookie);
								}catch(JSONException e){
									Log.e("TAG",e.getMessage());
									progressDialog.dismiss();
									Toast.makeText(LoginActivity.this, "登录失败,内部错误", Toast.LENGTH_SHORT).show();
								}
				        		MySharedPreferences.putString(LoginActivity.this,Constants.UserName, userName);
				     			MySharedPreferences.putString(LoginActivity.this,Constants.UserPass, password);
//				     			MySharedPreferences.putString(LoginActivity.this,Constants.UserPass, DES.md5Pwd(password));
				     			MySharedPreferences.putBoolean(LoginActivity.this,Constants.UserIsLogin,true);
				     			progressDialog.dismiss();
				     			Intent intent =new Intent(LoginActivity.this,MainActivity.class);
				     			startActivity(intent);
							}
							else {
								progressDialog.dismiss();
								try{
									String errorMesg=response.getString("errorMesg");
									Toast.makeText(LoginActivity.this, "登录失败:"+errorMesg, Toast.LENGTH_SHORT).show();
								}catch(JSONException e){
									Log.e("TAG",e.getMessage());
									Toast.makeText(LoginActivity.this, "登录失败,内部错误", Toast.LENGTH_SHORT).show();
								}
								
							}
						}
					},new Response.ErrorListener() {
				         @Override
				         public void onErrorResponse(VolleyError volleyError) {                   
				        	 progressDialog.dismiss();
				             Toast.makeText(LoginActivity.this, "网络不可用，登录失败！", Toast.LENGTH_SHORT).show();
				         }
				     }, params);
//			//对于每次请求，加上本地存储的cookie用于验证身份
//			     String localCookie = MySharedPreferences.getString(LoginActivity.this,Constants.Cookie, "");
//			     if(!localCookie.equals("")){
//			         jsonObjectPostRequest.setSendCookie(localCookie);//向服务器发起post请求时加上cookie字段
//			     }
			RequestCenter.getRequestQueue().add(jsonObjectPostRequest);
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

}

