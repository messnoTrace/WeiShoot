
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.shouye.ZambiaListAdapter;
import com.NationalPhotograpy.weishoot.adapter.shouye.ZambiaListAdapter.IAfterDeletedListener;
import com.NationalPhotograpy.weishoot.bean.ZambiaBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 赞列表
 */
public class ZambiaActivity extends BaseActivity implements OnClickListener {
    private TextView tv_title, tv_mid_title;

    private PullToRefreshListView lv_pulltorefresh;

    private ZambiaListAdapter zamAdapter;

    private ListView zamListView;

    public static final String ARG_FLAG = "arg_flag";

    public static final String ARG_TCODE = "arg_tcode";

    public static final String ARG_UCODE = "arg_ucode";

    private String tCode = "";

    private String flag = "";

    private String uCode = "";

    private int pageIndex = 1;

    private ProgressiveDialog progressDialog;

    private List<ZambiaBean> zamBias = new ArrayList<ZambiaBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_zambia);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_mid_title = (TextView) findViewById(R.id.tv_mid_title);
        progressDialog = new ProgressiveDialog(this);

        lv_pulltorefresh = (PullToRefreshListView) findViewById(R.id.lv_pulltorefresh);
        zamListView = lv_pulltorefresh.getRefreshableView();
        zamAdapter = new ZambiaListAdapter(this);
        zamListView.setAdapter(zamAdapter);

    }

    private void initData() {
        Intent it = getIntent();
        flag = it.getStringExtra(ARG_FLAG);
        tCode = it.getStringExtra(ARG_TCODE);
        uCode = it.getStringExtra(ARG_UCODE);

        if ("zambia".equals(flag)) {
            requestGetGoodUsers();
            progressDialog.show();
            tv_mid_title.setText("赞");
        }

        if ("fans".equals(flag)) {
            requestGetUsersByUCode();
            progressDialog.show();
            tv_mid_title.setText("粉丝");
        }

        if ("attention".equals(flag)) {
            zamAdapter.setFlag("attention");
            requestGetUserAttentionByUCode();
            progressDialog.show();
            tv_mid_title.setText("关注");
        }

    }

    private void setListener() {
        tv_title.setOnClickListener(this);

        lv_pulltorefresh.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh() {
                // 刷新
                pageIndex = 1;

                if ("zambia".equals(flag)) {
                    requestGetGoodUsers();
                } else if ("fans".equals(flag)) {
                    requestGetUsersByUCode();
                } else if ("attention".equals(flag)) {
                    requestGetUserAttentionByUCode();
                }

            }

            @Override
            public void onPullUpToRefresh() {
                // 加载

                if ("zambia".equals(flag)) {
                    requestGetGoodUsers();
                } else if ("fans".equals(flag)) {
                    requestGetUsersByUCode();
                } else if ("attention".equals(flag)) {
                    requestGetUserAttentionByUCode();
                }
            }
        });

        zamAdapter.setOnAfterDeleteListener(new IAfterDeletedListener() {

            @Override
            public void afterDelete(int position) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title:
                finish();
                break;

            default:
                break;
        }

    }

    private void requestGetGoodUsers() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("FCode", tCode);
        params.addBodyParameter("PageIndex", pageIndex + "");
        params.addBodyParameter("PageSize", 10 + "");
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetGoodUsers, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(ZambiaActivity.this,
                                ZambiaActivity.this.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {

                                    if (pageIndex == 1) {
                                        zamBias.clear();
                                    }
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject json = array.getJSONObject(i);
                                        ZambiaBean bean = new ZambiaBean();
                                        zamBias.add(bean.toJson(json));
                                    }

                                    zamAdapter.setData(zamBias);
                                    pageIndex++;
                                } else {
                                    WeiShootToast.makeErrorText(ZambiaActivity.this,
                                            obj.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(ZambiaActivity.this,
                                    ZambiaActivity.this.getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestGetUsersByUCode() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("PageIndex", pageIndex + "");
        params.addBodyParameter("currentUCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        Log.v("TAG", uCode + ", " + UserInfo.getInstance(this).getUserUCode());
        httpUtils.send(HttpMethod.POST, HttpUrl.GetUsersByUCode, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(ZambiaActivity.this,
                                ZambiaActivity.this.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        Log.v("TAG", "---------GetUsersByUCode--->" + temp);
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {

                                    if (pageIndex == 1) {
                                        zamBias.clear();
                                    }
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject json = array.getJSONObject(i);
                                        ZambiaBean bean = new ZambiaBean();
                                        zamBias.add(bean.toJson(json));
                                    }

                                    zamAdapter.setData(zamBias);
                                    pageIndex++;
                                } else {
                                    WeiShootToast.makeErrorText(ZambiaActivity.this,
                                            obj.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(ZambiaActivity.this,
                                    ZambiaActivity.this.getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestGetUserAttentionByUCode() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("PageIndex", pageIndex + "");
        params.addBodyParameter("currentUCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetUserAttentionByUCode, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(ZambiaActivity.this,
                                ZambiaActivity.this.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {

                                    if (pageIndex == 1) {
                                        zamBias.clear();
                                    }
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject json = array.getJSONObject(i);
                                        ZambiaBean bean = new ZambiaBean();
                                        zamBias.add(bean.toJson(json));
                                    }

                                    zamAdapter.setData(zamBias);
                                    pageIndex++;
                                } else {
                                    WeiShootToast.makeErrorText(ZambiaActivity.this,
                                            obj.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(ZambiaActivity.this,
                                    ZambiaActivity.this.getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
