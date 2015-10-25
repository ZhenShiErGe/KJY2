package com.xyz.kjy.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyCustomerOperate {
	private SQLiteDatabase db=null;
	public MyCustomerOperate(SQLiteDatabase db) {
		this.db = db;
	}
	private void insertNoClose(Customer customer){
		String sql = "insert into "+MyDatabaseHelper.TABLENAME_CUSTOMER 
				+"("+MyDatabaseHelper.STORENAME+","
				+MyDatabaseHelper.STOREOWNERNAME+","
				+MyDatabaseHelper.PHONE+","
				+MyDatabaseHelper.ADDRESS+","
				+MyDatabaseHelper.PRODUCTIONNAME+","
				+MyDatabaseHelper.UNITPRICE+") values(?,?,?,?,?,?);";
		Object args[] =new Object[]{customer.getStoreName()
				,customer.getStoreOwnerName(),customer.getPhone(),customer.getAddress()
				,customer.getProductionName(),customer.getUnitPrice()};
		this.db.execSQL(sql, args);
	}
	
	private void insert(Customer customer){
		this.insertNoClose(customer);
		this.db.close();
	}
	private void insertNoClose(List<Customer> customers){
		for(Customer customer:customers){
			this.insertNoClose(customer);
		}
	}
	/**
	 * @param customer
	 */
	public void delete(String storeName){
		String sql="delete from "+MyDatabaseHelper.TABLENAME_CUSTOMER+" where "+MyDatabaseHelper.STORENAME+" =?;";
		Object args[] = new Object[]{storeName};
		this.db.execSQL(sql,args);
		this.db.close();
	}
	
	private void deleteAllNoClose(){
		String sql="delete from "+MyDatabaseHelper.TABLENAME_CUSTOMER+";";
		this.db.execSQL(sql);
	}
	public void updateAll(List<Customer> customers){
		this.deleteAllNoClose();
		this.insertNoClose(customers);
		this.db.close();
	}
	/**
	 * 返回所有的customer
	 */
	public List<Customer> findAll(){
		List<Customer> customers=new ArrayList<Customer>();
		String columns[] = new String[] {MyDatabaseHelper.STORENAME,
				MyDatabaseHelper.STOREOWNERNAME,MyDatabaseHelper.PHONE,
				MyDatabaseHelper.ADDRESS,MyDatabaseHelper.PRODUCTIONNAME,MyDatabaseHelper.UNITPRICE};
		Cursor cursor=this.db.query(MyDatabaseHelper.TABLENAME_CUSTOMER, columns,null, null,null, null,null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			customers.add(new Customer(cursor.getString(0),cursor.getString(1),cursor.getString(2)
					,cursor.getString(3),cursor.getString(4),cursor.getString(5)));
		}
		this.db.close();
		return customers;
	}
	
	/**
	 * 查找Customer，没有返回null
	 * @return
	 */
	public Customer findByStoreName(String storeName){
		Customer customer=null;
		String columns[] = new String[] {MyDatabaseHelper.STORENAME,
				MyDatabaseHelper.STOREOWNERNAME,MyDatabaseHelper.PHONE,
				MyDatabaseHelper.ADDRESS,MyDatabaseHelper.PRODUCTIONNAME,MyDatabaseHelper.UNITPRICE};
		String[] args = {String.valueOf(storeName)};
		Cursor cursor=this.db.query(MyDatabaseHelper.TABLENAME_CUSTOMER, columns,
				MyDatabaseHelper.STORENAME+"=?", args,null, null,null);
		int resultNum=cursor.getCount();
		Log.i("TAG", "======================"+resultNum);
		if(resultNum>0){
			cursor.moveToFirst();
			return new Customer(cursor.getString(0),cursor.getString(1),cursor.getString(2)
					,cursor.getString(3),cursor.getString(4),cursor.getString(5));
		}
		this.db.close();
		return customer;
	}
	
}
