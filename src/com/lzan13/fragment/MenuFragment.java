package com.lzan13.fragment;


import com.lzan13.main.ConstantQuantity;
import com.lzan13.main.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuFragment extends Fragment {
	
	public TextView menuTitle;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_view, null);
		
		initView(view);
		
		return view;
	}
	
	private void initView(View view){
		
		menuTitle = (TextView)view.findViewById(R.id.menu_title);
		menuTitle.setWidth((int)(ConstantQuantity.screenWidth - ConstantQuantity.screenDensity*50));
		
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

}
