/**   
 * Copyright © 2014 All rights reserved.
 * 
 * @Title: SlidingPaneContentFragment.java 
 * @Prject: SlidingPane
 * @Package: com.example.slidingpane 
 * @Description: TODO
 * @author: raot  719055805@qq.com
 * @date: 2014年9月5日 上午10:44:01 
 * @version: V1.0   
 */
package com.xyz.kjy.fragment;



import com.example.kjy.R;
import com.xyz.kjy.db.DispatchInfo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DispatchInfoFragment extends Fragment{
	
	private View currentView;
    private TextView tv_dispatchPerson;
    private TextView tv_dispatchCar;
    private TextView tv_dispatchStarttime;
    private Button btn_dispatchOver;
    
	
	public void setCurrentViewPararms(FrameLayout.LayoutParams layoutParams) {
		currentView.setLayoutParams(layoutParams);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentView = inflater.inflate(R.layout.,
				container, false);
		tv_dispatchPerson=currentView.findViewById(R.id.)
		return currentView;
	}


}
