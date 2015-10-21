package com.xyz.kjy.fragment;

import com.example.kjy.R;
import com.xyz.kjy.constant.Constants;
import com.xyz.kjy.db.DispatchInfo;
import com.xyz.kjy.utils.MySharedPreferences;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DispatchInfoFragment extends Fragment {
		
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
			currentView = inflater.inflate(R.layout.fragment_dispachinfo,container,false);
			
			tv_dispatchPerson=(TextView) currentView.findViewById(R.id.txt_dispatchPerson);
			tv_dispatchCar=(TextView) currentView.findViewById(R.id.txt_dispatchCar);
			tv_dispatchStarttime=(TextView) currentView.findViewById(R.id.txt_dispatchStarttime);
			btn_dispatchOver=(Button) currentView.findViewById(R.id.btn_dispatchOver);
			
			btn_dispatchOver.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//本地是否有未发送卸货记录，结束配送
				}
			});
			
			tv_dispatchPerson.setText(MySharedPreferences.getString(this.getActivity(), Constants.UserName, ""));
			tv_dispatchCar.setText(getArguments().getString("dispatchCar"));
			tv_dispatchStarttime.setText(getArguments().getString("dispatchStarttime"));
			return currentView;
		}
}

