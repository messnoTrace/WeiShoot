
package com.NationalPhotograpy.weishoot.activity.find;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.HomeAdapter;
import com.NationalPhotograpy.weishoot.bean.TopCommentBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
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

/**
 * 微摄精品
 */
public class WeiShootJPActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private PullToRefreshListView lv_pulltorefresh;

    private ListView homeListView;

    private HomeAdapter homeAdapter;

    private String lastDataTime = "";

    private ProgressiveDialog progressDialog;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_weishoot_jp);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        lv_pulltorefresh = (PullToRefreshListView) findViewById(R.id.lv_pulltorefresh);
        homeListView = lv_pulltorefresh.getRefreshableView();
        progressDialog = new ProgressiveDialog(this);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if ("comment".equals(arg1.getAction())) {
                    if (homeAdapter != null) {
                        TopCommentBean bean = (TopCommentBean) arg1.getSerializableExtra("data");
                        int position = arg1.getIntExtra("position", -1);
                        if (position != -1) {
                            homeAdapter.data.get(position).CommentList.add(0, bean);
                            homeAdapter.notifyDataSetChanged();
                        }

                    }

                }

            }
        };

        IntentFilter filter = new IntentFilter("comment");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initData() {
        initCacheData();
        requestGetTopic();
        progressDialog.show();

    }

    private void initCacheData() {
        String json = SharePreManager.getInstance(this).getJpString();
        if (!TextUtils.isEmpty(json)) {
            TopicBean topicBean = new Gson().fromJson(json, new TypeToken<TopicBean>() {
            }.getType());
            homeAdapter = new HomeAdapter(this, topicBean.data, homeListView);
            homeListView.setAdapter(homeAdapter);
        }
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        lv_pulltorefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh() {
                // 刷新
                // 刷新
                lastDataTime = "";
                requestGetTopic();
            }

            @Override
            public void onPullUpToRefresh() {
                // 加载
                requestGetTopic();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取首页主题
     */
    private void requestGetTopic() {

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("PageSize", "5");
        params.addBodyParameter("DisplayCom", "5");
        params.addBodyParameter("DisplayGood", "3");
        params.addBodyParameter("CurrentUCode", UserInfo.getInstance(this).getUserUCode());
        // params.addBodyParameter("TType", "");
        params.addBodyParameter("IsRecommend", "1");
        params.addBodyParameter("CreateDate", lastDataTime);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetTopic, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                progressDialog.dismiss();
                lv_pulltorefresh.onRefreshComplete();
                WeiShootToast.makeErrorText(WeiShootJPActivity.this,
                        WeiShootJPActivity.this.getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                lv_pulltorefresh.onRefreshComplete();
                progressDialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                Log.v("TAG", "wwwww" + temp);
                TopicBean topicBean = new Gson().fromJson(temp, new TypeToken<TopicBean>() {
                }.getType());
                if (topicBean != null && topicBean.result != null) {
                    if ("200".equals(topicBean.result.ResultCode)) {
                        SharePreManager.getInstance(WeiShootJPActivity.this).setJpString(temp);
                        if (homeAdapter != null && "".equals(lastDataTime)) {// 刷新操作
                            homeAdapter.data.clear();
                            homeAdapter.indexZans.clear();
                            homeAdapter.indexShouCangs.clear();
                        }
                        if (homeAdapter != null) {
                            homeAdapter.addData(topicBean.data, null, null, true);
                        } else {
                            homeAdapter = new HomeAdapter(WeiShootJPActivity.this, topicBean.data,
                                    homeListView);
                            homeListView.setAdapter(homeAdapter);
                        }
                        if (topicBean.data.size() > 0) {
                            lastDataTime = StringsUtil.getStringDate(topicBean.data
                                    .get(topicBean.data.size() - 1).CreateDate);
                        }
                    } else {
                        WeiShootToast.makeErrorText(WeiShootJPActivity.this,
                                topicBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(WeiShootJPActivity.this,
                            WeiShootJPActivity.this.getString(R.string.http_timeout),
                            WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
