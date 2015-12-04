
package com.NationalPhotograpy.weishoot.activity.photograph;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.MainActivity;
import com.NationalPhotograpy.weishoot.activity.changepassword.LikeStyleActivity;
import com.NationalPhotograpy.weishoot.bean.PicSortBean;
import com.NationalPhotograpy.weishoot.bean.PictureSortBean;
import com.NationalPhotograpy.weishoot.bean.ReqUploadBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.NationalPhotograpy.weishoot.utils.ImageLoader;
import com.NationalPhotograpy.weishoot.utils.ImageLoader.Type;
import com.NationalPhotograpy.weishoot.utils.Methods;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.baidulocation.SelectAddressActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 上传图片
 */
public class UploadWeiShootActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back, layout_label;

    private EditText et_content;

    private HorizontalScrollView scroll_pattern;

    private ImageView iv_location;

    private RadioGroup group_picPattern;

    private RelativeLayout rlayout_picPattern, layoutPicture, rl_label;

    private TextView tv_picCount, tv_location, tv_label, tv_text_location;

    private Button btn_xieyi, btn_sale, btn_submit;

    private List<String> uploadPic;

    private StringBuffer reqLabel = new StringBuffer("");

    private PicSortBean reqSCodeBean;

    private String reqAddressName = "", reqAddressNum = "";

    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_upload_weishoot);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_label = (LinearLayout) findViewById(R.id.layout_label);
        scroll_pattern = (HorizontalScrollView) findViewById(R.id.scroll_pattern);
        rlayout_picPattern = (RelativeLayout) findViewById(R.id.rlayout_picPattern);
        rl_label = (RelativeLayout) findViewById(R.id.rl_label);
        layoutPicture = (RelativeLayout) findViewById(R.id.layoutPicture);
        iv_location = (ImageView) findViewById(R.id.iv_location);
        group_picPattern = (RadioGroup) findViewById(R.id.group_picPattern);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_picCount = (TextView) findViewById(R.id.tv_picCount);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_text_location = (TextView) findViewById(R.id.tv_text_location);
        tv_label = (TextView) findViewById(R.id.tv_label);
        btn_xieyi = (Button) findViewById(R.id.btn_xieyi);
        btn_sale = (Button) findViewById(R.id.btn_sale);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        uploadPic = new ArrayList<String>();

        btn_sale.setSelected(true);
    }

    private void initData() {
        onRefresh(getIntent());
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        rl_label.setOnClickListener(this);
        iv_location.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        btn_sale.setOnClickListener(this);
        btn_xieyi.setOnClickListener(this);
        layoutPicture.setOnClickListener(this);
        layout_label.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layoutPicture:
                finish();
                break;
            case R.id.btn_submit:
                if (reqSCodeBean == null) {
                    WeiShootToast.makeErrorText(UploadWeiShootActivity.this, "请为图片选择一种布局",
                            WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                String[] tempArr = reqLabel.toString().split(",");
                if (tempArr.length < 1) {
                    WeiShootToast.makeErrorText(UploadWeiShootActivity.this, "至少选择1个标签",
                            WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                String str_IsSale = "1";
                if (btn_sale.isSelected()) {
                    str_IsSale = "1";
                } else {
                    str_IsSale = "0";
                }
                ReqUploadBean uploadBean = new ReqUploadBean("1", et_content.getText().toString()
                        .trim(), reqLabel.toString(), reqSCodeBean, reqAddressName, reqAddressNum,
                        str_IsSale, uploadPic, cid);

                it = new Intent(this, MainActivity.class);
                it.putExtra("Result", "RESULT_UPLOAD_HOME");
                it.putExtra("Request", "upload");
                it.putExtra("ReqUploadBean", uploadBean);
                startActivity(it);
                finish();
                break;
            case R.id.layout_label:
            case R.id.rl_label:
                it = new Intent(UploadWeiShootActivity.this, LikeStyleActivity.class);
                it.putExtra("ActivityType", "UPLOAD");
                startActivityForResult(it, 1);
                break;
            case R.id.iv_location:
                it = new Intent(UploadWeiShootActivity.this, SelectAddressActivity.class);
                it.putExtra("ActivityType", "UPLOAD");
                startActivityForResult(it, 1);
                break;
            case R.id.tv_location:
                it = new Intent(UploadWeiShootActivity.this, SelectAddressActivity.class);
                it.putExtra("ActivityType", "UPLOAD");
                startActivityForResult(it, 1);
                break;
            case R.id.btn_sale:
                if (btn_sale.isSelected()) {
                    btn_sale.setSelected(false);
                } else {
                    btn_sale.setSelected(true);
                }
                break;
            case R.id.btn_xieyi:
                it = new Intent(UploadWeiShootActivity.this, WeiShootWebActiivty.class);
                it.putExtra("url", HttpUrl.AgreementSale);
                startActivity(it);
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        switch (resultCode) {
            case Constant.RESULT_LABEL_UPLOAD:

                ArrayList<String> labelList = it.getStringArrayListExtra("label");
                if (labelList.size() > 0) {
                    tv_label.setVisibility(View.GONE);
                } else {
                    tv_label.setVisibility(View.VISIBLE);
                }
                setLayoutLabel(labelList);
                for (int i = 0; i < labelList.size(); i++) {
                    reqLabel.append(labelList.get(i) + ",");
                }
                break;
            case Constant.RESULT_LOCATION_UPLOAD:
                reqAddressName = it.getStringExtra("addressName");
                reqAddressNum = it.getStringExtra("addressNum");
                tv_location.setText(reqAddressName);
                tv_text_location.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    private void setLayoutLabel(ArrayList<String> labelList) {
        layout_label.removeAllViews();
        for (int i = 0; i < labelList.size(); i++) {
            final TextView label = new TextView(this);
            LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    (int) DimensionPixelUtil.getDimensionPixelSize(1, 25, this));
            ps.rightMargin = Methods.dip2px(this, 2);
            ps.leftMargin = Methods.dip2px(this, 2);
            label.setLayoutParams(ps);
            label.setGravity(Gravity.CENTER);
            label.setSingleLine();
            label.setEllipsize(TextUtils.TruncateAt.END);
            label.setText(labelList.get(i));
            label.setTextSize(16);
            label.setTextColor(this.getResources().getColor(R.color.white));
            label.setPadding(Methods.dip2px(this, 8), 0, Methods.dip2px(this, 8), 0);
            label.setBackgroundColor(Color.parseColor("#8cc11f"));
            layout_label.addView(label);
        }

    }

    private void onRefresh(Intent it) {
        if (it.hasExtra("picUri")) {
            Uri picUri = it.getParcelableExtra("picUri");
            uploadPic.add(picUri.getPath());
        } else if (it.hasExtra("mSelectedImage")) {
            uploadPic.addAll(it.getStringArrayListExtra("mSelectedImage"));
        }
        cid = it.getStringExtra("cid");
        int picSize = 4;
        if (uploadPic.size() < picSize) {
            picSize = uploadPic.size();
        }
        for (int i = 0; i < picSize; i++) {
            findViewById(
                    getResources().getIdentifier("iv_pic" + i, "id",
                            "com.Dailyfood.meirishejian")).setVisibility(View.VISIBLE);
            ImageLoader.getInstance(3, Type.LIFO).loadImage(
                    uploadPic.get(i),
                    (ImageView) findViewById(getResources().getIdentifier("iv_pic" + i, "id",
                            "com.Dailyfood.meirishejian")));
        }
        tv_picCount.setText(uploadPic.size() + "");
        requestGetPicSort(uploadPic.size());
    }

    private void setLayoutPicSort(final List<PicSortBean> data) {
        if (data.size() == 1) {
            reqSCodeBean = data.get(0);
            return;
        }
        rlayout_picPattern.setVisibility(View.VISIBLE);
        group_picPattern.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            int radioButtonWidth = (int) DimensionPixelUtil.getDimensionPixelSize(1, 69, this);
            int radioButtonHeight = (int) DimensionPixelUtil.getDimensionPixelSize(1, 100, this);
            radioButton.setLayoutParams(new LayoutParams(radioButtonWidth, radioButtonHeight));
            int padding = (int) DimensionPixelUtil.getDimensionPixelSize(1, 2, this);
            radioButton.setPadding(padding / 2, 0, 0, padding / 2);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setBackgroundResource(getResources().getIdentifier(
                    "xx" + data.get(i).Sort.replace("-", "_"), "drawable",
                    "com.Dailyfood.meirishejian"));
            group_picPattern.addView(radioButton);
            if(i==0){
            	radioButton.setChecked(true);
            	//
            	  
            }
        }
        reqSCodeBean = data.get(0);

        group_picPattern.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        group.getChildAt(i).setBackgroundResource(
                                getResources().getIdentifier(
                                        "x" + data.get(i).Sort.replace("-", "_"), "drawable",
                                        "com.Dailyfood.meirishejian"));
                        int scrox = 72 * i - 152;
                        scroll_pattern.scrollTo(dip2px(scrox), 0);
                        reqSCodeBean = data.get(i);
                    } else {
                        group.getChildAt(i).setBackgroundResource(
                                getResources().getIdentifier(
                                        "xx" + data.get(i).Sort.replace("-", "_"), "drawable",
                                        "com.Dailyfood.meirishejian"));
                    }
                }
            }
        });
        group_picPattern.setVisibility(View.GONE);
        rlayout_picPattern.setVisibility(View.GONE);
    }

    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 请求布局
     */
    private void requestGetPicSort(int count) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("Num", count + "");
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetPicSort, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                WeiShootToast.makeErrorText(UploadWeiShootActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                PictureSortBean picSortBean = new Gson().fromJson(temp,
                        new TypeToken<PictureSortBean>() {
                        }.getType());
                if (picSortBean != null && picSortBean.result != null) {
                    if ("200".equals(picSortBean.result.ResultCode)) {
                        setLayoutPicSort(picSortBean.data);
                    } else {
                        WeiShootToast.makeErrorText(UploadWeiShootActivity.this,
                                picSortBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(UploadWeiShootActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
