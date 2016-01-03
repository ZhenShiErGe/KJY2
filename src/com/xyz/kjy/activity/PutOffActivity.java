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
import com.xyz.kjy.utils.MySharedPreferences;

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
import android.widget.LinearLayout;
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
	private LinearLayout backToMain;
	
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
		backToMain= (LinearLayout) findViewById(R.id.putoff_back_main);
		
		tv_storeName.setText(storeName);
		
        backToMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PutOffActivity.this.finish();
				PutOffActivity.this.overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
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

	private void sendMessage(final String storeName,final String outNum,String returnNum,final String settleType) {
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
					try{
						String jsonString=response.getString("content");
						JSONObject json=new JSONObject(jsonString);
						String payNum=json.getString("totalMoney");
						Log.i("TAG",payNum);
						Intent intent=new Intent(PutOffActivity.this,PayActivity.class);
						intent.putExtra("payType", Integer.parseInt(settleType));
						intent.putExtra("payNum", Integer.parseInt(payNum));
						intent.putExtra("storeName", storeName);
						intent.putExtra("putoffNum", outNum);//卸货数量
						intent.putExtra("price",Integer.parseInt(payNum)/Integer.parseInt(outNum));//卸货单价
						startActivity(intent);
						PutOffActivity.this.overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
					}
				}
				else {
					try{
						String errorMesg=response.getString("errorMesg");
						if("未登陆，请先登陆".equals(errorMesg)){
							MySharedPreferences.putBoolean(PutOffActivity.this, Constants.UserIsLogin, false);
							Intent intent=new Intent(PutOffActivity.this,LoginActivity.class);
							startActivity(intent);
							PutOffActivity.this.overridePendingTransition(R.anim.push_left_in,
									R.anim.push_left_out);
						}else{
							Toast.makeText(PutOffActivity.this, errorMesg, Toast.LENGTH_SHORT).show();
						}
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(PutOffActivity.this, "发送信息失败", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(PutOffActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
