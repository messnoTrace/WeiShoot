
package com.NationalPhotograpy.weishoot.activity.shouye;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.registered.LoginActivity;
import com.NationalPhotograpy.weishoot.adapter.shouye.CommentListAdapter;
import com.NationalPhotograpy.weishoot.bean.TopCommentBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean.TopicData;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshBase;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CommentListActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private Button btn_right;

    private PullToRefreshListView lv_pulltorefresh;

    private ListView commListView;

    private TopicData topicData;

    private CommentListAdapter commAdapter;

    private int page = 1;

    private int position;

    private List<TopCommentBean> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_comment_list);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        lv_pulltorefresh = (PullToRefreshListView) findViewById(R.id.lv_pulltorefresh);
        commListView = lv_pulltorefresh.getRefreshableView();

    }

    private void initData() {

        commAdapter = new CommentListAdapter(this);
        commListView.setAdapter(commAdapter);
        comments = new ArrayList<TopCommentBean>();
        topicData = (TopicData) getIntent().getSerializableExtra("data");
        position = getIntent().getIntExtra("position", -1);
        commAdapter.setTopicData(topicData);
        if (topicData != null && topicData.CommentList != null) {
            commAdapter.setData(topicData.CommentList);
        }
        commAdapter.setIndex(position);

    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        lv_pulltorefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh() {
                // 刷新
                page = 1;
                requestGetComment();

            }

            @Override
            public void onPullUpToRefresh() {
                // 加载
                requestGetComment();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_right:
                if (isLogin()) {
                    Intent it = new Intent(CommentListActivity.this, CreateCommentActivity.class);
                    it.putExtra(CreateCommentActivity.ARG_DATA, topicData);
                    it.putExtra(CreateCommentActivity.ARG_TYPE, "parent");
                    it.putExtra("position", position);
                    startActivity(it);
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        requestGetComment();
    }

    private boolean isLogin() {
        if (UserInfo.getInstance(this).isLogin()) {
            return true;
        } else {
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
            return false;
        }

    }

    private void requestGetComment() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("FCode", topicData.TCode);
        params.addBodyParameter("PageIndex", page + "");
        params.addBodyParameter("PageSize", "10");
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.GetComment, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                lv_pulltorefresh.onRefreshComplete();

                WeiShootToast.makeErrorText(CommentListActivity.this,
                        CommentListActivity.this.getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                lv_pulltorefresh.onRefreshComplete();
                Log.v("TAG", "------->" + temp);
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject object = new JSONObject(temp);

                        JSONObject obj = object.getJSONObject("result");
                        if ("200".equals(obj.optString("ResultCode"))) {
                            if (page == 1) {
                                comments.clear();
                            }
                            JSONArray array = object.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject comment = array.getJSONObject(i);
                                TopCommentBean commentBean = new TopCommentBean();
                                commentBean.CCode = comment.optString("CCode");
                                commentBean.PCode = comment.optString("PCode");
                                commentBean.FCode = comment.optString("FCode");
                                commentBean.Contents = comment.optString("Contents");
                                commentBean.CreateDate = comment.optString("CreateDate");
                                commentBean.UCode = comment.optString("UCode");
                                commentBean.NickName = comment.optString("NickName");
                                commentBean.UserHead = comment.optString("UserHead");
                                comments.add(commentBean);
                            }

                            commAdapter.setData(comments);
                            page++;

                        } else {
                            WeiShootToast.makeErrorText(CommentListActivity.this,
                                    obj.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    WeiShootToast.makeErrorText(CommentListActivity.this,
                            CommentListActivity.this.getString(R.string.http_timeout),
                            WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
