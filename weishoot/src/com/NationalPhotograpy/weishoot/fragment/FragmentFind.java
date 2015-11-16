
package com.NationalPhotograpy.weishoot.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.find.DiscoverImgActivity;
import com.NationalPhotograpy.weishoot.activity.find.PictureMarketActivity;
import com.NationalPhotograpy.weishoot.activity.find.RecommendedActivity;
import com.NationalPhotograpy.weishoot.activity.find.SearchWeishootActivity;
import com.NationalPhotograpy.weishoot.activity.find.WeiShootJPActivity;
import com.NationalPhotograpy.weishoot.activity.photograph.WeiShootWebActiivty;
import com.NationalPhotograpy.weishoot.activity.registered.LoginActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.bean.AdvertisingBean;
import com.NationalPhotograpy.weishoot.bean.AdvertisingBean.ADBean;
import com.NationalPhotograpy.weishoot.bean.HotSearchBean;
import com.NationalPhotograpy.weishoot.bean.HotSearchBean.HotSearch;
import com.NationalPhotograpy.weishoot.bean.RecommendBean;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean.UserInfos;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.adcyclegallery.HomeAdCycleGallery;
import com.NationalPhotograpy.weishoot.view.adcyclegallery.HomeAdCycleGalleryAdapter;
import com.NationalPhotograpy.weishoot.view.zxing.MipcaCaptureActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 发现
 */
public class FragmentFind extends Fragment implements OnClickListener, OnItemSelectedListener {

    private Button btn_dasai, btn_tudian, btn_sys, btn_tupianqiang, btn_wsjingpin;

    private HomeAdCycleGallery gallery_ad;

    private HomeAdCycleGalleryAdapter adCycleGalleryAdapter;

    private LinearLayout layout_pointAll, layout_search_hint, layout_tuijian, layout_hot;

    private AdvertisingBean advertisingBean;

    private EditText et_search;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;

    private Button btn_allCommended;

    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6;

    private ProgressiveDialog progressDialog;

