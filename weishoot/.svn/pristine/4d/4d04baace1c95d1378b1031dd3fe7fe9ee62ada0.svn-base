
package com.NationalPhotograpy.weishoot.activity.find;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.photograph.WeiShootWebActiivty;
import com.NationalPhotograpy.weishoot.adapter.find.PictureMarketAdapter;
import com.NationalPhotograpy.weishoot.bean.AdvertisingBean;
import com.NationalPhotograpy.weishoot.bean.AdvertisingBean.ADBean;
import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.adcyclegallery.HomeAdCycleGallery;
import com.NationalPhotograpy.weishoot.view.adcyclegallery.HomeAdCycleGalleryAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 图店
 */
public class PictureMarketActivity extends BaseActivity implements OnItemSelectedListener {

    private LinearLayout layout_back;

    private ListView lvContent;

    private ProgressiveDialog progressDialog;

    private HomeAdCycleGallery gallery_ad;

    private HomeAdCycleGalleryAdapter adCycleGalleryAdapter;

    private LinearLayout layout_pointAll;

    private AdvertisingBean advertisingBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_pic_market);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        lvContent = (ListView) findViewById(R.id.lvContent);
    }

    private void initData() {
        requestGetAd();
        requestGetPhotoShopType();
    }

    private void setListener() {
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        refreshPointView(position % advertisingBean.data.size());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addPointView(int size) {
        layout_pointAll.removeAllViews();
        int buttonW = (int) DimensionPixelUtil.getDimensionPixelSize(1, 7, this);
        int buttonH = (int) DimensionPixelUtil.getDimensionPixelSize(1, 7, this);
        LinearLayout.LayoutParams lyp = new LinearLayout.LayoutParams(buttonW, buttonH);
        lyp.setMargins(0, 0, 10, 0);
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(lyp);
            imageView.setBackgroundResource(R.drawable.selector_point);
            layout_pointAll.addView(imageView);
        }
        refreshPointView(0);
    }

    private void refreshPointView(int position) {
        if (advertisingBean.data != null) {
            for (int i = 0; i < advertisingBean.data.size(); i++) {
                ImageView imageView = (ImageView) layout_pointAll.getChildAt(i);
                if (i == position) {
                    imageView.setEnabled(false);
                } else {
                    imageView.setEnabled(true);
                }
            }
        }
    }

    /**
     * 获取图店分类
     */
    private void requestGetPhotoShopType() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetPhotoShopType, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(PictureMarketActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        PhotoShopTypeBean photoBean = new Gson().fromJson(temp,
                                new TypeToken<PhotoShopTypeBean>() {
                                }.getType());
                        if (photoBean != null && photoBean.result != null) {
                            if ("200".equals(photoBean.result.ResultCode)) {
                                PictureMarketAdapter picAdapter = new PictureMarketAdapter(
                                        PictureMarketActivity.this, photoBean.data);
                                lvContent.setAdapter(picAdapter);
                            } else {
                                WeiShootToast.makeErrorText(PictureMarketActivity.this,
                                        photoBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(PictureMarketActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    /**
     * 获取广告
     */
    private void requestGetAd() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "newshop");
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetAd, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                WeiShootToast.makeErrorText(PictureMarketActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                advertisingBean = new Gson().fromJson(temp, new TypeToken<AdvertisingBean>() {
                }.getType());
                if (advertisingBean != null && advertisingBean.result != null) {
                    if ("200".equals(advertisingBean.result.ResultCode)) {
                        View picView = View.inflate(PictureMarketActivity.this,
                                R.layout.view_pic_market, null);
                        RelativeLayout layout_ad = (RelativeLayout) picView
                                .findViewById(R.id.layout_ad);
                        layout_pointAll = (LinearLayout) picView.findViewById(R.id.layout_pointAll);
                        gallery_ad = (HomeAdCycleGallery) picView.findViewById(R.id.gallery_ad);
                        if (StaticInfo.widthPixels == 0) {
                            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
                            StaticInfo.widthPixels = mDisplayMetrics.widthPixels;
                        }
                        int ADHeight = (int) (StaticInfo.widthPixels * Constant.AD_SIZE);
                        int tempHeight = (int) DimensionPixelUtil.getDimensionPixelSize(1, 5,
                                PictureMarketActivity.this);
                        LinearLayout.LayoutParams lyp1 = new LinearLayout.LayoutParams(
                                RelativeLayout.LayoutParams.FILL_PARENT, ADHeight + tempHeight);
                        LinearLayout.LayoutParams lyp2 = new LinearLayout.LayoutParams(
                                RelativeLayout.LayoutParams.FILL_PARENT, ADHeight);
                        layout_ad.setLayoutParams(lyp1);
                        gallery_ad.setLayoutParams(lyp2);
                        gallery_ad.setSoundEffectsEnabled(false);
                        lvContent.addHeaderView(picView);
                        adCycleGalleryAdapter = new HomeAdCycleGalleryAdapter(
                                PictureMarketActivity.this, advertisingBean.data,
                                StaticInfo.widthPixels);
                        addPointView(advertisingBean.data.size());
                        gallery_ad.setAdapter(adCycleGalleryAdapter);
                        gallery_ad.setSelection(adCycleGalleryAdapter.getCount() / 2);
                        gallery_ad.setOnItemSelectedListener(PictureMarketActivity.this);
                        gallery_ad.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                                ADBean adBea = (ADBean) advertisingBean.data.get(position
                                        % advertisingBean.data.size());
                                Intent intent = new Intent(PictureMarketActivity.this,
                                        WeiShootWebActiivty.class);
                                intent.putExtra("url", adBea.ALink);
                                startActivity(intent);
                            }
                        });
                        if (advertisingBean.data.size() > 1) {
                            gallery_ad.startSwitchTimer();// 开启轮偱
                        }
                    } else {
                        WeiShootToast.makeErrorText(PictureMarketActivity.this,
                                advertisingBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    WeiShootToast.makeErrorText(PictureMarketActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
