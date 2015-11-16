
package com.NationalPhotograpy.weishoot.activity.changepassword;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.NationalPhotograpy.weishoot.utils.ClickUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 找回密码页面
 */
public class FindBackPasswordActivity extends BaseActivity implements OnClickListener, Callback {
    private Button btn_left, btn_next, btn_get_code, btn_ok;

    private LinearLayout view_container;

    private EditText et_user, et_set_new_password, et_set_new_password_again, et_message_code;

    private LayoutInflater inflater;

    private View input_user_view, finding_now_view, new_password_view, no_find_password_view;

    private TextView tv_user_account, tv_password, tv_user_bind_phone_num, tv_code_countdown,
            tv_lianxi;

    private LinearLayout layout_code;

    public static final String TAG = "FindBackPasswordActivity";

    private String uCode = "";

    private String phone = "";

    private Handler handler = new Handler(this);

    private ProgressiveDialog dialog;

    private String phoneNum = "01068391604";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_find_back_password);

        initView();
        initData();
        setListener();
    }

    private void initView() {
        dialog = new ProgressiveDialog(this);
        inflater = LayoutInflater.from(this);
        btn_left = (Button) findViewById(R.id.btn_left);
        view_container = (LinearLayout) findViewById(R.id.view_container);

        input_user_view = inflater.inflate(R.layout.view_input_user, null);
        btn_next = (Button) input_user_view.findViewById(R.id.btn_next);
        et_user = (EditText) input_user_view.findViewById(R.id.et_user);

        finding_now_view = inflater.inflate(R.layout.view_finding_now, null);
        tv_user_account = (TextView) finding_now_view.findViewById(R.id.tv_user_account);
        tv_password = (TextView) finding_now_view.findViewById(R.id.tv_password);
        tv_code_countdown = (TextView) finding_now_view.findViewById(R.id.tv_code_countdown);
        tv_user_bind_phone_num = (TextView) finding_now_view
                .findViewById(R.id.tv_user_bind_phone_num);
        btn_get_code = (Button) finding_now_view.findViewById(R.id.btn_get_code);
        layout_code = (LinearLayout) finding_now_view.findViewById(R.id.layout_code);
        et_message_code = (EditText) finding_now_view.findViewById(R.id.et_message_code);

        new_password_view = inflater.inflate(R.layout.view_new_password, null);
        btn_ok = (Button) new_password_view.findViewById(R.id.btn_ok);
        et_set_new_password = (EditText) new_password_view.findViewById(R.id.et_set_new_password);
        et_set_new_password_again = (EditText) new_password_view
                .findViewById(R.id.et_set_new_password_again);

        no_find_password_view = inflater.inflate(R.layout.view_no_find_password, null);
        tv_lianxi = (TextView) no_find_password_view.findViewById(R.id.tv_lianxi);

        view_container.addView(input_user_view);

    }

    private void initData() {
     

        String text = "上一步输入的用户名，未绑定手机或邮箱请您拨打电话010-68391604，联系工作人员找回密码";
        SpannableString spStr = new SpannableString(text);
        // 2.NoLineClickSpan 写好了制定位置的颜色和click事件
        NoLineClickSpan clickSpan = new NoLineClickSpan(R.color.deepblue_word);
        // 3.span帮顶下click span
        spStr.setSpan(clickSpan, 24, 36, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        // 4.需要设置下str
        tv_lianxi.setText(spStr);

        // 5.设置TextView可以点击
        tv_lianxi.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void setListener() {
        btn_left.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_next:
                // 获取输入框值下一步
                String userName = et_user.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    WeiShootToast.makeErrorText(this, "登录名不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }

                getUserInfo(userName);

                break;
            case R.id.btn_get_code:

                break;

            case R.id.btn_ok:

                String password = et_set_new_password.getText().toString();
                String passwordagain = et_set_new_password_again.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    WeiShootToast.makeErrorText(this, "密码不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordagain)) {
                    WeiShootToast.makeErrorText(this, "密码不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordagain)) {
                    WeiShootToast.makeErrorText(this, "新密码和确认密码不一致", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }

                changePassword(password);

                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    int totalTime = 60;

    @Override
    public boolean handleMessage(Message msg) {
     

        return false;
    }

    public class NoLineClickSpan extends ClickableSpan {
        int showcolor;

        public NoLineClickSpan(int cl) {
            super();
            showcolor = cl;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(showcolor);
            ds.setUnderlineText(false); // 去掉下划线
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
            FindBackPasswordActivity.this.startActivity(intent);
            avoidHintColor(widget);
        }
    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView)
            ((TextView) view).setHighlightColor(getResources()
                    .getColor(android.R.color.transparent));
    }

    private void changePassword(String password) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("passwords", password);

        httpUtils.send(HttpMethod.POST, HttpUrl.ForgotPassword, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(FindBackPasswordActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
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

                                    WeiShootToast.makeText(FindBackPasswordActivity.this, "密码找回成功",
                                            WeiShootToast.LENGTH_SHORT).show();
                                    finish();

                                } else {

                                    WeiShootToast.makeErrorText(FindBackPasswordActivity.this,
                                            jObject.optString("ResultMsg"),
                                            WeiShootToast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            WeiShootToast.makeErrorText(FindBackPasswordActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
    }

    private void getUserInfo(String userName) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("loginName", userName);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.GetUserByLoginName, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        dialog.dismiss();
                        WeiShootToast.makeErrorText(FindBackPasswordActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        Log.v(TAG, TAG + temp);
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject jsonObject = new JSONObject(temp);
                                JSONObject jObject = jsonObject.getJSONObject("result");
                                if ("200".equals(jObject.optString("ResultCode"))) {

                                    JSONObject object = jsonObject.getJSONObject("data");

                                    phone = object.optString("Telephone");
                                    if (TextUtils.isEmpty(phone)) {
                                        view_container.removeAllViews();
                                        view_container.addView(no_find_password_view);
                                    } else {
                                        view_container.removeAllViews();
                                        view_container.addView(finding_now_view);
                                        uCode = object.optString("UCode");

                                        tv_user_bind_phone_num.setText("********"
                                                + phone.substring(8, 11));
                                    }

                                } else {
                                    view_container.removeAllViews();
                                    view_container.addView(no_find_password_view);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            WeiShootToast.makeErrorText(FindBackPasswordActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
    }
}