    private RelativeLayout layout_ad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View createView = inflater.inflate(R.layout.fragment_find, null, false);
        initView(createView);
        initData();
        setListener();
        return createView;
    }

    private void initView(View createView) {
        progressDialog = new ProgressiveDialog(getActivity());
        tv_1 = (TextView) createView.findViewById(R.id.tv_1);
        tv_2 = (TextView) createView.findViewById(R.id.tv_2);
        tv_3 = (TextView) createView.findViewById(R.id.tv_3);
        tv_4 = (TextView) createView.findViewById(R.id.tv_4);
        tv_5 = (TextView) createView.findViewById(R.id.tv_5);
        tv_6 = (TextView) createView.findViewById(R.id.tv_6);
        layout_hot = (LinearLayout) createView.findViewById(R.id.layout_hot);
        layout_search_hint = (LinearLayout) createView.findViewById(R.id.layout_search_hint);
        layout_ad = (RelativeLayout) createView.findViewById(R.id.layout_ad);
        et_search = (EditText) createView.findViewById(R.id.et_search);
        btn_tudian = (Button) createView.findViewById(R.id.btn_tudian);
        btn_sys = (Button) createView.findViewById(R.id.btn_sys);
        btn_tupianqiang = (Button) createView.findViewById(R.id.btn_tupianqiang);
        btn_wsjingpin = (Button) createView.findViewById(R.id.btn_wsjingpin);
        btn_dasai = (Button) createView.findViewById(R.id.btn_dasai);
        layout_pointAll = (LinearLayout) createView.findViewById(R.id.layout_pointAll);
        layout_tuijian = (LinearLayout) createView.findViewById(R.id.layout_tuijian);
        btn_allCommended = (Button) createView.findViewById(R.id.btn_allCommended);
        gallery_ad = (HomeAdCycleGallery) createView.findViewById(R.id.gallery_ad);
        gallery_ad.setSoundEffectsEnabled(false);
        imageLoader = ImageLoader.getInstance();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
        layout_ad.setVisibility(View.GONE);
    }

    private void initData() {
        requestGetAd();
        requestGetRecommendedUsers();
        requestGetHotSearch();
    }

    private void setListener() {
        btn_tudian.setOnClickListener(this);
        btn_sys.setOnClickListener(this);
        btn_tupianqiang.setOnClickListener(this);
        btn_wsjingpin.setOnClickListener(this);
        btn_dasai.setOnClickListener(this);
        btn_allCommended.setOnClickListener(this);
        gallery_ad.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ADBean adBea = (ADBean) advertisingBean.data.get(position
                        % advertisingBean.data.size());
                Intent intent = new Intent(getActivity(), WeiShootWebActiivty.class);
                intent.putExtra("url", adBea.ALink);
                startActivity(intent);
            }
        });
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
                    layout_search_hint.setVisibility(View.GONE);
                } else {
                    layout_search_hint.setVisibility(View.VISIBLE);
                }
            }
        });
        et_search.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {// 修改回车键功能
                    String KeyWord = et_search.getText().toString();
                    if (KeyWord.length() < 2) {
                        WeiShootToast.makeErrorText(getActivity(), "请输入2个字以上进行搜索",
                                WeiShootToast.LENGTH_SHORT).show();
                        return true;
                    }
                    // 先隐藏键盘
                    ((InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // 跳转到搜索结果界面
                    Intent it = new Intent(getActivity(), SearchWeishootActivity.class);
                    it.putExtra("KeyWord", et_search.getText().toString());
                    getActivity().startActivity(it);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.btn_dasai:
                it = new Intent(getActivity(), WeiShootWebActiivty.class);
                String postData = "u=" + UserInfo.getInstance(getActivity()).getUserLoginName()
                        + "&p=" + UserInfo.getInstance(getActivity()).getUserPassword();
                it.putExtra("url", "http://weishoot.com/Campaign/CampList");
                it.putExtra("postData", postData);
                startActivity(it);
                break;
            case R.id.btn_tudian:
                if (!com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo.getInstance(
                        getActivity()).isLogin()) {
                    it = new Intent(getActivity(), LoginActivity.class);
                    startActivity(it);
                    return;
                }
                it = new Intent(getActivity(), PictureMarketActivity.class);
                startActivity(it);
                break;
            case R.id.btn_sys:
                it = new Intent(getActivity(), MipcaCaptureActivity.class);
                startActivity(it);
                break;
            case R.id.btn_tupianqiang:
                it = new Intent(getActivity(), DiscoverImgActivity.class);
                startActivity(it);
                break;
            case R.id.btn_wsjingpin:
                it = new Intent(getActivity(), WeiShootJPActivity.class);
                startActivity(it);
                break;
            case R.id.btn_allCommended:
                it = new Intent(getActivity(), RecommendedActivity.class);
                startActivity(it);
                break;
            case R.id.tv_1:
                it = new Intent(getActivity(), SearchWeishootActivity.class);
                it.putExtra("KeyWord", ((TextView) v).getText().toString());
                getActivity().startActivity(it);
                break;
            case R.id.tv_2:
                it = new Intent(getActivity(), SearchWeishootActivity.class);
                it.putExtra("KeyWord", ((TextView) v).getText().toString());
                getActivity().startActivity(it);
                break;
            case R.id.tv_3:
                it = new Intent(getActivity(), SearchWeishootActivity.class);
                it.putExtra("KeyWord", ((TextView) v).getText().toString());
                getActivity().startActivity(it);
                break;
            case R.id.tv_4:
                it = new Intent(getActivity(), SearchWeishootActivity.class);
                it.putExtra("KeyWord", ((TextView) v).getText().toString());
                getActivity().startActivity(it);
                break;
            case R.id.tv_5:
                it = new Intent(getActivity(), SearchWeishootActivity.class);
                it.putExtra("KeyWord", ((TextView) v).getText().toString());
                getActivity().startActivity(it);
                break;
            case R.id.tv_6:
                it = new Intent(getActivity(), SearchWeishootActivity.class);
                it.putExtra("KeyWord", ((TextView) v).getText().toString());
                getActivity().startActivity(it);
                break;
            default:
                break;
        }
    }

    private void addPointView(int size) {
        layout_pointAll.removeAllViews();
        int buttonW = (int) DimensionPixelUtil.getDimensionPixelSize(1, 7, getActivity());
        int buttonH = (int) DimensionPixelUtil.getDimensionPixelSize(1, 7, getActivity());
        LinearLayout.LayoutParams lyp = new LinearLayout.LayoutParams(buttonW, buttonH);
        lyp.setMargins(0, 0, 10, 0);
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getActivity());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        refreshPointView(position % advertisingBean.data.size());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setTuiJianLayout(List<UserInfos> data) {
        btn_allCommended.setVisibility(View.VISIBLE);
        layout_tuijian.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            final String uCode = data.get(i).UCode;
            View tjView = LayoutInflater.from(getActivity()).inflate(R.layout.item_attention_user,
                    null);
            ImageView iv_com_head = (ImageView) tjView.findViewById(R.id.iv_com_head);
            TextView tv_com_name = (TextView) tjView.findViewById(R.id.tv_com_name);
            TextView tv_com_content = (TextView) tjView.findViewById(R.id.tv_com_content);
            Button btn_guanzhu = (Button) tjView.findViewById(R.id.btn_guanzhu);
            imageLoader.displayImage(data.get(i).UserHead, iv_com_head, mOptionsHead);
            setTuiJianListener(tjView, data.get(i));
            tv_com_name.setText(data.get(i).NickName);
            tv_com_content.setText(data.get(i).Introduction);
            if ("1".equals(data.get(i).IsAttention)) {
                btn_guanzhu.setVisibility(View.GONE);
            } else {
                btn_guanzhu.setVisibility(View.VISIBLE);
            }

            btn_guanzhu.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!UserInfo.getInstance(getActivity()).isLogin()) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                        return;
                    }

                    requestAttentionOption(uCode);
                }
            });

            layout_tuijian.addView(tjView);
        }
    }

    private void setTuiJianListener(View iv_com_head, final UserInfos userInfo) {
        iv_com_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, userInfo.UCode);
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, userInfo.NickName);
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, userInfo.UserHead);
                startActivity(it);
            }
        });
    }

    private void setHotLayout(List<HotSearch> data) {
        layout_hot.setVisibility(View.VISIBLE);
        tv_1.setText(data.get(0).keyword);
        tv_2.setText(data.get(1).keyword);
        tv_3.setText(data.get(2).keyword);
        tv_4.setText(data.get(3).keyword);
        tv_5.setText(data.get(4).keyword);
        tv_6.setText(data.get(5).keyword);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_5.setOnClickListener(this);
        tv_6.setOnClickListener(this);
    }

    /**
     * 获取广告
     */
    private void requestGetAd() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "newsearch");
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetAd, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                if (getActivity() == null) {
                    return;
                }
                String temp = (String) objectResponseInfo.result;
                advertisingBean = new Gson().fromJson(temp, new TypeToken<AdvertisingBean>() {
                }.getType());
                if (advertisingBean != null && advertisingBean.result != null) {
                    if ("200".equals(advertisingBean.result.ResultCode)) {
                        layout_ad.setVisibility(View.VISIBLE);
                        if (StaticInfo.widthPixels == 0) {
                            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay()
                                    .getMetrics(mDisplayMetrics);
                            StaticInfo.widthPixels = mDisplayMetrics.widthPixels;
                        }
                        int ADHeight = (int) (StaticInfo.widthPixels * Constant.AD_SIZE);
                        LinearLayout.LayoutParams lyp1 = new LinearLayout.LayoutParams(
                                RelativeLayout.LayoutParams.FILL_PARENT, ADHeight);
                        layout_ad.setLayoutParams(lyp1);
                        gallery_ad.setLayoutParams(lyp1);
                        adCycleGalleryAdapter = new HomeAdCycleGalleryAdapter(getActivity(),
                                advertisingBean.data, StaticInfo.widthPixels);
                        addPointView(advertisingBean.data.size());
                        gallery_ad.setAdapter(adCycleGalleryAdapter);
                        gallery_ad.setSelection(adCycleGalleryAdapter.getCount() / 2);
                        gallery_ad.setOnItemSelectedListener(FragmentFind.this);
                        if (advertisingBean.data.size() > 1) {
                            gallery_ad.startSwitchTimer();// 开启轮偱
                        }
                    } else {
                        // /
                    }
                } else {
                    // /
                }
            }
        });

    }

    /**
     * 获取推荐用户
     */
    private void requestGetRecommendedUsers() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", SharePreManager.getInstance(getActivity()).getUserUCode());
        params.addBodyParameter("DisplayNum", "4");
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetRecommendedUsers, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        if (getActivity() == null) {
                            return;
                        }
                        WeiShootToast.makeErrorText(getActivity(),
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        if (getActivity() == null) {
                            return;
                        }
                        String temp = (String) objectResponseInfo.result;
                        RecommendBean userBean = new Gson().fromJson(temp,
                                new TypeToken<RecommendBean>() {
                                }.getType());
                        if (userBean != null && userBean.result != null) {
                            if ("200".equals(userBean.result.ResultCode)) {
                                setTuiJianLayout(userBean.data);
                            } else {
                                WeiShootToast.makeErrorText(getActivity(),
                                        userBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(getActivity(),
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
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
        httpUtils.send(HttpMethod.POST, HttpUrl.GetHotSearch, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        if (getActivity() == null) {
                            return;
                        }
                        WeiShootToast.makeErrorText(getActivity(),
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        if (getActivity() == null) {
                            return;
                        }
                        String temp = (String) objectResponseInfo.result;
                        HotSearchBean hotBean = new Gson().fromJson(temp,
                                new TypeToken<HotSearchBean>() {
                                }.getType());
                        if (hotBean != null && hotBean.result != null) {
                            if ("200".equals(hotBean.result.ResultCode)) {
                                setHotLayout(hotBean.data);
                            } else {
                                WeiShootToast.makeErrorText(getActivity(),
                                        hotBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(getActivity(),
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void requestAttentionOption(String fCode) {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("FCode", fCode);
        params.addBodyParameter("PCode", UserInfo.getInstance(getActivity()).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AttentionOption, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        if (getActivity() == null) {
                            return;
                        }
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(getActivity(),
                                getActivity().getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        if (getActivity() == null) {
                            return;
                        }
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {
                                    WeiShootToast.makeErrorText(getActivity(), "关注成功",
                                            WeiShootToast.LENGTH_SHORT).show();
                                    requestGetRecommendedUsers();
                                } else {
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(getActivity(),
                                    getActivity().getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
