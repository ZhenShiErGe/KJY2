package com.xyz.kjy.fragment;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.kjy.R;
import com.xyz.kjy.db.Customer;
import com.xyz.kjy.utils.CustomerComparator;
import com.xyz.kjy.utils.PingYinUtil;



public class CustomerAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private List<Customer> customers;

	public CustomerAdapter(Context mContext, List<Customer> customers) {
		this.mContext = mContext;
		this.customers=customers;
		// 排序(实现了中英文混排)
	
		if(customers!=null){
			Collections.sort(customers, new CustomerComparator());
		}
	}

	@Override
	public int getCount() {
		return customers==null?0:customers.size();
	}

	@Override
	public Object getItem(int position) {
		return customers==null?null:customers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return customers==null?0:position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		Customer customer=customers.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.contact_item, null);

		}
		ImageView ivAvatar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		
		TextView tvCatalog = ViewHolder.get(convertView,
				R.id.contactitem_catalog);//不同字母之间的隔栏
		
		TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
		
		String catalog = PingYinUtil.converterToFirstSpell(customer.getCustomerName())
				.substring(0, 1);
		
		if (position == 0) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(catalog);
		} else {
			Customer nextCustomer = customers.get(position - 1);
			String lastCatalog = PingYinUtil.converterToFirstSpell(
					nextCustomer.getCustomerName()).substring(0, 1);
			if (catalog.equals(lastCatalog)) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			}
		}

		ivAvatar.setImageResource(R.drawable.head);
		tvNick.setText(customer.getCustomerName());
		return convertView;
	}

	//返回某个部分的头一个元素所在位置
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < customers.size(); i++) {
			Customer customer =customers.get(i);
			String l = PingYinUtil.converterToFirstSpell(customer.getCustomerName())
					.substring(0, 1);
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
