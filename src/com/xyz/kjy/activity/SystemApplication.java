package com.xyz.kjy.activity;


import java.util.LinkedList; 
import java.util.List; 
import android.app.Activity; 
import android.app.Application; 
 
/**
 * 此类用于退出android主程序
 * @author xuyizhen
 *
 */
public class SystemApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>(); 
    private static SystemApplication instance; 
 
    private SystemApplication() {   
    } 
    public synchronized static SystemApplication getInstance() { 
        if (null == instance) { 
            instance = new SystemApplication(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
}