package com.xyz.kjy.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xyz.kjy.activity.LoginActivity;
import com.xyz.kjy.activity.MainActivity;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.net.HttpClientCenter;
import com.xyz.kjy.utils.MySharedPreferences;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DispatchInfoFragment extends Fragment {
	    private Activity ctx;
		private View currentView;
	    private TextView tv_dispatchPerson;
	    private TextView tv_dispatchCar;
	    private TextView tv_dispatchStarttime;
	    private Button btn_dispatchOver;
	    
		public void setCurrentViewPararms(FrameLayout.LayoutParams layoutParams) {
			currentView.setLayoutParams(layoutParams);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ctx=this.getActivity();
			currentView = inflater.inflate(R.layout.fragment_dispachinfo,container,false);
			
			tv_dispatchPerson=(TextView) currentView.findViewById(R.id.txt_dispatchPerson);
			tv_dispatchCar=(TextView) currentView.findViewById(R.id.txt_dispatchCar);
			tv_dispatchStarttime=(TextView) currentView.findViewById(R.id.txt_dispatchStarttime);
			btn_dispatchOver=(Button) currentView.findViewById(R.id.btn_dispatchOver);
			
			
			tv_dispatchPerson.setText(MySharedPreferences.getString(this.getActivity(), Constants.UserName, ""));
			tv_dispatchCar.setText(getArguments().getString("dispatchCar"));
			tv_dispatchStarttime.setText(getArguments().getString("dispatchStarttime"));
			
			btn_dispatchOver.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//查看本地是否有尚未发送成功的记录，如果有，先发送这些记录
					final ProgressDialog progressDialog=new ProgressDialog(ctx);
					progressDialog.setMessage(Constants.BeingOverDispatch);
					progressDialog.show();
					
					AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
					if(HttpClientCenter.getCookie().size()!=0)
						client.setCookieStore(HttpClientCenter.getCookieStore());	
					
					client.post(Constants.LoginURI, new JsonHttpResponseHandler(){
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
							Intent intent=new Intent(ctx,MainActivity.class);
							startActivity(intent);
							progressDialog.dismiss();
						}
						else {
							progressDialog.dismiss();
							try{
								String errorMesg=response.getString("errorMesg");
								Toast.makeText(ctx, "结束配送失败："+errorMesg, Toast.LENGTH_SHORT).show();
							}catch(JSONException e){
								Log.e("TAG","内部错误："+e.getMessage());
								Toast.makeText(ctx, "结束配送失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						Log.e("TAG",throwable.getMessage());
						Toast.makeText(ctx, "请检查网络连接", Toast.LENGTH_SHORT).show();
					}
				});
				}
			});
			
			return currentView;
		}
}

