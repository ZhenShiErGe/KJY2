package com.xyz.kjy.fragment;

import com.loopj.android.http.SyncHttpClient;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.DispatchInfo;
import com.xyz.kjy.net.HttpClientCenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DispatchingsFragment extends Fragment {
	private Activity ctx;
	private View layout;
	private DispatchInfo dispatchInfo=null;
	private DispatchInfoFragment dispatchInfoFragment=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("初始化fragment_dispatch一次");
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_dispaching,
					null);
			initDispatchInfoFromNet();//获取当前配送员的配送信息
			if(dispatchInfo!=null){
				//layout中添加新的fragment_dispatching_info
				dispatchInfoFragment=new DispatchInfoFragment();
				//为该fragment关联数据
				Bundle bundle=new Bundle();
				bundle.putString("dispatchCar",dispatchInfo.getCarNum());
				bundle.putString("dispatchStarttime", dispatchInfo.getStartTime());
				dispatchInfoFragment.setArguments(bundle);
				ctx.getFragmentManager().beginTransaction().add(R.id.fragment_dispatch_container,dispatchInfoFragment).show(dispatchInfoFragment).commit();
			}
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
		SyncHttpClient client=HttpClientCenter.getSyncHttpClient();
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
							dispatchInfo=com.alibaba.fastjson.JSONObject.parseObject(jsonString1, DispatchInfo.class);
						}
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(ctx, "获取配送信息失败", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					try{
						String errorMesg=response.getString("errorMesg");
						Toast.makeText(ctx, "获取配送信息失败:"+errorMesg, Toast.LENGTH_SHORT).show();
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(ctx, "获取配送信息失败", Toast.LENGTH_SHORT).show();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Log.e("TAG",throwable.getMessage());
				Toast.makeText(ctx, "网络连接失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	public DispatchInfo getDispatchInfo() {
		return dispatchInfo;
	}
	
	
}




	
	
	
