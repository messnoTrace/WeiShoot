
package com.NationalPhotograpy.weishoot.activity.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.adapter.HomeAdapter;
import com.NationalPhotograpy.weishoot.adapter.find.SearchUserAdapter;
import com.NationalPhotograpy.weishoot.bean.SerchUserBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.HorizontalListView;
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
 * 搜索微摄
 */
public class SearchWeishootActivity extends BaseActivity {

    private PullToRefreshListView lv_pulltorefresh;

    private ListView searchListView;

    private EditText et_search;

    private LinearLayout layout_hint;

    private HorizontalListView listview_head;

    private String KeyWord = "";

    private int pageIndex = 1;

    private SerchUserBean searchBean;

    private ProgressiveDialog progressDialog;

    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_search_weishoot);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        lv_pulltorefresh = (PullToRefreshListView) findViewById(R.id.lv_pulltorefresh);
        searchListView = lv_pulltorefresh.getRefreshableView();
        View searchView = View.inflate(this, R.layout.view_search, null);
        et_search = (EditText) searchView.findViewById(R.id.et_search);
        layout_hint = (LinearLayout) searchView.findViewById(R.id.layout_hint);
        listview_head = (HorizontalListView) searchView.findViewById(R.id.listview_head);
        listview_head.setVisibility(View.GONE);
        searchListView.addHeaderView(searchView);
    }

    private void initData() {
        KeyWord = getIntent().getStringExtra("KeyWord");
        et_search.setText(KeyWord);
        layout_hint.setVisibility(View.GONE);
        requestSerchGetAllUsers();
        requestGetTopicSerch();
    }

    private void setListener() {
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                StringBuilder sbstr = new StringBuilder(s.toString().trim());
                if (sbstr.length() > 0) {
                    layout_hint.setVisibility(View.GONE);
                } else {
                    layout_hint.setVisibility(View.VISIBLE);
                }
            }
        });
        et_search.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {// 修改回车键功能
                    String KeyWord = et_search.getText().toString();
                    if (KeyWord.length() < 2) {
                        WeiShootToast.makeErrorText(SearchWeishootActivity.this, "请输入2个字以上进行搜索",
                                WeiShootToast.LENGTH_SHORT).show();
                        return false;
                    }
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    // 跳转到搜索结果界面
                    // /
                }
                return false;
            }
        });
        lv_pulltorefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh() {
                // 刷新
                lv_pulltorefresh.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh() {
                // 加载
                lv_pulltorefresh.onRefreshComplete();
            }
        });
        listview_head.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(SearchWeishootActivity.this, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, searchBean.data.get(position).UCode);
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME,
                        searchBean.data.get(position).NickName);
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD,
                        searchBean.data.get(position).UserHead);
                startActivity(it);
            }
        });
    }

    /**
     * 搜索用户
     */
    private void requestSerchGetAllUsers() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("PageIndex", "1");
        params.addBodyParameter("PageSize", "10");
        params.addBodyParameter("KeyWord", KeyWord);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.SerchGetAllUsers, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        WeiShootToast.makeErrorText(SearchWeishootActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        searchBean = new Gson().fromJson(temp, new TypeToken<SerchUserBean>() {
                        }.getType());
                        if (searchBean != null && searchBean.result != null) {
                            if ("200".equals(searchBean.result.ResultCode)) {
                                SearchUserAdapter searchUserBean = new SearchUserAdapter(
                                        SearchWeishootActivity.this, searchBean.data);
                                listview_head.setAdapter(searchUserBean);
                                listview_head.setVisibility(View.VISIBLE);
                            } else {
                                WeiShootToast.makeErrorText(SearchWeishootActivity.this,
                                        searchBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(SearchWeishootActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    /**
     * 搜索主题
     */
    private void requestGetTopicSerch() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("pageIndex", pageIndex + "");
        params.addBodyParameter("Key", KeyWord);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetTopicSerch, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        WeiShootToast.makeErrorText(SearchWeishootActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        TopicBean topicBean = new Gson().fromJson(temp, new TypeToken<TopicBean>() {
                        }.getType());
                        if (topicBean != null && topicBean.result != null) {
                            if ("200".equals(topicBean.result.ResultCode)) {
                                if (homeAdapter != null && pageIndex == 1) {// 刷新操作
                                    homeAdapter.data.clear();
                                    homeAdapter.indexZans.clear();
                                    homeAdapter.indexShouCangs.clear();
                                }
                                if (homeAdapter != null) {
                                    homeAdapter.addData(topicBean.data, null, null, true);
                                } else {
                                    homeAdapter = new HomeAdapter(SearchWeishootActivity.this,
                                            topicBean.data, searchListView);
                                    searchListView.setAdapter(homeAdapter);
                                }
                            } else {
                                WeiShootToast.makeErrorText(SearchWeishootActivity.this,
                                        topicBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(SearchWeishootActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
