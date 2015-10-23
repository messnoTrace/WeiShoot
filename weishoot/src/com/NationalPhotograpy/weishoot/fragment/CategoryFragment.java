package com.NationalPhotograpy.weishoot.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.find.SearchWeishootActivity;
import com.NationalPhotograpy.weishoot.adapter.CommomAdapter;
import com.NationalPhotograpy.weishoot.adapter.CommomViewHolder;
import com.NationalPhotograpy.weishoot.bean.HotSearchBean;
import com.NationalPhotograpy.weishoot.bean.HotSearchBean.HotSearch;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.UnScrollableGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CategoryFragment extends Fragment {



	private List<HotSearch> list;
	private CommomAdapter<HotSearch> adater;
	private SwipeRefreshLayout srf_category;
	private UnScrollableGridView gv_category;

	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View createView = inflater.inflate(R.layout.fragment_category, null, false);
		mContext=getActivity();
		initView(createView);
		setListener();
		initData();

		return createView;
	}

	private void initView(View createView) {
		srf_category=(SwipeRefreshLayout) createView.findViewById(R.id.sfl_category);
		gv_category=(UnScrollableGridView) createView.findViewById(R.id.gv_category);
		

	}

	private void initData() {

		list = new ArrayList<HotSearch>();
		adater = new CommomAdapter<HotSearch>(mContext, list, R.layout.item_category) {
			@Override
			public void convert(CommomViewHolder mHolder, HotSearch item, int position) {
				mHolder.setText(R.id.tv_category_id, item.Id);
				mHolder.setText(R.id.tv_category, item.keyword);
			}
		};

		gv_category.setAdapter(adater);

		loadData();

	}

	private void loadData() {

		requestGetHotSearch();
	}

	private void setListener() {
		

		gv_category.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(mContext, SearchWeishootActivity.class).putExtra("KeyWord", list.get((int)id).keyword));
			}
		});
		
		srf_category.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				loadData();
			}
		});
	}

	/**
	 * 热门搜索
	 */
	private void requestGetHotSearch() {
		HttpUtils httpUtils = new HttpUtils(1000 * 20);
		RequestParams params = new RequestParams();
		params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
		httpUtils.send(HttpMethod.POST, HttpUrl.GetHotSearch, params, new RequestCallBack<Object>() {

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				if (getActivity() == null) {
					return;
				}
				srf_category.setRefreshing(false);
				WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
				if (getActivity() == null) {
					return;
				}
				String temp = (String) objectResponseInfo.result;
				HotSearchBean hotBean = new Gson().fromJson(temp, new TypeToken<HotSearchBean>() {
				}.getType());
				if (hotBean != null && hotBean.result != null) {
					if ("200".equals(hotBean.result.ResultCode)) {
						list=hotBean.data;
						adater.setData(list);
						srf_category.setRefreshing(false);
					} else {
						WeiShootToast.makeErrorText(getActivity(), hotBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
					}
				} else {
					WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
