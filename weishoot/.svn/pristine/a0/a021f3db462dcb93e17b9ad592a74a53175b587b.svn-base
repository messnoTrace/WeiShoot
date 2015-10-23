
package com.NationalPhotograpy.weishoot.activity.shouye;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.find.ChongzhiActivity;
import com.NationalPhotograpy.weishoot.bean.BuyImageBean;
import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean.PhotoShopBean;
import com.NationalPhotograpy.weishoot.bean.PictureDetailBean;
import com.NationalPhotograpy.weishoot.bean.ShareBean;
import com.NationalPhotograpy.weishoot.bean.UserCionBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.utils.imageloader.core.DisplayImageOptions;
import com.NationalPhotograpy.weishoot.utils.imageloader.core.ImageLoader;
import com.NationalPhotograpy.weishoot.utils.imageloader.core.display.RoundedBitmapDisplayer;
import com.NationalPhotograpy.weishoot.view.DialogViews_typeAsk;
import com.NationalPhotograpy.weishoot.view.DialogViews_typeAsk.DialogViews_ask;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.SharePopupWindow;
import com.NationalPhotograpy.weishoot.view.album.AlbumViewSinglePager;
import com.NationalPhotograpy.weishoot.view.album.MatrixImageView.OnSingleTapListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PictureDetailSingleActivity extends BaseActivity implements OnClickListener,
        OnSingleTapListener {

    private ImageButton ibn_back;

    private ImageView iv_head;

    private TextView tv_name, tv_time, tv_picSum, tv_picCurrent;

    private Button btn_gz;

    private AlbumViewSinglePager mViewPager;

    private RelativeLayout layout_picDetail, layout_picBuy, layout_picShare;

    private RelativeLayout layout_top, layoutForShare;

    private LinearLayout layout_bottom;

    private DisplayImageOptions mOptionsHead;

    private ScrollView scrollView_picInfo;

    private TextView tv01, tv02, tv03, tv04, tv05, tv06, tv07, tv08, tv09, tv10, tv11;

    private PhotoShopBean photoShopBean;

    private SharePopupWindow showShareWindow;

    private ProgressiveDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.at_pic_single_detail);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        dialog = new ProgressiveDialog(this);
        showShareWindow = new SharePopupWindow(this);
        ibn_back = (ImageButton) findViewById(R.id.ibn_back);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_picSum = (TextView) findViewById(R.id.tv_picSum);
        tv_picCurrent = (TextView) findViewById(R.id.tv_picCurrent);
        btn_gz = (Button) findViewById(R.id.btn_gz);
        mViewPager = (AlbumViewSinglePager) findViewById(R.id.mViewPager);
        layout_picDetail = (RelativeLayout) findViewById(R.id.layout_picDetail);
        layout_picBuy = (RelativeLayout) findViewById(R.id.layout_picBuy);
        layout_picShare = (RelativeLayout) findViewById(R.id.layout_picShare);
        layoutForShare = (RelativeLayout) findViewById(R.id.layoutForShare);
        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        scrollView_picInfo = (ScrollView) findViewById(R.id.scrollView_picInfo);
        tv01 = (TextView) findViewById(R.id.tv01);
        tv02 = (TextView) findViewById(R.id.tv02);
        tv03 = (TextView) findViewById(R.id.tv03);
        tv04 = (TextView) findViewById(R.id.tv04);
        tv05 = (TextView) findViewById(R.id.tv05);
        tv06 = (TextView) findViewById(R.id.tv06);
        tv07 = (TextView) findViewById(R.id.tv07);
        tv08 = (TextView) findViewById(R.id.tv08);
        tv09 = (TextView) findViewById(R.id.tv09);
        tv10 = (TextView) findViewById(R.id.tv10);
        tv11 = (TextView) findViewById(R.id.tv11);

    }

    private void initData() {
        photoShopBean = (PhotoShopBean) getIntent().getSerializableExtra("PhotoShopBean");
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
        ImageLoader.getInstance().displayImage(photoShopBean.UserHead, iv_head, mOptionsHead);
        tv_name.setText(photoShopBean.NickName);
        tv_time.setVisibility(View.GONE);
        if ("1".equals(photoShopBean.IsAttention)) {
            btn_gz.setVisibility(View.GONE);
        } else {
            btn_gz.setVisibility(View.VISIBLE);
        }
        if ("1".equals(photoShopBean.IsSale)) {
            if ("1".equals(photoShopBean.IsBuyed)) {
                layout_picBuy.setVisibility(View.VISIBLE);
                // layout_picBuy.setBackgroundResource(resid);//保存图片按钮
            } else {
                layout_picBuy.setVisibility(View.VISIBLE);
            }
        } else {
            layout_picBuy.setVisibility(View.GONE);
        }
        mViewPager.setAdapter(mViewPager.new ViewPagerAdapter(photoShopBean));
        mViewPager.setCurrentItem(0);
        tv_picCurrent.setText("1");
        tv_picSum.setText("/" + mViewPager.getAdapter().getCount());
        requestGetExifByCode(photoShopBean.PCode);
    }

    private void setListener() {
        ibn_back.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        btn_gz.setOnClickListener(this);
        layout_picDetail.setOnClickListener(this);
        layout_picBuy.setOnClickListener(this);
        layout_picShare.setOnClickListener(this);
        mViewPager.setOnSingleTapListener(this);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                tv_picCurrent.setText(arg0 + 1 + "");
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.ibn_back:
                finish();
                break;
            case R.id.iv_head:
                it = new Intent(PictureDetailSingleActivity.this, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, photoShopBean.UCode);
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, photoShopBean.NickName);
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, photoShopBean.UserHead);
                startActivity(it);
                break;
            case R.id.btn_gz:
                requestAttentionOption();
                break;
            case R.id.layout_picDetail:
                if (scrollView_picInfo.getVisibility() == View.VISIBLE) {
                    scrollView_picInfo.setVisibility(View.GONE);
                } else {
                    scrollView_picInfo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.layout_picBuy:
                requestGetCoinAndFee();
                break;
            case R.id.layout_picShare:
                ShareBean sharebean = new ShareBean("图片分享自微摄(http://weishoot.com)", "点击查看更多详情",
                        photoShopBean.SmallPath, "http://weishoot.com/SharePage?u="
                                + photoShopBean.UCode + "&t=");
                showShareWindow.setShareData(sharebean);
                if (showShareWindow.isShowing()) {
                    showShareWindow.dismiss();
                } else {
                    showShareWindow.showAtLocation(layoutForShare, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSingleTap() {
        // 设置标题以及购买隐藏
        if (layout_top.getVisibility() == View.VISIBLE) {
            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(300);
            layout_top.startAnimation(animation);
            layout_bottom.startAnimation(animation);
            layout_top.setVisibility(View.GONE);
            layout_bottom.setVisibility(View.GONE);
        } else {
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(300);
            layout_top.startAnimation(animation);
            layout_bottom.startAnimation(animation);
            layout_top.setVisibility(View.VISIBLE);
            layout_bottom.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 版本dialog
     * 
     * @param content
     * @param url
     */
    private void showVersionUpdateDialog(String remanent_cion, final String image_cion) {

        DialogViews_typeAsk tDialog = new DialogViews_typeAsk(PictureDetailSingleActivity.this,
                true, new DialogViews_ask() {

                    @Override
                    public void doOk() {
                        String picName = photoShopBean.ImgName;
                        String a[] = picName.split("/");
                        requestBuyPhoto(a[a.length - 1], photoShopBean.UCode);
                    }

                    @Override
                    public void doCancle() {
                    }

                    @Override
                    public void doPay() {
                        Intent it = new Intent(PictureDetailSingleActivity.this,
                                ChongzhiActivity.class);
                        startActivity(it);
                    }
                });
        tDialog.setCancelable(false);
        tDialog.setImageTubei(image_cion);
        tDialog.setRemanenTubeit(remanent_cion);
        int cion = Integer.parseInt(image_cion);
        int cion_remanent = Integer.parseInt(remanent_cion);
        if (cion <= cion_remanent) {
            tDialog.setIsPayLayout(false);
        } else {
            tDialog.setIsPayLayout(true);
        }
        tDialog.show();
    }

    /**
     * 根据图片编号获取图片扩展信息
     */
    private void requestGetExifByCode(String PCode) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("PCode", PCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetExifByCode, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        //
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        PictureDetailBean picDetail = new Gson().fromJson(temp,
                                new TypeToken<PictureDetailBean>() {
                                }.getType());
                        if (picDetail != null && picDetail.result != null) {
                            if ("200".equals(picDetail.result.ResultCode) && picDetail.data != null) {
                                tv01.setText(picDetail.data.createDate);
                                tv02.setText(picDetail.data.files);
                                tv03.setText(picDetail.data.equipment);
                                tv04.setText(picDetail.data.pattern);
                                tv05.setText(picDetail.data.exposure);
                                tv06.setText(picDetail.data.focalLength);
                                tv07.setText(picDetail.data.focalPlane);
                                tv08.setText(picDetail.data.colour);
                                tv09.setText(picDetail.data.sceneType);
                                tv10.setText(picDetail.data.flash);
                                tv11.setText(picDetail.data.ver);
                            } else {
                                //
                            }
                        } else {
                            //
                        }
                    }
                });

    }

    private void requestAttentionOption() {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("FCode", photoShopBean.UCode);
        params.addBodyParameter("PCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AttentionOption, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(PictureDetailSingleActivity.this,
                                PictureDetailSingleActivity.this.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        dialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {

                                    WeiShootToast.makeText(PictureDetailSingleActivity.this,
                                            "关注成功", WeiShootToast.LENGTH_SHORT).show();
                                    btn_gz.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(
                                    PictureDetailSingleActivity.this,
                                    PictureDetailSingleActivity.this
                                            .getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 获取用户的图贝和单张图片的费用
     */
    private void requestGetCoinAndFee() {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", SharePreManager.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetCoinAndFee, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(PictureDetailSingleActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        dialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        UserCionBean userBean = new UserCionBean();
                        userBean = userBean.toJson(temp);
                        if (userBean != null && userBean.resultCode != null) {
                            if ("200".equals(userBean.resultCode)) {
                                showVersionUpdateDialog(userBean.Coin, userBean.Fee);
                            } else {
                                WeiShootToast.makeErrorText(PictureDetailSingleActivity.this,
                                        getString(R.string.http_timeout),
                                        WeiShootToast.LENGTH_SHORT).show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(PictureDetailSingleActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    /**
     * 购买图片
     */
    private void requestBuyPhoto(String photoMsg, String owner) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", SharePreManager.getInstance(this).getUserUCode());
        params.addBodyParameter("photoMsg", photoMsg);
        params.addBodyParameter("owner", owner);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.BuyPhoto, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                dialog.dismiss();
                WeiShootToast.makeErrorText(PictureDetailSingleActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                dialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                BuyImageBean userBean = new BuyImageBean();
                userBean = userBean.toJson(temp);
                if (userBean != null && userBean.resultCode != null) {
                    if ("200".equals(userBean.resultCode)) {
                        // 成功购买，通过picAddress字段返回图片地址进行下载
                        WeiShootToast.makeText(PictureDetailSingleActivity.this, "购买成功",
                                WeiShootToast.LENGTH_SHORT).show();
                        downloadImage(userBean.picAddress);
                    } else if ("-10".equals(userBean.resultCode)) {
                        // 代表已经购买过的，通过picAddress字段返回图片地址进行下载
                        WeiShootToast.makeText(PictureDetailSingleActivity.this, "已购买",
                                WeiShootToast.LENGTH_SHORT).show();
                        downloadImage(userBean.picAddress);
                    } else {
                        WeiShootToast.makeErrorText(PictureDetailSingleActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    WeiShootToast.makeErrorText(PictureDetailSingleActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void downloadImage(String url) {
        HttpUtils http = new HttpUtils();
        HttpHandler handler = http.download(url, Environment.getExternalStorageDirectory()
                + "/weishoot/image/" + "qr_" + System.currentTimeMillis() + ".jpg", true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        dialog.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        // testTextView.setText("downloaded:" +
                        // responseInfo.result.getPath());
                        dialog.dismiss();
                        WeiShootToast.makeText(PictureDetailSingleActivity.this, "下载图片成功",
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // testTextView.setText(msg);
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(PictureDetailSingleActivity.this, "下载图片失败",
                                WeiShootToast.LENGTH_SHORT).show();
                    }
                });

    }
}
