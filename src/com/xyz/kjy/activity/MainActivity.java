package com.xyz.kjy.activity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kjy.R;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.Customer;
import com.xyz.kjy.db.MyCustomerOperate;
import com.xyz.kjy.db.MyDatabaseHelper;
import com.xyz.kjy.fragment.CustomersFragment;
import com.xyz.kjy.fragment.DispatchingsFragment;
import com.xyz.kjy.fragment.MeFragment;
import com.xyz.kjy.utils.MySharedPreferences;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	/**
	 * 主界面的tab相关
	 */
	private DispatchingsFragment dispatchingsFragment=new DispatchingsFragment();;
	private CustomersFragment customersFragment=new CustomersFragment();
	private MeFragment meFragment=new MeFragment();;
	private Fragment [] fragments;
	
	private ImageView btnDispatching;
	private ImageView btnCustomer;
	private ImageView btnMe;
	private ImageView btns[];
	
	int currentTabIndex;
	int destTabIndex;
	/**
	 * 扫描二维码
	 */
	private ImageView btnScan;
	/**
	 * 商家数据更新试用的数据库 
	 */
	private SQLiteOpenHelper helper=null;
	private MyCustomerOperate myCustomerOperate=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		btns[0].setSelected(true);
		
		findViewById(R.id.re_weixin).setOnClickListener(this);
		findViewById(R.id.re_contact_list).setOnClickListener(this);
		findViewById(R.id.re_profile).setOnClickListener(this);
		
		currentTabIndex=0;
		
		getFragmentManager().beginTransaction().add(R.id.fragment_container, fragments[0])
		.add(R.id.fragment_container, fragments[1]).add(R.id.fragment_container,fragments[2])
		.hide(fragments[1]).hide(fragments[2]).show(fragments[0]).commit();
		
	}
	/**
	 * 定义左上部添加配送记录
	 */
	private void initAddDispitchBtn(){
		ImageView btn_add_dispitch=(ImageView) findViewById(R.id.add_dispatching);
		btn_add_dispitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//添加配送记录的操作代码
				//跳转到新的activity，在这里选择盖茨配送的具体信息
				//点击确认即可添加新的配送信息（注意同一个时间一个用户只能有一个正在配送的记录）
				//添加成功后返回到主页面
			}
		});
	}
	/**
	 * 定义二维码扫描项
	 */
	private void initScan(){
		btnScan=(ImageView) findViewById(R.id.scan_customer);
		btnScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast toast=Toast.makeText(MainActivity.this, "正在加载", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Intent scanIntent=new Intent(MainActivity.this,CaptureActivity.class);
				startActivity(scanIntent);
			}
		});
	}
	/**
	 * 二维码扫描成功结果处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			String result=data.getExtras().getString("result");
			System.out.println("result="+result);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	
		
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
		MySharedPreferences mySharedPreferences=new MySharedPreferences(this);
		if(!mySharedPreferences.contains("lastUpdateTime")){
			mySharedPreferences.putString("lastUpdateTime", sdf.format(new Date()));
			updateCustomerData();
		}
		else{
			try {
				String str_lastUpdateDate=mySharedPreferences.getString("lastUpdateTime", "2015-09-19 11:45:16");//后面为默认值
				Date lastUpDate;
				lastUpDate = sdf.parse(str_lastUpdateDate);
				Date currentDate=new Date();
				if(currentDate.getTime()-lastUpDate.getTime()>=2*24*60*60*1000){//每两天更新数据
					mySharedPreferences.putString("lastUpdateTime", sdf.format(currentDate));
					updateCustomerData();
				}
			} 
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * 更新customer数据到数据库
	 */
	private void updateCustomerData(){
		RequestQueue mQueue = Volley.newRequestQueue(this);
//		StringRequest stringRequest = new StringRequest("http://192.168.0.89/NewDemo/getNewsJSON.php",
		StringRequest stringRequest = new StringRequest(Constants.GetCustomersURI,		
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						List<Customer> result=JSONArray.parseArray(response,Customer.class);
						MainActivity.this.myCustomerOperate=new MyCustomerOperate(
								MainActivity.this.helper.getWritableDatabase());
						MainActivity.this.myCustomerOperate.updateAll(result);
						customersFragment.refresh();//跟新数据后刷新数据列表
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				});
		mQueue.add(stringRequest);  
	}

}
