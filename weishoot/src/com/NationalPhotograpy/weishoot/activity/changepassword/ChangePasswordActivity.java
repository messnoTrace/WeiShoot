
package com.NationalPhotograpy.weishoot.activity.changepassword;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
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
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity implements OnClickListener {
    private Button btn_left, btn_ok;

    private EditText et_current_password, et_set_new_password, et_set_new_password_again;

    private ProgressiveDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_change_password);
        initView();
        setListener();
    }

    private void initView() {
        dialog = new ProgressiveDialog(this);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        et_current_password = (EditText) findViewById(R.id.et_current_password);
        et_set_new_password = (EditText) findViewById(R.id.et_set_new_password);
        et_set_new_password_again = (EditText) findViewById(R.id.et_set_new_password_again);
    }

    private void setListener() {
        btn_left.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_ok:
                String oldPassword = et_current_password.getText().toString();
                String newPassword_1 = et_set_new_password.getText().toString();
                String newPassword_2 = et_set_new_password_again.getText().toString();
                if (TextUtils.isEmpty(oldPassword)) {
                    WeiShootToast.makeErrorText(this, "密码不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPassword_1)) {
                    WeiShootToast.makeErrorText(this, "新密码不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPassword_2)) {
                    WeiShootToast.makeErrorText(this, "新密码不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword_1.equals(newPassword_2)) {
                    WeiShootToast.makeErrorText(this, "新密码和确认密码不一致", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }

                changePassword(oldPassword, newPassword_1);
                break;

            default:
                break;
        }
    }

    private void changePassword(String oldPassword, final String newPassword_1) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("OldPasswords", oldPassword);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("updateField", "Passwords");
        params.addBodyParameter("Passwords", newPassword_1);
        Log.v("TAG", "sdddd" + UserInfo.getInstance(this).getUserUCode() + "  " + oldPassword);

        httpUtils.send(HttpMethod.POST, HttpUrl.UpdateUser, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                dialog.dismiss();
                WeiShootToast.makeErrorText(ChangePasswordActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                Log.v("TAG", "sdddd" + temp);
                dialog.dismiss();
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {

                            WeiShootToast.makeText(ChangePasswordActivity.this, "修改密码成功",
                                    WeiShootToast.LENGTH_SHORT).show();

                            UserInfo.getInstance(ChangePasswordActivity.this).setUserPassword(
                                    newPassword_1);
                            finish();

                        } else {

                            WeiShootToast.makeErrorText(ChangePasswordActivity.this,
                                    jObject.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeiShootToast.makeErrorText(ChangePasswordActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
