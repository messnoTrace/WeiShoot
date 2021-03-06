package com.NationalPhotograpy.weishoot;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.HomeAdapter;
import com.NationalPhotograpy.weishoot.bean.TopicBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean.TopicData;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshBase;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class TopicDetailActivity extends BaseActivity {

	
	private PullToRefreshListView lv_pulltorefresh;
    private String lastDataTime = "";
    
    private ListView homeListView;
    private HomeAdapter homeAdapter;

    
    

    private boolean isFinish = true;

    private TopicData tempTopicBean;
    

    private TopicData resultTopicBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_detail);
		
		findview();
		bindListener();
		initData();
	}

	
	private void findview(){
		
		 lv_pulltorefresh = (PullToRefreshListView) findViewById(R.id.lv_pulltorefresh);
		 
		   homeListView = lv_pulltorefresh.getRefreshableView();
		 findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		
	}
	private void bindListener(){
		 findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {

				finish();
			}
		});

		 
	      lv_pulltorefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

	            @Override
	            public void onPullDownToRefresh() {
	                // 刷新
	                lastDataTime = "";
	                lv_pulltorefresh.onRefreshComplete();
	            }

	            @Override
	            public void onPullUpToRefresh() {
	                // 加载
	            	 lv_pulltorefresh.onRefreshComplete();
	            }
	        });
		
	}
	private void initData(){
		String ucode=getIntent().getStringExtra("ucode");
		String tcode=getIntent().getStringExtra("tcode");
		 requestGetTopic(ucode,tcode);
	}
	
	
	 private void requestGetTopic(String ucode,String tcode) {
	        HttpUtils httpUtils = new HttpUtils(1000 * 20);
	        RequestParams params = new RequestParams();
	        params.addBodyParameter("UCode", ucode);
	        params.addBodyParameter("PageSize", "5");
	        params.addBodyParameter("DisplayCom", "5");
	        params.addBodyParameter("DisplayGood", "5");
	        params.addBodyParameter("TCode", tcode);
	        params.addBodyParameter("IsPerson", "1");
	      
	        if(UserInfo.getInstance(this).getUserUCode()!=null){
	            params.addBodyParameter("CurrentUCode", UserInfo.getInstance(this).getUserUCode());
	        }
	        // params.addBodyParameter("TType", "");
	        params.addBodyParameter("CreateDate", lastDataTime);
	        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
	        httpUtils.send(HttpMethod.POST, HttpUrl.GetTopic, params, new RequestCallBack<Object>() {

	            @Override
	            public void onFailure(HttpException e, String s) {
	                e.printStackTrace();
	         
	                lv_pulltorefresh.onRefreshComplete();
	                WeiShootToast.makeErrorText(TopicDetailActivity.this, getString(R.string.http_timeout),
	                        WeiShootToast.LENGTH_SHORT).show();
	            }

	            @Override
	            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
	 
	                lv_pulltorefresh.onRefreshComplete();
	                String temp = (String) objectResponseInfo.result;
	                TopicBean topicBean = new Gson().fromJson(temp, new TypeToken<TopicBean>() {
	                }.getType());
	                if (topicBean != null && topicBean.result != null) {
	                    if ("200".equals(topicBean.result.ResultCode)) {
	                        SharePreManager.getInstance(TopicDetailActivity.this).setString("home_topic_json",
	                                temp);
	                        if (homeAdapter != null && "".equals(lastDataTime)) {// 刷新操作
	                            homeAdapter.data.clear();
	                            homeAdapter.indexZans.clear();
	                            homeAdapter.indexShouCangs.clear();
	                        }
	                        if (homeAdapter != null) {
	                            homeAdapter.addData(topicBean.data, tempTopicBean, resultTopicBean,
	                                    isFinish);
	                            resultTopicBean = null;
	                        } else {
	                            homeAdapter = new HomeAdapter(TopicDetailActivity.this, topicBean.data,
	                                    homeListView);
	                            homeListView.setAdapter(homeAdapter);
	                        }
	                        if (topicBean.data.size() > 0) {
	                            lastDataTime = StringsUtil.getStringDate(topicBean.data
	                                    .get(topicBean.data.size() - 1).CreateDate);
	                        }
	                    } else {
	                        WeiShootToast.makeErrorText(TopicDetailActivity.this, topicBean.result.ResultMsg,
	                                WeiShootToast.LENGTH_SHORT).show();
	                    }
	                } else {
	                    WeiShootToast.makeErrorText(TopicDetailActivity.this, getString(R.string.http_timeout),
	                            WeiShootToast.LENGTH_SHORT).show();
	                }
	            }
	        });

	    }

}
