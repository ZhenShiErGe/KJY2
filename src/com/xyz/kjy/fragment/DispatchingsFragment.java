package com.xyz.kjy.fragment;

import com.example.kjy.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class DispatchingsFragment extends Fragment {
	private View currentView;
	
	public View getCurrentView() {
		return currentView;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentView = inflater.inflate(R.layout.fragment_dispaching,
				container, false);
		
		return currentView;
	}
}
