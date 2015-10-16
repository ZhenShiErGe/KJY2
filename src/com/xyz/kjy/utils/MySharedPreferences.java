package com.xyz.kjy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
	private static final String GLOBALFILENAME="kjy";
		
	public static void putString(Context context,String key,String value){
		SharedPreferences sharedPreferences=context.getSharedPreferences(GLOBALFILENAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static String getString(Context context,String key,String defValue){
		SharedPreferences sharedPreferences=context.getSharedPreferences(GLOBALFILENAME, Activity.MODE_PRIVATE);
		return sharedPreferences.getString(key, defValue);
	}
	public static void putBoolean(Context context,String key,boolean value){
		SharedPreferences sharedPreferences=context.getSharedPreferences(GLOBALFILENAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static boolean getString(Context context,String key,boolean defValue){
		SharedPreferences sharedPreferences=context.getSharedPreferences(GLOBALFILENAME, Activity.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defValue);
	}
	public static boolean contains(Context context,String key){
		SharedPreferences sharedPreferences=context.getSharedPreferences(GLOBALFILENAME, Activity.MODE_PRIVATE);
		return sharedPreferences.contains(key);
	}
}
