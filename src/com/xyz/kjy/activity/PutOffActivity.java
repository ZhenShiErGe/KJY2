package com.xyz.kjy.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.MyDatabaseHelper;
import com.xyz.kjy.net.HttpClientCenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PutOffActivity extends Activity {
	private String storeName;
	private TextView tv_storeName;
	private EditText et_putoffNum;
	private EditText et_putonNum;
	private RadioGroup rg_settletype;
	private Button btn_sendMess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_putoff);
		super.onCreate(savedInstanceState);
		//在所有activity中添加该行代码，用于需要时退出应用程序
		SystemApplication.getInstance().addActivity(this);
		
		Intent intent=this.getIntent();
		storeName=intent.getStringExtra(MyDatabaseHelper.STORENAME);
		tv_storeName=(TextView) findViewById(R.id.txt_putoffcustomer);
		et_putoffNum=(EditText) findViewById(R.id.et_putoffnum);
		et_putonNum=(EditText) findViewById(R.id.et_putonnum);
		rg_settletype=(RadioGroup) findViewById(R.id.rg_settletype);
		btn_sendMess=(Button) findViewById(R.id.btnsendmess);
		
		
		tv_storeName.setText(storeName);
		
		btn_sendMess.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//添加确认对话框
				final Dialog dialog=new AlertDialog.Builder(PutOffActivity.this)
				.setMessage("确定结算？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.cancel();
					}
				})
				.setPositiveButton("结算", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if(rg_settletype.getCheckedRadioButtonId()==R.id.rb_money){
						sendMessage(storeName,et_putoffNum.getText().toString().trim(),
								et_putonNum.getText().toString().trim(),1+"");
						}else if(rg_settletype.getCheckedRadioButtonId()==R.id.rb_debt){
							sendMessage(storeName,et_putoffNum.getText().toString().trim(),
									et_putonNum.getText().toString().trim(),2+"");
						}
					}
				})
				.create();
				dialog.show();
			}
		});
	}

	private void sendMessage(String storeName,String outNum,String returnNum,String settleType) {
		RequestParams params=new RequestParams();
		params.put("storeName", storeName);
		params.put("productionOutNo",outNum);
		params.put("productionReturnNo", returnNum);
		params.put("settleType",settleType);
		AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
		if(HttpClientCenter.getCookie().size()!=0)
			client.setCookieStore(HttpClientCenter.getCookieStore());
		client.post(Constants.SendMessURI,params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				boolean result=false;
				try{
					result=response.getBoolean("isSuccess");
				}catch(JSONException e){
					Log.e("TAG",e.getMessage());
					Toast.makeText(PutOffActivity.this, "发送信息失败", Toast.LENGTH_SHORT).show();
				}
				if(result){
					Intent intent=new Intent(PutOffActivity.this,MainActivity.class);
					startActivity(intent);
				}
				else {
					try{
						String errorMesg=response.getString("errorMesg");
						Toast.makeText(PutOffActivity.this, "发送信息失败", Toast.LENGTH_SHORT).show();
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(PutOffActivity.this, "发送信息失败", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
//				Log.e("TAG",throwable.getMessage());
				Toast.makeText(PutOffActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
