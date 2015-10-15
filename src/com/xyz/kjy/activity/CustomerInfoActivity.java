package com.xyz.kjy.activity;

import com.example.kjy.R;
import com.xyz.kjy.db.MyDatabaseHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerInfoActivity extends FragmentActivity {
	private TextView tvCustomerName;
	private TextView tvCustomerPerson;
	private TextView tvPhoneNumber;
	private TextView tvAddress;
	
	private ImageView backToMainActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customerinfo);
		Intent intent=this.getIntent();
		
		tvCustomerName=(TextView) findViewById(R.id.txt_customername);
		tvCustomerPerson=(TextView) findViewById(R.id.txt_customerperson);
		tvPhoneNumber=(TextView) findViewById(R.id.txt_phonenumber);
		tvAddress=(TextView) findViewById(R.id.txt_address);
		backToMainActivity=(ImageView) findViewById(R.id.customerinfo_back_main);
		
		tvCustomerName.setText(intent.getStringExtra(MyDatabaseHelper.CUSATOMERNAME));
		tvCustomerPerson.setText(intent.getStringExtra(MyDatabaseHelper.CUSTOMERPERSON));
		tvPhoneNumber.setText(intent.getStringExtra(MyDatabaseHelper.PHONENUMBER));
		tvAddress.setText(intent.getStringExtra(MyDatabaseHelper.ADDRESS));
		
		backToMainActivity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomerInfoActivity.this.finish();
				CustomerInfoActivity.this.overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
//				Intent intent= new Intent(CustomerInfoActivity.this,MainActivity.class);
//				startActivity(intent);
				
			}
		});
		
	}
}
