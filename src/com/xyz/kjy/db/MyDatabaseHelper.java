package com.xyz.kjy.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 为数据库中表的操作类提供 链接数据库的接口 通过 getWritableDatabase()传入
 * @author xuyizhen
 *
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME="hao.db";
	private static final int DATABASEVERSION=1;
	
	/**
	 * 表customer以及他的字段
	 */
	public static final String TABLENAME_CUSTOMER="customer";
	public static final String ID="id";
	public static final String STORENAME="storeName";
	public static final String STOREOWNERNAME	="storeOwnerName";
	public static final String PHONE	="phone";
	public static final String ADDRESS	="address";
	public static final String PRODUCTIONNAME	="productionName";
	public static final String UNITPRICE="unitPrice";
	
	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}
	public MyDatabaseHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
		// TODO Auto-generated constructor stub
	}
	
	
	private void createTable(SQLiteDatabase db){
		String sql="create   table  "+ TABLENAME_CUSTOMER +" ("+ID             + " integer , "
	                                           +STORENAME  +" varchar(50), " 
	                                           +STOREOWNERNAME +" varchar(50), "
	                                           +PHONE    +" varchar(50), "
	                                           +ADDRESS        +" varchar(50),"
	                                           +PRODUCTIONNAME   +" varchar(50),"
	                                           +UNITPRICE+" integer"+"); ";
		db.execSQL(sql);
		
	}
	private void dropTable(SQLiteDatabase db){
		String sql="drop table if exists "+TABLENAME_CUSTOMER+";";
		db.execSQL(sql);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
//		this.dropTable(db);//这句现在写是每次打开应用更新数据，以后换成每十次打开应用更新数据
		this.createTable(db);		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		this.dropTable(db);
		this.createTable(db);
	}
	
}
