package com.xyz.kjy.net;

import java.util.List;

import org.apache.http.cookie.Cookie;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;

import android.app.Application;

public class HttpClientCenter extends Application {
	private static AsyncHttpClient client;
	private static PersistentCookieStore cookieStore;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		client=new AsyncHttpClient();
		client.setTimeout(5000);
//		client.setMaxRetriesAndTimeout(1, 3000);
		cookieStore=new PersistentCookieStore(getApplicationContext());
	}

	public static AsyncHttpClient getAsyncHttpClient(){
		return client;
	}
	
	
	public static void saveCookie(AsyncHttpClient client){
		client.setCookieStore(cookieStore);
	}
	
	public static List<Cookie> getCookie(){
		return cookieStore.getCookies();
	}
	
	public static void clearCookie(){
		cookieStore.clear();
	}
	
	public static PersistentCookieStore  getCookieStore(){
		return cookieStore;
	}
	
}
