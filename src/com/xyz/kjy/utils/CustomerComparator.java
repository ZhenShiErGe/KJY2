package com.xyz.kjy.utils;



import java.util.Comparator;

import com.xyz.kjy.db.Customer;



public class CustomerComparator implements Comparator<Customer> {

	@Override
	public int compare(Customer customer0, Customer customer1) {
		// 按照商家名称排序
	
		String catalog0 = "";
		String catalog1 = "";

		if (customer0 != null && customer0.getCustomerName() != null
				&& customer0.getCustomerName().length() > 1)
			catalog0 = PingYinUtil.converterToFirstSpell(customer0.getCustomerName())
					.substring(0, 1);

		if (customer1 != null && customer1.getCustomerName() != null
				&& customer1.getCustomerName().length() > 1)
			catalog1 = PingYinUtil.converterToFirstSpell(customer1.getCustomerName())
					.substring(0, 1);
		int flag = catalog0.compareTo(catalog1);
		return flag;
	}

}
