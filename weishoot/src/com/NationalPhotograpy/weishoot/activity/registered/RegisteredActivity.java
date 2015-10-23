
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
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.ClickUtil;
import com.NationalPhotograpy.weishoot.utils.Methods;
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

/**
 * 注册页面
 */
public class RegisteredActivity extends BaseActivity implements OnClickListener, Callback,
        PlatformActionListener {
    public LinearLayout layout_QQ, layout_weibo, layout_wechat;

    public Button btn_registered;

    public EditText edt_phone, edt_setpassword, edt_setpassword_again, edt_verification_code;

    public TextView tv_get_verification_code, tv_title;

    public static final String TAG = "RegisteredActivity";

    private String phone;

    private String passwordAgain;

    private Handler handler = new Handler(this);

    private ProgressiveDialog dialog;

    public static final int WHAT_QQ = 1;

    public static final int WHAT_WEIBO = 2;

    public static final int WHAT_WECHAT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();
        initData();
        setListener();
        ShareSDK.initSDK(this);
    }

    private void initView() {
        layout_QQ = (LinearLayout) findViewById(R.id.layout_QQ);
        layout_weibo = (LinearLayout) findViewById(R.id.layout_weibo);
        layout_wechat = (LinearLayout) findViewById(R.id.layout_wechat);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_setpassword = (EditText) findViewById(R.id.edt_setpassword);
        edt_setpassword_again = (EditText) findViewById(R.id.edt_setpassword_again);
        edt_verification_code = (EditText) findViewById(R.id.edt_verification_code);
        tv_get_verification_code = (TextView) findViewById(R.id.tv_get_verification_code);
        tv_title = (TextView) findViewById(R.id.tv_title);
        dialog = new ProgressiveDialog(this);
    }

    private void initData() {
        SMSSDK.initSDK(this, Constant.SMS_APPKEY, Constant.SMS_APPSECRET);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.what = 200;
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }

    private void setListener() {
        layout_QQ.setOnClickListener(this);
        layout_weibo.setOnClickListener(this);
        layout_wechat.setOnClickListener(this);
        btn_registered.setOnClickListener(this);
        tv_get_verification_code.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        Intent it;
        switch (v.getId()) {
            case R.id.tv_title:
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
            case R.id.btn_registered:
                // ToDo发送注册

                phone = edt_phone.getText().toString().trim();
                String password = edt_setpassword.getText().toString();
                passwordAgain = edt_setpassword_again.getText().toString();
                String code = edt_verification_code.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    WeiShootToast.makeErrorText(this, "请输入手机号", WeiShootToast.LENGTH_SHORT).show();

                    return;
                }
                if (!Methods.validatePhone(phone)) {
                    WeiShootToast.makeErrorText(this, "手机号码输入有误", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    WeiShootToast.makeErrorText(this, "密码和确认密码不能为空", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (!passwordAgain.equals(password)) {
                    WeiShootToast.makeErrorText(this, "密码和确认密码不一致", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    WeiShootToast.makeErrorText(this, "验证码不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
//                if (totalTime == 60) {
//                    WeiShootToast.makeErrorText(this, "请获取验证码", WeiShootToast.LENGTH_SHORT).show();
//                    return;
//                }
//                SMSSDK.submitVerificationCode("86", phone, code);
                requestRegist(passwordAgain);
                dialog.show();

                break;
            case R.id.tv_get_verification_code:
                if ("获取验证码".equals(tv_get_verification_code.getText().toString())) {
                    phone = edt_phone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        WeiShootToast.makeErrorText(this, "请输入手机号", WeiShootToast.LENGTH_SHORT)
                                .show();
                        return;
                    }

                    requestChkTelephone(phone);

                }

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
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        ShareSDK.stopSDK(this);
    }

    int totalTime = 60;

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;
        Log.e("event", "event=" + event);
        if (what == 200) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                    requestRegist(passwordAgain);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    dialog.dismiss();
                    WeiShootToast.makeText(this, "验证码已经发送", WeiShootToast.LENGTH_SHORT).show();
                    Message message = Message.obtain();
                    message.what = 100;
                    handler.sendMessageDelayed(message, 0);

                }
            } else {
                WeiShootToast.makeText(this, "验证码输入有误", WeiShootToast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }

        if (what == 100) {
            if (totalTime >= 0) {
                tv_get_verification_code.setText(totalTime + "秒后重发");
                Message message = Message.obtain();
                message.what = 100;
                handler.sendMessageDelayed(message, 1000);
                totalTime--;
            } else {
                totalTime = 60;
                tv_get_verification_code.setText("获取验证码");

            }
        }
        if (msg.what == WHAT_QQ || msg.what == WHAT_WECHAT || msg.what == WHAT_WEIBO) {
            Platform plat = (Platform) msg.obj;
            requestChkThirdUCode(plat, msg.what);
        }

        return false;
    }

    private void requestChkThirdUCode(final Platform plat, final int what) {
        if (what != WHAT_QQ && what != WHAT_WECHAT && what != WHAT_WEIBO) {
            return;
        }

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
                        WeiShootToast.makeErrorText(RegisteredActivity.this,
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
                                    Intent it = new Intent(RegisteredActivity.this,
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
                            WeiShootToast.makeErrorText(RegisteredActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
    }

    public void requestRegist(String password) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("Telephone", phone);
        params.addBodyParameter("Passwords", password);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.UserRegist, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                dialog.dismiss();
                WeiShootToast.makeErrorText(RegisteredActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
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
                            JSONObject object = jsonObject.getJSONObject("data");

                            UserInfo.getInstance(RegisteredActivity.this).setUserUCode(
                                    object.optString("UCode"));
                            UserInfo.getInstance(RegisteredActivity.this).setUserNickName(
                                    object.optString("NickName"));
                            Intent it = new Intent(RegisteredActivity.this,
                                    PerfectPersonalDataActivity.class);
                            it.putExtra(PerfectPersonalDataActivity.ARG_UCODE,
                                    object.optString("UCode"));
                            it.putExtra(PerfectPersonalDataActivity.ARG_NICKNAME,
                                    object.optString("NickName"));

                            startActivity(it);

                            finish();
                        } else {
                            WeiShootToast.makeErrorText(RegisteredActivity.this,
                                    jObject.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeiShootToast.makeErrorText(RegisteredActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void requestChkTelephone(final String phone) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("Telephone", phone);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.ChkTelephone, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(RegisteredActivity.this,
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
                                if ("200".equals(jObject.optString("resultCode"))) {

                                    JSONObject object = jsonObject.getJSONObject("data");
                                    if ("1".equals(object.optString("data"))) {
                                        WeiShootToast.makeErrorText(RegisteredActivity.this,
                                                "手机号被占用", WeiShootToast.LENGTH_SHORT).show();
                                    } else if ("0".equals(object.optString("data"))) {

                                        SMSSDK.getVerificationCode("86", phone);
                                        dialog.show();
                                    }

                                } else {
                                    WeiShootToast.makeErrorText(RegisteredActivity.this,
                                            jObject.optString("resultMsg"),
                                            WeiShootToast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            WeiShootToast.makeErrorText(RegisteredActivity.this,
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
                WeiShootToast.makeErrorText(RegisteredActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
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
                        UserInfo.getInstance(RegisteredActivity.this).clearUserInfo();
                        UserInfo.getInstance(RegisteredActivity.this).setUserLoginStatus(true);
                        UserInfo.getInstance(RegisteredActivity.this)
                                .setUserUCode(uInfo.data.UCode);
                        UserInfo.getInstance(RegisteredActivity.this).setUserHead(
                                uInfo.data.UserHead);
                        UserInfo.getInstance(RegisteredActivity.this).setUserNickName(
                                uInfo.data.NickName);
                        UserInfo.getInstance(RegisteredActivity.this).setUserSex(uInfo.data.Sex);
                        UserInfo.getInstance(RegisteredActivity.this).setUserIntroduction(
                                uInfo.data.Introduction);
                        UserInfo.getInstance(RegisteredActivity.this).setUserTelephone(
                                uInfo.data.Telephone);
                        // todo解密password、
                        requestDecryptPassword(uInfo.data.Passwords);
                        finish();

                    } else {
                        WeiShootToast.makeErrorText(RegisteredActivity.this,
                                uInfo.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(RegisteredActivity.this,
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
                        WeiShootToast.makeErrorText(RegisteredActivity.this,
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
                                    UserInfo.getInstance(RegisteredActivity.this).setUserPassword(
                                            jsonObject.getJSONObject("data").optString("Password"));

                                } else {
                                    WeiShootToast.makeErrorText(
                                            RegisteredActivity.this,
                                            jsonObject.getJSONObject("result").optString(
                                                    "ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                            .show();
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(RegisteredActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

}
