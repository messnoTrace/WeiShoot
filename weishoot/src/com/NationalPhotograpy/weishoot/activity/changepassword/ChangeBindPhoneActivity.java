
package com.NationalPhotograpy.weishoot.activity.changepassword;

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

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 修改绑定手机
 */
public class ChangeBindPhoneActivity extends BaseActivity implements OnClickListener, Callback {

    private Button btn_left, btn_get_code;

    private TextView tv_user_bind_phone_text, tv_user_bind_phone_num, tv_code_countdown;

    private EditText et_current_password, et_new_phone_num;

    private LinearLayout layout_current_password;

    private String phone, newPhone;

    private Handler handler = new Handler(this);

    private ProgressiveDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_change_bind_phone);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        dialog = new ProgressiveDialog(this);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_get_code = (Button) findViewById(R.id.btn_get_code);
        tv_user_bind_phone_text = (TextView) findViewById(R.id.tv_user_bind_phone_text);
        tv_user_bind_phone_num = (TextView) findViewById(R.id.tv_user_bind_phone_num);
        tv_code_countdown = (TextView) findViewById(R.id.tv_code_countdown);
        et_current_password = (EditText) findViewById(R.id.et_current_password);
        et_new_phone_num = (EditText) findViewById(R.id.et_new_phone_num);
        layout_current_password = (LinearLayout) findViewById(R.id.layout_current_password);

    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(phone)) {
            tv_user_bind_phone_num.setText("********" + phone.substring(8, 11));
        }
    }

    private void setListener() {
        btn_left.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_get_code:
                String password = et_current_password.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    WeiShootToast.makeErrorText(this, "请输入密码", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }

                String btnText = btn_get_code.getText().toString().trim();
                    newPhone = et_new_phone_num.getText().toString();
                    if (TextUtils.isEmpty(newPhone)) {
                        WeiShootToast.makeErrorText(this, "请输入新手机号", WeiShootToast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    requestLogin(phone, password);
                    String code = et_new_phone_num.getText().toString();
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initUi() {
        btn_get_code.setText("确定");
        layout_current_password.setVisibility(View.GONE);
        tv_code_countdown.setVisibility(View.VISIBLE);
        tv_user_bind_phone_text.setText("更改绑定手机为：");
        tv_user_bind_phone_num.setText("********" + newPhone.substring(8, 11));
        et_new_phone_num.setText("");
        et_new_phone_num.setHint("请输入短信验证码");
        handler.sendEmptyMessage(0);

    }

    int totalTime = 60;


    private void requestLogin(String user, String password) {
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
                WeiShootToast.makeErrorText(ChangeBindPhoneActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                dialog.dismiss();

            }
        });
    }

    private void requestChangePhone() {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("updateField", "Telephone");
        params.addBodyParameter("Telephone", newPhone);

        httpUtils.send(HttpMethod.POST, HttpUrl.UpdateUser, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                dialog.dismiss();
                WeiShootToast.makeErrorText(ChangeBindPhoneActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                dialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {

                            WeiShootToast.makeText(ChangeBindPhoneActivity.this, "绑定手机修改成功",
                                    WeiShootToast.LENGTH_SHORT).show();

                            UserInfo.getInstance(ChangeBindPhoneActivity.this).setUserTelephone(
                                    newPhone);
                            Intent it = new Intent();
                            it.putExtra("phone", newPhone);
                            setResult(Constant.RESULT_PHONE, it);
                            finish();

                        } else {

                            WeiShootToast.makeErrorText(ChangeBindPhoneActivity.this,
                                    jObject.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeiShootToast.makeErrorText(ChangeBindPhoneActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }

            }
        });
    }

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
