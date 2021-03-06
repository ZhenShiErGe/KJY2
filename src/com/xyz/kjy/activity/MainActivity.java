package com.xyz.kjy.activity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.example.kjy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.Customer;
import com.xyz.kjy.db.MyCustomerOperate;
import com.xyz.kjy.db.MyDatabaseHelper;
import com.xyz.kjy.fragment.CustomersFragment;
import com.xyz.kjy.fragment.DispatchingsFragment;
import com.xyz.kjy.fragment.MeFragment;
import com.xyz.kjy.net.HttpClientCenter;
import com.xyz.kjy.utils.MySharedPreferences;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private final static int SCAN_CODE = 1;
	private static final int APP_EXIT = 0;
	/**
	 * 主界面的tab相关
	 */
	private DispatchingsFragment dispatchingsFragment=new DispatchingsFragment();;
	private CustomersFragment customersFragment=new CustomersFragment();
	private MeFragment meFragment=new MeFragment();;
	private Fragment [] fragments;
	private ImageView btns[];
	
	int currentTabIndex;
	int destTabIndex;
	/**
	 * 扫描二维码
	 */
	private LinearLayout btnScan;
	/**
	 * 商家数据更新试用的数据库 
	 */
	private SQLiteOpenHelper helper=null;
	private MyCustomerOperate myCustomerOperate=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//在所有activity中添加该行代码，用于需要时退出应用程序
		SystemApplication.getInstance().addActivity(this);
		initTabs();
		initScan();
		initAddDispitchBtn();
		this.helper=new MyDatabaseHelper(this);
		updateCustomerDataRegularly();
	}
	
	
	private void initTabs(){
		fragments=new Fragment[]{dispatchingsFragment,customersFragment,meFragment};
		
		btns=new ImageView[3];
		btns[0]=(ImageView) findViewById(R.id.ib_weixin);
		btns[1]= (ImageView) findViewById(R.id.ib_contact_list);
		btns[2]=(ImageView) findViewById(R.id.ib_profile);
//		btns[0].setSelected(true);
		btns[1].setSelected(true);
		
		findViewById(R.id.re_weixin).setOnClickListener(this);
		findViewById(R.id.re_contact_list).setOnClickListener(this);
		findViewById(R.id.re_profile).setOnClickListener(this);
		
//		currentTabIndex=0;
		currentTabIndex=1;
		
		getFragmentManager().beginTransaction().add(R.id.fragment_container, fragments[0])
		.add(R.id.fragment_container, fragments[1]).add(R.id.fragment_container,fragments[2])
		.hide(fragments[0]).hide(fragments[2]).show(fragments[1]).commit();
		
	}
	/**
	 * 定义左上部添加配送记录
	 */
	private void initAddDispitchBtn(){
		LinearLayout btn_add_dispitch= (LinearLayout) findViewById(R.id.add_dispatching);
		btn_add_dispitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(MySharedPreferences.getBoolean(MainActivity.this, Constants.DispatchIsDoing, false)){
					Toast.makeText(MainActivity.this, "已经在配送", Toast.LENGTH_SHORT).show();
				}
				else{
					startDispatchActivity();
				}
			}
		});
	}


	/**
	 * 定义二维码扫描项
	 */
	private void initScan(){
		btnScan=(LinearLayout) findViewById(R.id.scan_customer);
		btnScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "正在加载", Toast.LENGTH_SHORT).show();
				Intent scanIntent=new Intent(MainActivity.this,CaptureActivity.class);
				scanIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(scanIntent,SCAN_CODE);
				MainActivity.this.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});
	}
	/**
	 * 二维码扫描成功结果处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case SCAN_CODE:
				if(resultCode==RESULT_OK){
					String storeName=data.getExtras().getString("result").trim();
					this.myCustomerOperate=new MyCustomerOperate(this.helper.getWritableDatabase());
					Customer customer=this.myCustomerOperate.findByStoreName(storeName);
					if(customer!=null){
						Intent intent = new Intent(MainActivity.this, CustomerInfoActivity.class);
						
						intent.putExtra(MyDatabaseHelper.STORENAME, customer.getStoreName());
						intent.putExtra(MyDatabaseHelper.STOREOWNERNAME,customer.getStoreOwnerName());
						intent.putExtra(MyDatabaseHelper.PHONE, customer.getPhone());			
						intent.putExtra(MyDatabaseHelper.PRODUCTIONNAME, customer.getProductionName());
						intent.putExtra(MyDatabaseHelper.ADDRESS,customer.getAddress());
						intent.putExtra(MyDatabaseHelper.UNITPRICE, Integer.parseInt(customer.getUnitPrice().trim())/100+"");
						
						MainActivity.this.startActivity(intent);
						MainActivity.this.overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
					}else{
						Intent intent = new Intent(MainActivity.this, CustomerInfoActivity.class);
						intent.putExtra(MyDatabaseHelper.STORENAME, storeName);
						intent.putExtra(MyDatabaseHelper.STOREOWNERNAME,"暂无信息");
						intent.putExtra(MyDatabaseHelper.PHONE, "暂无信息");			
						intent.putExtra(MyDatabaseHelper.PRODUCTIONNAME, "暂无信息");
						intent.putExtra(MyDatabaseHelper.ADDRESS,"暂无信息");
						intent.putExtra(MyDatabaseHelper.UNITPRICE, "暂无信息");
						
						MainActivity.this.startActivity(intent);
						MainActivity.this.overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
					}
				}else if(resultCode==RESULT_CANCELED){
					Log.i("TAG","扫描二维码取消");
					MainActivity.this.overridePendingTransition(R.anim.push_right_in,
							R.anim.push_right_out);
				}else{
					Log.i("TAG","扫描二维码失败");
					Toast.makeText(MainActivity.this, "扫描商家信息失败", Toast.LENGTH_SHORT).show();
					MainActivity.this.overridePendingTransition(R.anim.push_right_in,
							R.anim.push_right_out);
				}
				break;
			default:
				break;
		}
	}


	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.re_weixin)
		{
			destTabIndex=0;
			if(destTabIndex!=currentTabIndex){
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.hide(fragments[currentTabIndex]);
				if(!fragments[destTabIndex].isAdded())
					ft.add(R.id.fragment_container,fragments[destTabIndex]);
				ft.show(fragments[destTabIndex]).commit();
			}
			btns[currentTabIndex].setSelected(false);
			btns[destTabIndex].setSelected(true);
			currentTabIndex=destTabIndex;
		}
		else if(v.getId()==R.id.re_contact_list)
		{
		    destTabIndex=1;
		    if(destTabIndex!=currentTabIndex){
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.hide(fragments[currentTabIndex]);
				if(!fragments[destTabIndex].isAdded())
					ft.add(R.id.fragment_container,fragments[destTabIndex]);
				ft.show(fragments[destTabIndex]).commit();
			}
			btns[currentTabIndex].setSelected(false);
			btns[destTabIndex].setSelected(true);
			currentTabIndex=destTabIndex;
		}
		else if(v.getId()==R.id.re_profile)
		{
			destTabIndex=2;
			if(destTabIndex!=currentTabIndex){
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.hide(fragments[currentTabIndex]);
				if(!fragments[destTabIndex].isAdded())
					ft.add(R.id.fragment_container,fragments[destTabIndex]);
				ft.show(fragments[destTabIndex]).commit();
			}
			btns[currentTabIndex].setSelected(false);
			btns[destTabIndex].setSelected(true);
			currentTabIndex=destTabIndex;
		}	
		
	}	
	/**
	 * 定时的更新商家的数据
	 */
	private void updateCustomerDataRegularly(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!MySharedPreferences.contains(this,"lastUpdateTime")){
			MySharedPreferences.putString(this,"lastUpdateTime", sdf.format(new Date()));
			updateCustomerData();
		}
		else{
			try {
				String str_lastUpdateDate=MySharedPreferences.getString(this,"lastUpdateTime", "2015-09-19 11:45:16");//后面为默认值
				Date lastUpDate = sdf.parse(str_lastUpdateDate);
				Date currentDate=new Date();
				if(currentDate.getTime()-lastUpDate.getTime()>=60*1000){//每小时更新数据
//				if(true){//每次刷新都要更新数据
					MySharedPreferences.putString(this,"lastUpdateTime", sdf.format(currentDate));
					updateCustomerData();
				}
			} 
			catch (ParseException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * 更新customer数据到数据库
	 */
	private void updateCustomerData(){
		
		//显示正在登录处理对话窗
//		final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
//		progressDialog.setMessage(Constants.BeingUpdateCustomer);
//		progressDialog.show();
		//开始发送请求
		AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
		if(HttpClientCenter.getCookie().size()!=0)
			client.setCookieStore(HttpClientCenter.getCookieStore());
		client.get(Constants.CustomersURI, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				boolean result=false;
				try{
					result=response.getBoolean("isSuccess");
				}catch(JSONException e){
					Log.e("TAG",e.getMessage());
					Toast.makeText(MainActivity.this, "商家信息更新失败", Toast.LENGTH_SHORT).show();
				}
				if(result){
					try{
						String jsonString=response.getString("content");
						List<Customer> customers=JSONArray.parseArray(jsonString,Customer.class);
						MainActivity.this.myCustomerOperate=new MyCustomerOperate(
								MainActivity.this.helper.getWritableDatabase());
						MainActivity.this.myCustomerOperate.updateAll(customers);
						customersFragment.refresh();//更新数据后刷新数据列表
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(MainActivity.this, "商家信息更新失败", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					try{
						String errorMesg=response.getString("errorMesg");
						if("未登陆，请先登陆".equals(errorMesg)){
							MySharedPreferences.putBoolean(MainActivity.this,Constants.UserIsLogin, false);
							Intent intent=new Intent(MainActivity.this,LoginActivity.class);
							startActivity(intent);
						}else{
							Toast.makeText(MainActivity.this, errorMesg, Toast.LENGTH_SHORT).show();
						}
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(MainActivity.this, "商家信息更新失败", Toast.LENGTH_SHORT).show();
					}
					
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
//				progressDialog.dismiss();
//				Log.e("TAG",throwable.getMessage());
				Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			}
		});
	}
	/*************重写返回键----start**********/
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
            showDialog(APP_EXIT);  
            return true;  
        } else  
            return super.onKeyDown(keyCode, event);  
    }  
	 @Override  
	    protected Dialog onCreateDialog(int id) {  
	        if (id == APP_EXIT) {  
	            return new AlertDialog.Builder(MainActivity.this)  
	                    .setMessage("是否退出程序?")  
	                    .setPositiveButton("确定",  
	                            new DialogInterface.OnClickListener() {  
	                                public void onClick(DialogInterface dialog,  
	                                        int which) {  
	        			     			SystemApplication.getInstance().exit();
	        			     			dialog.dismiss();  
	                                }  
	                            })  
	                    .setNegativeButton("取消",  
	                            new DialogInterface.OnClickListener() {  
	                                public void onClick(DialogInterface dialog,  
	                                        int which) {  
	                                    dialog.dismiss();  
	                                }  
	                            }).create();  
	  
	        }  
	        return null;  
	  
	    }  
	 /*************重写返回键---end**********/
	private void startDispatchActivity() {
		AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
		if(HttpClientCenter.getCookie().size()!=0)
			client.setCookieStore(HttpClientCenter.getCookieStore());
		client.get(Constants.AllCarURI, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				boolean result=false;
				try{
					result=response.getBoolean("isSuccess");
				}catch(JSONException e){
					Log.e("TAG",e.getMessage());
					Toast.makeText(MainActivity.this, "获取车辆信息失败", Toast.LENGTH_SHORT).show();
				}
				if(result){
					try{
						String jsonString=response.getString("content");
						Intent intent=new Intent(MainActivity.this,StartDispatchActivity.class);
						intent.putExtra("carNums", jsonString);
						startActivity(intent);
						MainActivity.this.overridePendingTransition(R.anim.push_right_in,
								R.anim.push_right_out);
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(MainActivity.this, "获取车辆信息失败", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					try{
						String errorMesg=response.getString("errorMesg");
						if("未登陆，请先登陆".equals(errorMesg)){
							MySharedPreferences.putBoolean(MainActivity.this,Constants.UserIsLogin, false);
							Intent intent=new Intent(MainActivity.this,LoginActivity.class);
							startActivity(intent);
						}else{
							Toast.makeText(MainActivity.this, errorMesg, Toast.LENGTH_SHORT).show();
						}
					}catch(JSONException e){
						Log.e("TAG",e.getMessage());
						Toast.makeText(MainActivity.this, "获取车辆信息失败", Toast.LENGTH_SHORT).show();
					}
					
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
//				progressDialog.dismiss();
//				Log.e("TAG",throwable.getMessage());
				Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			}
		});
	}
//	private void updateCustomerDataAndGoto(final String storeName) {
//		// TODO Auto-generated method stub
//		AsyncHttpClient client=HttpClientCenter.getAsyncHttpClient();
//		if(HttpClientCenter.getCookie().size()!=0)
//			client.setCookieStore(HttpClientCenter.getCookieStore());
//		client.get(Constants.CustomersURI, new JsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONObject response) {
//				boolean result=false;
//				try{
//					result=response.getBoolean("isSuccess");
//				}catch(JSONException e){
//					Log.e("TAG",e.getMessage());
//					Toast.makeText(MainActivity.this, "商家信息更新失败", Toast.LENGTH_SHORT).show();
//				}
//				if(result){
//					try{
//						String jsonString=response.getString("content");
//						List<Customer> customers=JSONArray.parseArray(jsonString,Customer.class);
//						MainActivity.this.myCustomerOperate=new MyCustomerOperate(
//								MainActivity.this.helper.getWritableDatabase());
//						MainActivity.this.myCustomerOperate.updateAll(customers);
//						customersFragment.refresh();//更新数据后刷新数据列表
//						
//						MainActivity.this.myCustomerOperate=new MyCustomerOperate(
//								MainActivity.this.helper.getWritableDatabase());
//						Customer customer=MainActivity.this.myCustomerOperate.findByStoreName(storeName);
//						Intent intent = new Intent(MainActivity.this, CustomerInfoActivity.class);
//						intent.putExtra(MyDatabaseHelper.STORENAME, customer.getStoreName());
//						intent.putExtra(MyDatabaseHelper.STOREOWNERNAME,customer.getStoreOwnerName());
//						intent.putExtra(MyDatabaseHelper.PHONE, customer.getPhone());			
//						intent.putExtra(MyDatabaseHelper.PRODUCTIONNAME, customer.getProductionName());
//						intent.putExtra(MyDatabaseHelper.ADDRESS,customer.getAddress());
//						intent.putExtra(MyDatabaseHelper.UNITPRICE, customer.getUnitPrice());
//
//						MainActivity.this.startActivity(intent);
//						MainActivity.this.overridePendingTransition(R.anim.push_left_in,
//								R.anim.push_left_out);
//					}catch(JSONException e){
//						Log.e("TAG",e.getMessage());
//						Toast.makeText(MainActivity.this, "商家信息更新失败", Toast.LENGTH_SHORT).show();
//					}
//				}
//				else {
//					try{
//						String errorMesg=response.getString("errorMesg");
////						Toast.makeText(MainActivity.this, "商家信息更新失败:"+errorMesg, Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(MainActivity.this, CustomerInfoActivity.class);
//						intent.putExtra(MyDatabaseHelper.STORENAME, storeName);
//						intent.putExtra(MyDatabaseHelper.STOREOWNERNAME,"暂无信息");
//						intent.putExtra(MyDatabaseHelper.PHONE, "暂无信息");			
//						intent.putExtra(MyDatabaseHelper.PRODUCTIONNAME, "暂无信息");
//						intent.putExtra(MyDatabaseHelper.ADDRESS,"暂无信息");
//						intent.putExtra(MyDatabaseHelper.UNITPRICE, "暂无信息");
//					}catch(JSONException e){
//						Log.e("TAG",e.getMessage());
//						Toast.makeText(MainActivity.this, "商家信息更新失败", Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					Throwable throwable, JSONObject errorResponse) {
//				Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
	
}





//发起请求
//JsonObjectGetRequest jsonObjectGetRequest =
//		new JsonObjectGetRequest(Constants.CustomersURI, new Response.Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				boolean result=false;
//				try{
//					result=response.getBoolean("isSuccess");
//				}catch(JSONException e){
//					Log.e("TAG",e.getMessage());
//					Log.e("TAG", "更新商家信息失败,内部错误");
//					progressDialog.dismiss();
//				}
//				if(result){
//					try{
//						String jsonString=response.getString("content");
//						List<Customer> customers=JSONArray.parseArray(jsonString,Customer.class);
//						MainActivity.this.myCustomerOperate=new MyCustomerOperate(
//								MainActivity.this.helper.getWritableDatabase());
//						MainActivity.this.myCustomerOperate.updateAll(customers);
//						progressDialog.dismiss();
//						customersFragment.refresh();//跟新数据后刷新数据列表
//					}catch(JSONException e){
//						Log.e("TAG",e.getMessage());
//						Log.e("TAG", "更新商家信息失败,内部错误");
//						progressDialog.dismiss();
//					}
//				}
//				else {
//					progressDialog.dismiss();
//					try{
//						String errorMesg=response.getString("errorMesg");
//						Toast.makeText(MainActivity.this, "商家信息更新失败:"+errorMesg, Toast.LENGTH_SHORT).show();
//					}catch(JSONException e){
//						Log.e("TAG",e.getMessage());
//						Log.e("TAG", "更新商家信息失败,内部错误");
//					}
//					
//				}
//			}
//		},new Response.ErrorListener() {
//	         @Override
//	         public void onErrorResponse(VolleyError volleyError) {                   
//	        	 progressDialog.dismiss();
//	             Toast.makeText(MainActivity.this, "网络不可用，登录失败！", Toast.LENGTH_SHORT).show();
//	         }
//	     });
//     //对于每次请求，加上本地存储的cookie用于验证身份
//     String localCookie = MySharedPreferences.getString(MainActivity.this,Constants.Cookie, "");
//     if(!localCookie.equals("")){
//         jsonObjectGetRequest.setSendCookie(localCookie);//向服务器发起post请求时加上cookie字段
//     }
//     RequestCenter.getRequestQueue().add(jsonObjectGetRequest);
