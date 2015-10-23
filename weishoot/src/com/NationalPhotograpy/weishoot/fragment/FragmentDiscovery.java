package com.NationalPhotograpy.weishoot.fragment;

import com.NationalPhotograpy.weishoot.FindPeopleActivity;
import com.NationalPhotograpy.weishoot.LookAroundActivity;
import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.find.DiscoverImgActivity;
import com.NationalPhotograpy.weishoot.view.zxing.MipcaCaptureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentDiscovery  extends Fragment implements OnClickListener{
	
	private Button btn_sao,btn_lookaround,btn_find,btn_pics;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.fragment_discovery, null);
		findViews(view);
		bindListener();
		initData();
		return view;
	}
	private void findViews(View view){
		btn_sao=(Button) view.findViewById(R.id.btn_sao);
		btn_lookaround=(Button) view.findViewById(R.id.btn_lookaround);
		btn_find=(Button) view.findViewById(R.id.btn_find);
		btn_pics=(Button) view.findViewById(R.id.btn_pics);
		
		
		
	}
	private void bindListener(){
		
		btn_pics.setOnClickListener(this);
		btn_find.setOnClickListener(this);
		btn_lookaround.setOnClickListener(this);
		btn_sao.setOnClickListener(this);
	}
	private void initData(){}
	@Override
	public void onClick(View v) {
		
		
		Intent it;
		switch (v.getId()) {
		case R.id.btn_sao:
	           it = new Intent(getActivity(), MipcaCaptureActivity.class);
               startActivity(it);
			break;
		case R.id.btn_find:
			it=new Intent(getActivity(),FindPeopleActivity.class);
			startActivity(it);
			break;
		case R.id.btn_lookaround:
			startActivity(new Intent(getActivity(),LookAroundActivity.class));
			break;
		case R.id.btn_pics:
	        it = new Intent(getActivity(), DiscoverImgActivity.class);
            startActivity(it);
			break;

		default:
			break;
		}
	}

}
