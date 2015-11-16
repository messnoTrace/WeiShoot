
package com.NationalPhotograpy.weishoot.activity.shouye;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 编辑个性签名 给点意见界面
 */
public class UserAdviceActivity extends BaseActivity implements OnClickListener {
    private TextView tv_left, tv_title, tv_right;

    private EditText et_content;

    private String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_user_advice);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        et_content = (EditText) findViewById(R.id.et_content);

    }

    private void initData() {
        flag = getIntent().getStringExtra("flag");
        if ("advice".equals(flag)) {
            tv_left.setText("设置");
            tv_title.setText("给点建议");
            tv_right.setText("提交");
            et_content.setHint("编辑个人建议...");
        }

        if ("gxqm".equals(flag)) {
            tv_left.setText("我的资料");
            tv_title.setText("编辑个人签名");
            tv_right.setText("保存");
            et_content.setHint("编辑个人签名...");
        }
    }

    private void setListener() {
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                String content = et_content.getText().toString();

                if ("gxqm".equals(flag)) {
                    if (TextUtils.isEmpty(content)) {
                        WeiShootToast.makeErrorText(this, "请编辑个人签名", WeiShootToast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    Intent it = new Intent();
                    it.putExtra("content", content);
                    setResult(Constant.RESULT_QM, it);
                    finish();
                } else if ("advice".equals(flag)) {// todo发表建议
                    if (TextUtils.isEmpty(content)) {
                        WeiShootToast.makeErrorText(this, "请编辑个人建议", WeiShootToast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    requestAdvice(content);
                }
                break;

            default:
                break;
        }
    }

    private void requestAdvice(String content) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("errorType", "建议想法");
        params.addBodyParameter("errorConetnt", content);
        params.addBodyParameter("contact", UserInfo.getInstance(this).getUserTelephone());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AddErrorBack, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        WeiShootToast.makeErrorText(UserAdviceActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {

                            try {
                                JSONObject object = new JSONObject(temp);
                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {
                                    WeiShootToast.makeText(UserAdviceActivity.this, "建议提交成功",
                                            WeiShootToast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    WeiShootToast.makeErrorText(UserAdviceActivity.this,
                                            obj.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(UserAdviceActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

}
