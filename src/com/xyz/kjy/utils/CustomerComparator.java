package com.xyz.kjy.utils;

import java.util.Comparator;
import com.xyz.kjy.db.Customer;

public class CustomerComparator implements Comparator<Customer> {

	@Override
	public int compare(Customer customer0, Customer customer1) {
		// 按照商家名称排序
		String catalog0 = "";
		String catalog1 = "";
//
//		if (customer0 != null && customer0.getStoreName() != null)
//			catalog0 = PingYinUtil.converterToFirstSpell(customer0.getStoreName())
//					.substring(0, 1);
//
//		if (customer1 != null && customer1.getStoreName() != null
//				&& customer1.getStoreName().length() > 1)
//			catalog1 = PingYinUtil.converterToFirstSpell(customer1.getStoreName())
//					.substring(0, 1);
		if(customer0!=null&&customer0.getStoreName()!=null)
			catalog0=HanYuUtil.getStringPinYin(customer0.getStoreName());
		if(customer1!=null&&customer1.getStoreName()!=null)
			catalog1=HanYuUtil.getStringPinYin(customer1.getStoreName());
		return catalog0.compareTo(catalog1);
	}

}
