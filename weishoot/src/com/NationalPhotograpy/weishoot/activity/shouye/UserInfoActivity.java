
package com.NationalPhotograpy.weishoot.activity.shouye;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.changepassword.ChangeBindPhoneActivity;
import com.NationalPhotograpy.weishoot.activity.changepassword.ChangePasswordActivity;
import com.NationalPhotograpy.weishoot.activity.changepassword.LikeStyleActivity;
import com.NationalPhotograpy.weishoot.activity.changepassword.MyQRcodeActivity;
import com.NationalPhotograpy.weishoot.activity.photograph.SelectPicActivity;
import com.NationalPhotograpy.weishoot.bean.ProvinceBean;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean;
import com.NationalPhotograpy.weishoot.bean.UserTagBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.DbCitys;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.DialogUtil;
import com.NationalPhotograpy.weishoot.view.EditUserInfoDialog;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.UserInfoListViewDialog;
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
 * 我的资料
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener, Callback {

    private LinearLayout layout_back;

    private RelativeLayout rl_changePassword, rl_myQrCode, rl_change_bindPhone, rl_iv_head,
            rl_nickname, rl_gxqm, rl_true_name, rl_sex, rl_oc, rl_city, rl_like, rl_cover;

    private ProgressiveDialog progressDialog;

    private ImageView iv_head, iv_sex, iv_cover;

    private TextView tv_nickname, tv_gxqm, tv_true_name, tv_phone, tv_birthday, tv_oc, tv_city,
            tv_like, tv_name;

    private String sex = "";

    private String phone = "";

    private String city = "";

    private List<ProvinceBean> provinceBeans = new ArrayList<ProvinceBean>();

    private Handler hanlder = new Handler(this);

    private boolean headFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_userinfo);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        rl_changePassword = (RelativeLayout) findViewById(R.id.rl_changePassword);
        rl_myQrCode = (RelativeLayout) findViewById(R.id.rl_myQrCode);
        rl_change_bindPhone = (RelativeLayout) findViewById(R.id.rl_change_bindPhone);
        rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
        rl_oc = (RelativeLayout) findViewById(R.id.rl_oc);
        rl_city = (RelativeLayout) findViewById(R.id.rl_city);
        rl_iv_head = (RelativeLayout) findViewById(R.id.rl_iv_head);
        rl_gxqm = (RelativeLayout) findViewById(R.id.rl_gxqm);
        rl_cover = (RelativeLayout) findViewById(R.id.rl_cover);
        rl_true_name = (RelativeLayout) findViewById(R.id.rl_true_name);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_like = (RelativeLayout) findViewById(R.id.rl_like);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_oc = (TextView) findViewById(R.id.tv_oc);
        tv_gxqm = (TextView) findViewById(R.id.tv_gxqm);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_true_name = (TextView) findViewById(R.id.tv_true_name);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_like = (TextView) findViewById(R.id.tv_like);
        iv_cover = (ImageView) findViewById(R.id.iv_cover);

    }

    private void initData() {

        // 先从本地读取信息
        UserInfosBean uInfo = new Gson().fromJson(SharePreManager.getInstance(this).getUserInfo(),
                new TypeToken<UserInfosBean>() {
                }.getType());
        if (uInfo != null) {
            upDateUi(uInfo);
        }
        requestGetUsersByCode();
        requestGetOccupations();
        readyCity();
        requestGeTagsByUCode();
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        rl_changePassword.setOnClickListener(this);
        rl_myQrCode.setOnClickListener(this);
        rl_change_bindPhone.setOnClickListener(this);
        rl_iv_head.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_gxqm.setOnClickListener(this);
        rl_true_name.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        rl_oc.setOnClickListener(this);
        rl_city.setOnClickListener(this);
        rl_like.setOnClickListener(this);
        rl_cover.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (headFlag) {
                setResult(Constant.RESULT_HEAD);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.layout_back:
                if (headFlag) {
                    setResult(Constant.RESULT_HEAD);
                }
                finish();
                break;
            case R.id.rl_changePassword:
                it = new Intent(this, ChangePasswordActivity.class);
                startActivity(it);
                break;
            case R.id.rl_myQrCode:
                it = new Intent(this, MyQRcodeActivity.class);
                startActivity(it);
                break;
            case R.id.rl_change_bindPhone:
                it = new Intent(this, ChangeBindPhoneActivity.class);
                it.putExtra("phone", phone);
                startActivityForResult(it, 1);
                break;
            case R.id.rl_iv_head:
                it = new Intent(UserInfoActivity.this, SelectPicActivity.class);
                it.putExtra("type", "head");
                startActivityForResult(it, 1);
                break;
            case R.id.rl_cover:
                it = new Intent(UserInfoActivity.this, SelectPicActivity.class);
                it.putExtra("type", "TopCover");
                startActivityForResult(it, 2);
                break;

            case R.id.rl_nickname:
                final EditUserInfoDialog editDialog = new EditUserInfoDialog(this);
                editDialog.setTitle("请输入昵称");
                editDialog.setEdHint("");
                editDialog.show();
                editDialog.setOnCancelListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });
                editDialog.setOnOkListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String nickName = editDialog.getEtDialog().getText().toString();
                        if (!TextUtils.isEmpty(nickName)) {
                            tv_nickname.setText(nickName);
                            tv_name.setText(nickName);
                            requestUpDataUserInfo("NickName", nickName);
                        }
                        editDialog.dismiss();
                    }
                });

                break;

            case R.id.rl_gxqm:
                it = new Intent(UserInfoActivity.this, UserAdviceActivity.class);
                it.putExtra("flag", "gxqm");
                startActivityForResult(it, 1);
                break;
            case R.id.rl_true_name:
                final EditUserInfoDialog nameDialog = new EditUserInfoDialog(this);
                nameDialog.setTitle("请输入真实姓名");
                nameDialog.setEdHint("");
                nameDialog.show();
                nameDialog.setOnCancelListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        nameDialog.dismiss();
                    }
                });
                nameDialog.setOnOkListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String trueName = nameDialog.getEtDialog().getText().toString();
                        if (!TextUtils.isEmpty(trueName)) {
                            tv_true_name.setText(trueName);
                            requestUpDataUserInfo("TrueName", trueName);
                        }
                        nameDialog.dismiss();
                    }
                });
                break;

            case R.id.rl_sex:
                final EditUserInfoDialog sexDialog = new EditUserInfoDialog(this);
                sexDialog.getDialogBtnDividerMargin().setVisibility(View.GONE);
                List<String> list = new ArrayList<String>();
                list.add("男");
                list.add("女");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        R.layout.item_sex_selector, list);
                sexDialog.getDialogListView().setAdapter(adapter);
                sexDialog.show();
                sexDialog.setOnSingleCancelListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        sexDialog.dismiss();
                    }
                });
                sexDialog.getDialogListView().setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            sex = "男";
                            iv_sex.setBackgroundResource(R.drawable.sex_man);
                            sexDialog.dismiss();
                        } else if (position == 1) {
                            sex = "女";
                            iv_sex.setBackgroundResource(R.drawable.sex_women);
                            sexDialog.dismiss();
                        }

                        requestUpDataUserInfo("Sex", sex);
                    }
                });

                break;
            case R.id.tv_birthday:
                new DialogUtil().showCalenderDialog(UserInfoActivity.this, tv_birthday);
                break;

            case R.id.rl_oc:
                if (TextUtils.isEmpty(SharePreManager.getInstance(UserInfoActivity.this)
                        .getOccupations())) {
                    requestGetOccupations();
                    return;
                }
                new UserInfoListViewDialog(UserInfoActivity.this, "oc", SharePreManager
                        .getInstance(UserInfoActivity.this).getOccupations(), tv_oc, null);
                break;
            case R.id.rl_city:
                new UserInfoListViewDialog(UserInfoActivity.this, "city", "", tv_city,
                        provinceBeans);
                break;
            case R.id.rl_like:
                it = new Intent(UserInfoActivity.this, LikeStyleActivity.class);
                it.putExtra("ActivityType", "UPLOAD");
                startActivityForResult(it, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.RESULT_HEAD && requestCode == 1) {
            headFlag = true;
            String headPath = data.getStringExtra("picUri");
            ImageLoader.getInstance().displayImage(
                    "file://" + headPath,
                    iv_head,
                    new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                            .displayer(new RoundedBitmapDisplayer(200))
                            .showImageForEmptyUri(R.drawable.default_head).build());
        }
        if (resultCode == Constant.RESULT_TOPCOVER && requestCode == 2) {
            String headPath = data.getStringExtra("picUri");
            ImageLoader.getInstance().displayImage(
                    "file://" + headPath,
                    iv_cover,
                    new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                            .displayer(new RoundedBitmapDisplayer(0))
                            .showImageForEmptyUri(R.drawable.iv01).build());
        }

        if (resultCode == Constant.RESULT_QM) {
            String content = data.getStringExtra("content");
            if (!TextUtils.isEmpty(content)) {
                tv_gxqm.setText(content);
                requestUpDataUserInfo("Introduction", content);
            }

        }

        if (resultCode == Constant.RESULT_PHONE) {
            String phone = data.getStringExtra("phone");
            if (!TextUtils.isEmpty(phone)) {
                tv_phone.setText(phone);
            }

        }

        if (resultCode == Constant.RESULT_LABEL_UPLOAD) {
            tv_like.setText("");
            ArrayList<String> labelList = data.getStringArrayListExtra("label");
            for (int i = 0; i < labelList.size(); i++) {
                if (i == labelList.size() - 1) {
                    tv_like.append(labelList.get(i));
                } else {
                    tv_like.append(labelList.get(i) + "，");
                }

            }

            requestAddUserTags(labelList);

        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        setCityTv();
        return false;
    }

    private void setCityTv() {
        for (int i = 0; i < provinceBeans.size(); i++) {
            for (int j = 0; j < provinceBeans.get(i).CityBean.size(); j++) {
                for (int z = 0; z < provinceBeans.get(i).CityBean.get(j).CountyBean.size(); z++) {
                    if (city.equals(provinceBeans.get(i).CityBean.get(j).CountyBean.get(z).id)) {
                        tv_city.setText(provinceBeans.get(i).CityBean.get(j).CountyBean.get(z).name);
                    }
                }
            }
        }
    }

    private void readyCity() {
        // TODO Auto-generated method stub
        new Thread() {
            public void run() {
                provinceBeans.addAll(DbCitys.getCitys());
                hanlder.sendEmptyMessage(0);
            }
        }.start();
    }

    private void upDateUi(UserInfosBean uInfo) {
        ImageLoader.getInstance()
                .displayImage(
                        uInfo.data.UserHead
                                + "?"
                                + SharePreManager.getInstance(UserInfoActivity.this)
                                        .getUserHeadTimestamp(),
                        iv_head,
                        new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                                .displayer(new RoundedBitmapDisplayer(100))
                                .showImageForEmptyUri(R.drawable.default_head).build());
        ImageLoader.getInstance()
                .displayImage(
                        uInfo.data.Cover
                                + "?"
                                + SharePreManager.getInstance(UserInfoActivity.this)
                                        .getUserHeadTimestamp(),
                        iv_cover,
                        new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                                .displayer(new RoundedBitmapDisplayer(0))
                                .showImageForEmptyUri(R.drawable.default_head).build());

        if (!TextUtils.isEmpty(uInfo.data.NickName)) {
            tv_nickname.setText(uInfo.data.NickName);
            tv_name.setText(uInfo.data.NickName);
        }
        if (!TextUtils.isEmpty(uInfo.data.Introduction)) {
            tv_gxqm.setText(uInfo.data.Introduction);
        }
        if (!TextUtils.isEmpty(uInfo.data.TrueName)) {
            tv_true_name.setText(uInfo.data.TrueName);
        }

        if ("男".equals(uInfo.data.Sex)) {
            iv_sex.setBackgroundResource(R.drawable.sex_man);
        } else if ("女".equals(uInfo.data.Sex)) {
            iv_sex.setBackgroundResource(R.drawable.sex_women);
        }

        if (!TextUtils.isEmpty(uInfo.data.Telephone)) {
            phone = uInfo.data.Telephone;
            tv_phone.setText(uInfo.data.Telephone);
        }
        if (!TextUtils.isEmpty(uInfo.data.Birthday)) {
            tv_birthday.setText(uInfo.data.Birthday);
        }
        if (!TextUtils.isEmpty(uInfo.data.OcName)) {
            tv_oc.setText(uInfo.data.OcName);
        }
        if (!TextUtils.isEmpty(uInfo.data.City)) {
            tv_city.setText(uInfo.data.City);
        }
        if (!TextUtils.isEmpty(uInfo.data.City)) {
            city = uInfo.data.City;
        }

    }

    private void requestGetUsersByCode() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetUserInfo, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                progressDialog.dismiss();
                WeiShootToast.makeErrorText(UserInfoActivity.this,
                        UserInfoActivity.this.getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                progressDialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                UserInfosBean uInfo = new Gson().fromJson(temp, new TypeToken<UserInfosBean>() {
                }.getType());
                if (uInfo != null && uInfo.result != null) {
                    if ("200".equals(uInfo.result.ResultCode) && uInfo.data != null) {
                        SharePreManager.getInstance(UserInfoActivity.this).setUserInfo(temp);
                        upDateUi(uInfo);

                    } else {
                        WeiShootToast.makeErrorText(UserInfoActivity.this, uInfo.result.ResultMsg,
                                WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(UserInfoActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void requestUpDataUserInfo(final String key, final String value) {

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("updateField", key);
        params.addBodyParameter(key, value);

        httpUtils.send(HttpMethod.POST, HttpUrl.UpdateUser, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                WeiShootToast.makeErrorText(UserInfoActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {

                            if ("NickName".equals(key)) {
                                UserInfo.getInstance(UserInfoActivity.this).setUserNickName(value);
                            } else if ("Introduction".equals(key)) {
                                UserInfo.getInstance(UserInfoActivity.this).setUserIntroduction(
                                        value);
                            } else if ("Sex".equals(key)) {
                                UserInfo.getInstance(UserInfoActivity.this).setUserSex(value);
                            }

                        } else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void requestUpDataUserInfoExpand(final String key, final String value) {

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("updateField", key);
        params.addBodyParameter(key, value);

        httpUtils.send(HttpMethod.POST, HttpUrl.UpdateUserInfo, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        WeiShootToast.makeErrorText(UserInfoActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject jsonObject = new JSONObject(temp);
                                JSONObject jObject = jsonObject.getJSONObject("result");
                                if ("200".equals(jObject.optString("ResultCode"))) {

                                } else {

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void requestGetOccupations() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetOccupations, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        Log.v("TAG", "sdddd" + temp);
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject jsonObject = new JSONObject(temp);
                                JSONObject jObject = jsonObject.getJSONObject("result");
                                if ("200".equals(jObject.optString("ResultCode"))) {

                                    SharePreManager.getInstance(UserInfoActivity.this)
                                            .setOccupations(temp);

                                } else {

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void requestGeTagsByUCode() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GeTagsByUCode, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        UserTagBean uTag = new Gson().fromJson(temp, new TypeToken<UserTagBean>() {
                        }.getType());
                        if (uTag != null && uTag.result != null) {
                            if ("200".equals(uTag.result.ResultCode) && uTag.data != null) {
                                for (int i = 0; i < uTag.data.size(); i++) {
                                    if (i == uTag.data.size() - 1) {
                                        tv_like.append(uTag.data.get(i).TName);
                                    } else {
                                        tv_like.append(uTag.data.get(i).TName + "，");
                                    }

                                }
                            } else {
                                // WeiShootToast.makeErrorText(UserInfoActivity.this,
                                // uTag.result.ResultMsg,
                                // WeiShootToast.LENGTH_SHORT).show();
                            }
                        } else {
                            // WeiShootToast.makeErrorText(UserInfoActivity.this,
                            // getString(R.string.http_timeout),
                            // WeiShootToast.LENGTH_SHORT)
                            // .show();
                        }
                    }
                });
    }

    public void requestAddUserTags(ArrayList<String> labelList) {
        progressDialog.show();
        UserTagBean uTag = new Gson().fromJson(SharePreManager.getInstance(UserInfoActivity.this)
                .getAllTag(), new TypeToken<UserTagBean>() {
        }.getType());
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < uTag.data.size(); i++) {
            for (int j = 0; j < labelList.size(); j++) {
                if (labelList.get(j).equals(uTag.data.get(i).TName)) {
                    buffer.append(uTag.data.get(i).TCode + ",");
                }
            }

        }

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("tags",
                buffer.toString().substring(0, buffer.toString().length() - 1));
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AddUserTags, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                progressDialog.dismiss();
                WeiShootToast.makeErrorText(UserInfoActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                progressDialog.dismiss();
                String temp = (String) objectResponseInfo.result;

            }
        });
    }
}
