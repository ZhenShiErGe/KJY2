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
	private TextView tvStoreId;
	private TextView tvStoreName;
	private TextView tvStoreBoss;
	private TextView tvProduction;
	private TextView tvUnitPrice;
	private TextView tvAddress;
	private TextView tvPhone;
	
	private ImageView backToMainActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customerinfo);
		Intent intent=this.getIntent();
		
		tvStoreId=(TextView) findViewById(R.id.txt_storeid);
		tvStoreName=(TextView) findViewById(R.id.txt_storename);
		tvStoreBoss=(TextView) findViewById(R.id.txt_storeboss);
		tvProduction=(TextView) findViewById(R.id.txt_storeProduct);
		tvUnitPrice=(TextView) findViewById(R.id.txt_unitprice);
//		tvPhone=findViewById(R.id.txt_)
		tvAddress=(TextView) findViewById(R.id.txt_storeaddress);
		backToMainActivity=(ImageView) findViewById(R.id.customerinfo_back_main);
		
		tvStoreId.setText(intent.getStringExtra(MyDatabaseHelper.ID));
		tvStoreName.setText(intent.getStringExtra(MyDatabaseHelper.STORENAME));
		tvStoreBoss.setText(intent.getStringExtra(MyDatabaseHelper.STOREOWNERNAME));
		tvProduction.setText(intent.getStringExtra(MyDatabaseHelper.PRODUCTIONNAME));
		tvUnitPrice.setText(intent.getStringExtra(MyDatabaseHelper.UNITPRICE));
		tvAddress.setText(intent.getStringExtra(MyDatabaseHelper.ADDRESS));
		
		backToMainActivity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomerInfoActivity.this.finish();
				CustomerInfoActivity.this.overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
//				Intent intent= new Intent(CustomerInfoActivity.this,MainActivity.class);
//				startActivity(intent);
				
			}
		});
		
	}
}
