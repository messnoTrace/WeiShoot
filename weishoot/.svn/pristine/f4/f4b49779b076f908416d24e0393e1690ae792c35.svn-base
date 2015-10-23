
package com.NationalPhotograpy.weishoot.activity.find;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.find.DiscoverImgAdapter;
import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean.PhotoShopBean;
import com.NationalPhotograpy.weishoot.bean.PictureWallBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshBase.OnRefreshListener2;
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
 * 图片墙
 */
public class DiscoverImgActivity extends BaseActivity implements OnClickListener {
    private TextView tv_title;

    private PullToRefreshListView lv_pulltorefresh;

    private DiscoverImgAdapter adapter;

    private ListView listView;

    private ProgressiveDialog progressDialog;

    private List<PhotoShopBean> data = new ArrayList<PhotoShopBean>();

    private int pageIndex = 1;

    private String fId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_discover_img);

        initView();
        setData();
        setListener();

    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_pulltorefresh = (PullToRefreshListView) findViewById(R.id.lv_pulltorefresh);
        listView = lv_pulltorefresh.getRefreshableView();
        adapter = new DiscoverImgAdapter(this);
        listView.setAdapter(adapter);
    }

    private void setData() {

        if (getIntent().hasExtra("fId")) {
            fId = getIntent().getStringExtra("fId");
        }
        requestGetPhotosAll();
        progressDialog.show();
    }

    private void setListener() {
        tv_title.setOnClickListener(this);
        lv_pulltorefresh.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh() {
                pageIndex = 1;
                requestGetPhotosAll();

            }

            @Override
            public void onPullUpToRefresh() {
                requestGetPhotosAll();
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        finish();
    }

    /**
     * 获取图片墙
     */
    private void requestGetPhotosAll() {

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("pageIndex", pageIndex + "");
        params.addBodyParameter("pageSize", "14");
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        if (fId.length() > 0) {
            params.addBodyParameter("fId", fId);
        }
        httpUtils.send(HttpMethod.POST, HttpUrl.GetPhotosAll, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        lv_pulltorefresh.onRefreshComplete();
                        WeiShootToast.makeErrorText(DiscoverImgActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        lv_pulltorefresh.onRefreshComplete();
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        PictureWallBean picWallBean = new Gson().fromJson(temp,
                                new TypeToken<PictureWallBean>() {
                                }.getType());
                        if (picWallBean != null && picWallBean.result != null) {
                            if ("200".equals(picWallBean.result.ResultCode)) {
                                if (pageIndex == 1) {
                                    data.clear();
                                }
                                data.addAll(picWallBean.data);
                                adapter.setData(data);
                                pageIndex++;
                            } else {
                                WeiShootToast.makeErrorText(DiscoverImgActivity.this,
                                        picWallBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(DiscoverImgActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }
}
