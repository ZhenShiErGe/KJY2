package com.xyz.kjy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
	private static final String GLOBALFILENAME="kjy";
	private SharedPreferences sharedPreferences;
	public MySharedPreferences(Context context) {
		sharedPreferences=context.getSharedPreferences(GLOBALFILENAME, Activity.MODE_PRIVATE);
	}
	
	public void putString(String key,String value){
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public String getString(String key,String defValue){
		return sharedPreferences.getString(key, defValue);
	}
	
	public boolean contains(String key){
		return sharedPreferences.contains(key);
	}
}
