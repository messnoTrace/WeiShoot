
package com.NationalPhotograpy.weishoot.activity.registered;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.changepassword.FindBackPasswordActivity;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.ClickUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class LoginActivity extends BaseActivity implements OnClickListener, PlatformActionListener,
        Callback {
    public ImageButton ibtn_registered_back;

    public LinearLayout layout_QQ, layout_weibo, layout_wechat, layout;

    public EditText edt_username, edt_password;

    public Button btn_login, btn_registered;

    public TextView tv_login_problem;

    public static final String TAG = "LoginActivity";

    public static final int WHAT_QQ = 1;

    public static final int WHAT_WEIBO = 2;

    public static final int WHAT_WECHAT = 3;

    private Handler handler = new Handler(this);

    private ProgressiveDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        ShareSDK.initSDK(this);
        initView();
        setListener();

    }

    @Override
    protected void onDestroy() {
//        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    private void initView() {
        dialog = new ProgressiveDialog(this);
        ibtn_registered_back = (ImageButton) findViewById(R.id.ibtn_registered_back);
        layout_QQ = (LinearLayout) findViewById(R.id.layout_QQ);
        layout_weibo = (LinearLayout) findViewById(R.id.layout_weibo);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_wechat = (LinearLayout) findViewById(R.id.layout_wechat);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        tv_login_problem = (TextView) findViewById(R.id.tv_login_problem);
        // edt_username.setText("熊猫老四");
        // edt_password.setText("123456");

        switch ((int) (Math.random() * 3 + 1)) {
            case 1:
                layout.setBackgroundResource(R.drawable.spl1);
                break;
            case 2:
                layout.setBackgroundResource(R.drawable.spl2);
                break;
            case 3:
                layout.setBackgroundResource(R.drawable.spl3);
                break;

            default:
                break;
        }
    }

    private void setListener() {
        ibtn_registered_back.setOnClickListener(this);
        layout_QQ.setOnClickListener(this);
        layout_weibo.setOnClickListener(this);
        layout_wechat.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_registered.setOnClickListener(this);
        tv_login_problem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        Intent it;
        switch (v.getId()) {
            case R.id.ibtn_registered_back:
                finish();
                break;
            case R.id.layout_QQ:
                // ToDo QQ登录
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);

                break;
            case R.id.layout_weibo:
                // ToDo weibo登录
                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sinaWeibo);
                break;
            case R.id.layout_wechat:
                // ToDo wechat登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.btn_login:
                String user = edt_username.getText().toString();
                String password = edt_password.getText().toString();

                if (TextUtils.isEmpty(user)) {
                    WeiShootToast.makeErrorText(this, "用户名不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    WeiShootToast.makeErrorText(this, "密码不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }

                requestLogin(user, password);

                break;
            case R.id.btn_registered:
                it = new Intent(this, RegisteredActivity.class);
                startActivity(it);
                break;
            case R.id.tv_login_problem:
                // ToDo登入遇到问题
                it = new Intent(this, FindBackPasswordActivity.class);
                startActivity(it);
                break;

            default:
                break;
        }
    }

    private void authorize(Platform plat) {
        if (plat.isValid()) {
            plat.removeAccount();
            String userId = plat.getDb().getUserId();
            if (userId != null) {

            }
        }
        plat.setPlatformActionListener(this);
        // 关闭SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);

    }

    @Override
    public void onCancel(Platform arg0, int arg1) {

    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {

        Message msg = Message.obtain();

        if (QQ.NAME.equals(arg0.getName())) {

            msg.what = WHAT_QQ;
        } else if (Wechat.NAME.equals(arg0.getName())) {

            msg.what = WHAT_WECHAT;
        } else if (SinaWeibo.NAME.equals(arg0.getName())) {

            msg.what = WHAT_WEIBO;
        }

        msg.obj = arg0;
        handler.sendMessage(msg);

    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {

    }

    @Override
    public boolean handleMessage(Message msg) {

        Platform plat = (Platform) msg.obj;
        requestChkThirdUCode(plat, msg.what);

        return true;
    }

    private void requestLogin(final String user, final String password) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("loginName", user);
        params.addBodyParameter("Passwords", password);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.CheckLoin, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                dialog.dismiss();
                WeiShootToast.makeErrorText(LoginActivity.this, getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                dialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                Log.v(TAG, TAG + temp);
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {
                            StaticInfo.OnceFlag = false;
                            StaticInfo.isLoadWebView = true;
                            WeiShootToast.makeText(LoginActivity.this, "登录成功",
                                    WeiShootToast.LENGTH_SHORT).show();
                            JSONObject object = jsonObject.getJSONObject("data");

                            UserInfo.getInstance(LoginActivity.this).clearUserInfo();
                            UserInfo.getInstance(LoginActivity.this).setUserLoginStatus(true);
                            UserInfo.getInstance(LoginActivity.this).setUserUCode(
                                    object.optString("UCode"));
                            UserInfo.getInstance(LoginActivity.this).setUserHead(
                                    object.optString("UserHead"));
                            UserInfo.getInstance(LoginActivity.this).setUserNickName(
                                    object.optString("NickName"));
                            UserInfo.getInstance(LoginActivity.this).setUserSex(
                                    object.optString("Sex"));
                            UserInfo.getInstance(LoginActivity.this).setUserIntroduction(
                                    object.optString("Introduction"));
                            UserInfo.getInstance(LoginActivity.this).setUserTelephone(
                                    object.optString("Telephone"));
                            UserInfo.getInstance(LoginActivity.this).setUserPassword(password);
                            UserInfo.getInstance(LoginActivity.this).setUserLoginName(user);
                            finish();
                        } else {
                            WeiShootToast.makeErrorText(LoginActivity.this,
                                    jObject.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeiShootToast.makeErrorText(LoginActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestChkThirdUCode(final Platform plat, final int what) {
        dialog.show();

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("ThirdId", plat.getDb().getUserId());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.ChkThirdUCode, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(LoginActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        dialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        Log.v(TAG, TAG + temp);
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject jsonObject = new JSONObject(temp);
                                JSONObject jObject = jsonObject.getJSONObject("result");
                                if ("200".equals(jObject.optString("ResultCode"))) {

                                    requestGetUInfo(jObject.optString("ResultMsg"));
                                } else {
                                    Intent it = new Intent(LoginActivity.this,
                                            ThirdLoginActivity.class);
                                    it.putExtra("id", plat.getDb().getUserId());
                                    it.putExtra("name", plat.getDb().getUserName());
                                    it.putExtra("icon", plat.getDb().getUserIcon());
                                    it.putExtra("accesscode", plat.getDb().getToken());

                                    switch (what) {
                                        case WHAT_QQ:
                                            it.putExtra("flag", "qq");
                                            break;
                                        case WHAT_WECHAT:
                                            it.putExtra("flag", "wechat");
                                            break;
                                        case WHAT_WEIBO:
                                            it.putExtra("flag", "weibo");
                                            break;

                                        default:
                                            break;
                                    }

                                    startActivity(it);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            WeiShootToast.makeErrorText(LoginActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
    }

    /**
     * 获取个人信息
     */
    private void requestGetUInfo(String uCode) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("currentUCode", uCode);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetUserInfo, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                dialog.dismiss();
                WeiShootToast.makeErrorText(LoginActivity.this, getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                dialog.dismiss();
                UserInfosBean uInfo = new Gson().fromJson(temp, new TypeToken<UserInfosBean>() {
                }.getType());
                if (uInfo != null && uInfo.result != null) {
                    if ("200".equals(uInfo.result.ResultCode) && uInfo.data != null) {
                        StaticInfo.OnceFlag = false;
                        UserInfo.getInstance(LoginActivity.this).clearUserInfo();
                        UserInfo.getInstance(LoginActivity.this).setUserLoginStatus(true);
                        UserInfo.getInstance(LoginActivity.this).setUserUCode(uInfo.data.UCode);
                        UserInfo.getInstance(LoginActivity.this).setUserHead(uInfo.data.UserHead);
                        UserInfo.getInstance(LoginActivity.this).setUserNickName(
                                uInfo.data.NickName);
                        UserInfo.getInstance(LoginActivity.this).setUserSex(uInfo.data.Sex);
                        UserInfo.getInstance(LoginActivity.this).setUserIntroduction(
                                uInfo.data.Introduction);
                        UserInfo.getInstance(LoginActivity.this).setUserTelephone(
                                uInfo.data.Telephone);
                        // todo解密password、
                        requestDecryptPassword(uInfo.data.Passwords);
                        finish();

                    } else {
                        WeiShootToast.makeErrorText(LoginActivity.this, uInfo.result.ResultMsg,
                                WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(LoginActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void requestDecryptPassword(String password) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("Password", password);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.DecryptPassword, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(LoginActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject jsonObject = new JSONObject(temp);
                                if ("200".equals(jsonObject.getJSONObject("result").optString(
                                        "ResultCode"))) {
                                    UserInfo.getInstance(LoginActivity.this).setUserPassword(
                                            jsonObject.getJSONObject("data").optString("Password"));

                                } else {
                                    WeiShootToast.makeErrorText(
                                            LoginActivity.this,
                                            jsonObject.getJSONObject("result").optString(
                                                    "ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                            .show();
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(LoginActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }
}
