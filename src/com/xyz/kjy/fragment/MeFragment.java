/**   
 * Copyright © 2014 All rights reserved.
 * 
 * @Title: SlidingPaneContentFragment.java 
 * @Prject: SlidingPane
 * @Package: com.example.slidingpane 
 * @Description: TODO
 * @author: raot  719055805@qq.com
 * @date: 2014年9月5日 上午10:44:01 
 * @version: V1.0   
 */
package com.xyz.kjy.fragment;



import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xyz.kjy.activity.LoginActivity;
import com.xyz.kjy.activity.SystemApplication;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.net.HttpClientCenter;
import com.xyz.kjy.utils.MySharedPreferences;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MeFragment extends Fragment{
	private Activity ctx;
	private View layout;
	
	private TextView tv_userinfo;
	private TextView tv_updatepsw;
	private TextView tv_printerset;
	private TextView tv_updateversion;
	private TextView tv_aboutus;
	private Button btn_logout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		System.out.println("初始化fragment_me一次");
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_setting,
					null);
			tv_userinfo=(TextView) layout.findViewById(R.id.txt_userinfo);
			tv_updatepsw=(TextView) layout.findViewById(R.id.txt_updatepsw);
			tv_printerset=(TextView) layout.findViewById(R.id.txt_printerset);
			tv_updateversion=(TextView) layout.findViewById(R.id.txt_updateversion);
			tv_aboutus=(TextView) layout.findViewById(R.id.txt_aboutus);
			btn_logout=(Button) layout.findViewById(R.id.btnexit);
			initClickers();
		}else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}


	private void initClickers() {
		tv_userinfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		tv_updatepsw.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		tv_updateversion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		tv_printerset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		tv_aboutus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		btn_logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final ProgressDialog progressDialog=new ProgressDialog(ctx);
				progressDialog.setMessage(Constants.BeingLogout);
				progressDialog.show();
				//开始发送请求
				AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
				if(HttpClientCenter.getCookie().size()!=0)
					client.setCookieStore(HttpClientCenter.getCookieStore());
				client.post(Constants.LogoutURI,new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
						boolean result=false;
						try{
							result=response.getBoolean("isSuccess");
						}catch(JSONException e){
							Log.e("TAG","内部错误："+e.getMessage());
							Toast.makeText(ctx, "登出失败", Toast.LENGTH_SHORT).show();
						}
						if(result){
							MySharedPreferences.putBoolean(ctx, Constants.UserIsLogin,false);
			     			SystemApplication.getInstance().exit();
			     			progressDialog.dismiss();
						}
						else {
							progressDialog.dismiss();
							try{
								String errorMesg=response.getString("errorMesg");
								Toast.makeText(ctx, "登出失败:"+errorMesg, Toast.LENGTH_SHORT).show();
							}catch(JSONException e){
								Log.e("TAG","内部错误："+e.getMessage());
								Toast.makeText(ctx, "登出失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						progressDialog.dismiss();
						Toast.makeText(ctx, "请检查网络连接", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}


}
