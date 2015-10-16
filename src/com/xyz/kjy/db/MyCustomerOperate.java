package com.xyz.kjy.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyCustomerOperate {
	private SQLiteDatabase db=null;
	public MyCustomerOperate(SQLiteDatabase db) {
		this.db = db;
	}
	private void insertNoClose(Customer customer){
		String sql = "insert into "+MyDatabaseHelper.TABLENAME_CUSTOMER 
				+"("+MyDatabaseHelper.ID+","
				+MyDatabaseHelper.STORENAME+","
				+MyDatabaseHelper.STOREOWNERNAME+","
				+MyDatabaseHelper.PHONE+","
				+MyDatabaseHelper.ADDRESS+","
				+MyDatabaseHelper.PRODUCTIONNAME+","
				+MyDatabaseHelper.UNITPRICE+") values(?,?,?,?,?);";
		Object args[] =new Object[]{customer.getId(),customer.getStoreName()
				,customer.getStoreOwnerName(),customer.getPhone(),customer.getAddress()
				,customer.getProductionName(),customer.getUnitPrice()};
		this.db.execSQL(sql, args);
	}
	
	private void insertNoClose(List<Customer> customers){
		for(Customer customer:customers){
			this.insertNoClose(customer);
		}
	}
	/**
	 * @param customer
	 */
	public void delete(int customerId){
		String sql="delete from "+MyDatabaseHelper.TABLENAME_CUSTOMER+" where "+MyDatabaseHelper.ID+" =?;";
		Object args[] = new Object[]{customerId};
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
		String columns[] = new String[] {MyDatabaseHelper.ID,MyDatabaseHelper.STORENAME,
				MyDatabaseHelper.STOREOWNERNAME,MyDatabaseHelper.PHONE,
				MyDatabaseHelper.ADDRESS,MyDatabaseHelper.PRODUCTIONNAME,MyDatabaseHelper.UNITPRICE};
		Cursor cursor=this.db.query(MyDatabaseHelper.TABLENAME_CUSTOMER, columns,null, null,null, null,null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			customers.add(new Customer(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)
					,cursor.getString(4),cursor.getString(5),cursor.getInt(6)));
		}
		this.db.close();
		return customers;
	}
	
	
	
	
}
