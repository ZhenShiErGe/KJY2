package com.xyz.kjy.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.net.HttpClientCenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangePwdActivity extends Activity {
//	private ImageView btnback;
	private LinearLayout btnback;
	private EditText et_oldpwd,et_newpwd,et_newpwd1;
	private Button btn_changepwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_changepwd);
		super.onCreate(savedInstanceState);
		//在所有activity中添加该行代码，用于需要时退出应用程序
		SystemApplication.getInstance().addActivity(this);
		
		btnback= (LinearLayout) findViewById(R.id.changepwd_back_main);
		
		btnback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				Intent intent =new Intent(AboutusActivity.this,MainActivity.class);
//				startActivity(intent);
				ChangePwdActivity.this.finish();
				ChangePwdActivity.this.overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		
		
		et_oldpwd=(EditText) findViewById(R.id.et_oldpwd);
		et_newpwd=(EditText) findViewById(R.id.et_newpwd);
		et_newpwd1=(EditText) findViewById(R.id.et_newpwd1);
		
		et_oldpwd.addTextChangedListener(new TextChange());
		et_newpwd.addTextChangedListener(new TextChange());
		et_newpwd1.addTextChangedListener(new TextChange());
		
		btn_changepwd=(Button) findViewById(R.id.btn_changepwd);
		btn_changepwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changePwd();
			}
		});
	}
	
	protected void changePwd() {
		final String oldpwd = et_oldpwd.getText().toString().trim();
		final String newpwd = et_newpwd.getText().toString().trim();
		final String newpwd1 = et_newpwd1.getText().toString().trim();
		if(!newpwd.equals(newpwd1))
			Toast.makeText(ChangePwdActivity.this, "两次新密码不一致", Toast.LENGTH_SHORT).show();
		else{
			//需要发送的参数
			RequestParams params=new RequestParams();
			params.put("oldPassword", oldpwd);
			params.put("newPassword", newpwd);
			//显示正在登录处理对话窗
			final ProgressDialog progressDialog=new ProgressDialog(ChangePwdActivity.this);
			progressDialog.setMessage(Constants.BeingChangePwd);
			progressDialog.show();
			//开始发送请求
			AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
			if(HttpClientCenter.getCookie().size()!=0)
				client.setCookieStore(HttpClientCenter.getCookieStore());
			client.post(Constants.ChangePwdURI, params, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
					boolean result=false;
					try{
						result=response.getBoolean("isSuccess");
					}catch(JSONException e){
						Log.e("TAG","内部错误："+e.getMessage());
						Toast.makeText(ChangePwdActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
					}
					if(result){
//		     			Intent intent =new Intent(ChangePwdActivity.this,MainActivity.class);
//		     			startActivity(intent);
						ChangePwdActivity.this.finish();
						ChangePwdActivity.this.overridePendingTransition(R.anim.push_right_in,
								R.anim.push_right_out);
		     			progressDialog.dismiss();
		     			Toast.makeText(ChangePwdActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
					}
					else {
						progressDialog.dismiss();
						try{
							String errorMesg=response.getString("errorMesg");
							Toast.makeText(ChangePwdActivity.this, "修改密码失败:"+errorMesg, Toast.LENGTH_SHORT).show();
						}catch(JSONException e){
							Log.e("TAG","内部错误："+e.getMessage());
							Toast.makeText(ChangePwdActivity.this, "修改密码失败:", Toast.LENGTH_SHORT).show();
						}
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					progressDialog.dismiss();
//					Log.e("TAG",throwable.getMessage());
					Toast.makeText(ChangePwdActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
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
			boolean Sign2 = et_oldpwd.getText().length() >= 6;
			boolean Sign3 = et_newpwd.getText().length() >= 6;
			boolean Sign4 = et_newpwd1.getText().length() >= 6;
			if (Sign2 & Sign3 & Sign4) {
				btn_changepwd.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_changepwd.setEnabled(true);
			} else {
				btn_changepwd.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_changepwd.setTextColor(0xFFD0EFC6);
				btn_changepwd.setEnabled(false);
			}
		}
	}
}
