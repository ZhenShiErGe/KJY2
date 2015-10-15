package com.xyz.kjy.fragment;


import java.util.ArrayList;
import java.util.List;

import com.example.kjy.R;
import com.xyz.kjy.activity.CustomerInfoActivity;
import com.xyz.kjy.db.Customer;
import com.xyz.kjy.db.MyCustomerOperate;
import com.xyz.kjy.db.MyDatabaseHelper;
import com.xyz.kjy.utils.SideBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;



//通讯录

public class CustomersFragment extends Fragment implements OnItemClickListener {
	private Activity ctx;
	private View layout, layout_head;
	private ListView lvContact;
	private SideBar indexBar;
	private List<Customer> customers=null ;
	private CustomerAdapter adapter;
	
	private TextView mDialogText;
	private WindowManager mWindowManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("初始化fragment_friends一次");
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_customer,
					null);
			mWindowManager = (WindowManager) ctx
					.getSystemService(Context.WINDOW_SERVICE);
			initViews();
			if(customers==null){
				initListViewData();
			}
			lvContact.setOnItemClickListener(this);
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		
		return layout;
	}

	private void initViews() {
		lvContact = (ListView) layout.findViewById(R.id.lvContact);

		mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(
				R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		
		indexBar = (SideBar) layout.findViewById(R.id.sideBar);
		indexBar.setListView(lvContact);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		layout_head = ctx.getLayoutInflater().inflate(
				R.layout.layout_head_friend, null);
		lvContact.addHeaderView(layout_head);

	}

	@Override
	public void onDestroy() {
		mWindowManager.removeView(mDialogText);
		super.onDestroy();
	}
/**
 * 由于网络是异步加载，可能在 initListViewData执行之后，写此方法方便一部加载后调用
 */
	public void refresh(){
			initListViewData();
	}

	private void initListViewData() {
		MyDatabaseHelper helper=new MyDatabaseHelper(getActivity());
		MyCustomerOperate myCustomerOperate=new MyCustomerOperate(helper.getWritableDatabase());
		customers=new ArrayList<Customer>();
		customers.addAll(myCustomerOperate.findAll());
//		Log.i("customer=====",customers.toString());
		adapter = new CustomerAdapter(getActivity(),customers);
		lvContact.setAdapter(adapter);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Customer customer = customers.get(arg2 - 1);
		if (customer != null) {
			Intent intent = new Intent(getActivity(), CustomerInfoActivity.class);
			
			intent.putExtra(MyDatabaseHelper.ID, customer.getCustomerID());
			intent.putExtra(MyDatabaseHelper.CUSATOMERNAME, customer.getCustomerName());
			intent.putExtra(MyDatabaseHelper.CUSTOMERPERSON, customer.getCustomerPerson());
			intent.putExtra(MyDatabaseHelper.PHONENUMBER, customer.getPhoneNumber());
			intent.putExtra(MyDatabaseHelper.ADDRESS,customer.getAddress());
			
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			
		}

	}
	
	
}
