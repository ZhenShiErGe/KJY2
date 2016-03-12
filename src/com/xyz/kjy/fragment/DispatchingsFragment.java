package com.xyz.kjy.fragment;

import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xyz.kjy.activity.LoginActivity;
import com.xyz.kjy.activity.MainActivity;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.DispatchInfo;
import com.xyz.kjy.net.HttpClientCenter;
import com.xyz.kjy.utils.MySharedPreferences;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DispatchingsFragment extends Fragment {
	private Activity ctx;
	private View layout;
//	private DispatchInfoFragment dispatchInfoFragment=new DispatchInfoFragment();
//	private DispatchHintFragment dispatchHintFragment=new DispatchHintFragment();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("初始化fragment_dispatch一次");
		
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_dispaching,
					null);
//			//显示hint
//			FragmentTransaction ft=ctx.getFragmentManager().beginTransaction();
//			if(!dispatchHintFragment.isAdded()){
//				ft.add(R.id.fragment_dispatch_container,dispatchHintFragment);
//			}
//			ft.show(dispatchHintFragment).commit();
//			//以上为显示hint
			initDispatchInfoFromNet();
		}else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}
	
/**
 * 如果当前配送员没有正在配送记录，空白
 * 如果有正在配送记录，显示之
 */
	private void initDispatchInfoFromNet() {
		boolean flag=MySharedPreferences.getBoolean(ctx, Constants.DispatchIsDoing, false);
		if(flag){
			DispatchInfo dispatchInfo=new DispatchInfo(MySharedPreferences.getString(ctx, Constants.DispatchCar, ""),
					MySharedPreferences.getString(ctx, Constants.DispatchStarttime, ""));
			initDispatchFragment(dispatchInfo);
		}
		else{
			AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
			if(HttpClientCenter.getCookie().size()!=0)
				client.setCookieStore(HttpClientCenter.getCookieStore());
			
			client.get(Constants.DispatchInfoURI, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					boolean result=false;
					try{
						result=response.getBoolean("isSuccess");
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(ctx, "获取配送信息失败", Toast.LENGTH_SHORT).show();
					}
					if(result){
						try{
							String jsonString=response.getString("content");
							JSONObject json=new JSONObject(jsonString);
							String jsonString1=json.getString("dispatchInfo");
							if(!"".equals(jsonString1)){
								DispatchInfo dispatchInfo=com.alibaba.fastjson.JSONObject.parseObject(jsonString1, DispatchInfo.class);
								initDispatchFragment(dispatchInfo);
								//存储到本地
								MySharedPreferences.putBoolean(ctx, Constants.DispatchIsDoing, true);
								MySharedPreferences.putString(ctx, Constants.DispatchCar, dispatchInfo.getCarNum());
								MySharedPreferences.putString(ctx, Constants.DispatchStarttime, dispatchInfo.getStartTime());
							}
						}catch(JSONException e){
							Log.e("TAG",e.getMessage());
							Toast.makeText(ctx, "获取配送信息失败", Toast.LENGTH_SHORT).show();
						}
					}
					else {
						try{
							String errorMesg=response.getString("errorMesg");
							if("未登陆，请先登陆".equals(errorMesg)){
								MySharedPreferences.putBoolean(ctx, Constants.UserIsLogin, false);
								Intent intent=new Intent(ctx,LoginActivity.class);
								startActivity(intent);
							}else{
								Toast.makeText(ctx, errorMesg, Toast.LENGTH_SHORT).show();
							}
						}catch(JSONException e){
							Log.e("TAG",e.getMessage());
							Toast.makeText(ctx, "获取配送信息失败", Toast.LENGTH_SHORT).show();
						}
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
//					Log.e("TAG",throwable.getMessage());
					Toast.makeText(ctx, "请检查网络连接", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	private void initDispatchFragment(DispatchInfo dispatchInfo) {
		if(dispatchInfo!=null){
			
			//layout中添加新的fragment_dispatching_info
			RelativeLayout rl1=(RelativeLayout) this.layout.findViewById(R.id.dispatching_box_1);
			RelativeLayout rl2=(RelativeLayout) this.layout.findViewById(R.id.dispatching_box_2);
			RelativeLayout rl3=(RelativeLayout) this.layout.findViewById(R.id.dispatching_box_3);
			RelativeLayout rl4=(RelativeLayout) this.layout.findViewById(R.id.dispatching_box_4);
			Button rl5=(Button)this.layout.findViewById(R.id.btn_dispatchOver);
			
			rl1.setVisibility(View.VISIBLE);
			rl2.setVisibility(View.VISIBLE);
			rl3.setVisibility(View.VISIBLE);
			rl4.setVisibility(View.VISIBLE);
			rl5.setVisibility(View.VISIBLE);
			
			TextView tv_dispatchPerson=(TextView) this.layout.findViewById(R.id.txt_dispatchPerson);
			TextView tv_dispatchCar=(TextView) this.layout.findViewById(R.id.txt_dispatchCar);
			TextView tv_dispatchStarttime=(TextView) this.layout.findViewById(R.id.txt_dispatchStarttime);
			TextView btn_dispatchOver=(Button) this.layout.findViewById(R.id.btn_dispatchOver);
			
			tv_dispatchPerson.setText(MySharedPreferences.getString(this.getActivity(), Constants.UserName, ""));
			tv_dispatchCar.setText(dispatchInfo.getCarNum());
			tv_dispatchStarttime.setText(dispatchInfo.getStartTime());
			
			btn_dispatchOver.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//添加确认对话框
					final Dialog dialog=new AlertDialog.Builder(ctx)
					.setMessage("结束配送？")
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.cancel();
						}
					})
					.setPositiveButton("结束", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							overDispatch();
						}
					})
					.create();
					dialog.show();
				}
			});
			
			
			
			
			
		}
	}

	
	private void overDispatch(){
		//查看本地是否有尚未发送成功的记录，如果有，先发送这些记录
		final ProgressDialog progressDialog=new ProgressDialog(ctx);
		progressDialog.setMessage(Constants.BeingOverDispatch);
		progressDialog.show();
		
		AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
		if(HttpClientCenter.getCookie().size()!=0){
			client.setCookieStore(HttpClientCenter.getCookieStore());
		}
		
		client.post(Constants.DispatchOverURI, new JsonHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			boolean result=false;
			try{
				result=response.getBoolean("isSuccess");
			}catch(JSONException e){
				Log.e("TAG","内部错误："+e.getMessage());
				Toast.makeText(ctx, "结束配送失败", Toast.LENGTH_SHORT).show();
			}
			if(result){
				MySharedPreferences.putBoolean(ctx, Constants.DispatchIsDoing,false);
				Intent intent=new Intent(ctx,MainActivity.class);
				startActivity(intent);
				progressDialog.dismiss();
			}
			else {
				progressDialog.dismiss();
				try{
					String errorMesg=response.getString("errorMesg");
					if("未登陆，请先登陆".equals(errorMesg)){
						MySharedPreferences.putBoolean(ctx, Constants.UserIsLogin, false);
						Intent intent=new Intent(ctx,LoginActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(ctx, errorMesg, Toast.LENGTH_SHORT).show();
					}
				}catch(JSONException e){
					Log.e("TAG","内部错误："+e.getMessage());
					Toast.makeText(ctx, "结束配送失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			progressDialog.dismiss();
//			Log.e("TAG",throwable.getMessage());
			Toast.makeText(ctx, "请检查网络连接", Toast.LENGTH_SHORT).show();
		}
	});
	}

	
	
}




	
	
	
