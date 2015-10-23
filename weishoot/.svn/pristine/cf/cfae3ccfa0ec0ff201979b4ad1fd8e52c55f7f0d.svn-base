
package com.NationalPhotograpy.weishoot.activity.shouye;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 举报页面
 */
public class ReportActivity extends BaseActivity implements OnClickListener {
    private TextView tv_title, tv_report_name, tv_report_signature;

    private Button btn_report;

    private ImageButton ibtn_report_1, ibtn_report_2, ibtn_report_3, ibtn_report_4, ibtn_report_5,
            ibtn_report_6, ibtn_report_7;

    private static final String REPORT_1 = "垃圾营销";

    private static final String REPORT_2 = "淫秽色情";

    private static final String REPORT_3 = "不实信息";

    private static final String REPORT_4 = "敏感信息";

    private static final String REPORT_5 = "抄袭内容";

    private static final String REPORT_6 = "骚扰我";

    private static final String REPORT_7 = "虚假中奖";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_report);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_report_name = (TextView) findViewById(R.id.tv_report_name);
        tv_report_signature = (TextView) findViewById(R.id.tv_report_signature);
        btn_report = (Button) findViewById(R.id.btn_report);
        ibtn_report_1 = (ImageButton) findViewById(R.id.ibtn_report_1);
        ibtn_report_2 = (ImageButton) findViewById(R.id.ibtn_report_2);
        ibtn_report_3 = (ImageButton) findViewById(R.id.ibtn_report_3);
        ibtn_report_4 = (ImageButton) findViewById(R.id.ibtn_report_4);
        ibtn_report_5 = (ImageButton) findViewById(R.id.ibtn_report_5);
        ibtn_report_6 = (ImageButton) findViewById(R.id.ibtn_report_6);
        ibtn_report_7 = (ImageButton) findViewById(R.id.ibtn_report_7);
    }

    private void initData() {
        String nickName = getIntent().getStringExtra("NickName");
        String title = getIntent().getStringExtra("Title");
        if (!TextUtils.isEmpty(nickName)) {
            tv_report_name.setText("@" + nickName);
        }
        if (!TextUtils.isEmpty(title)) {
            tv_report_signature.setText(title);
        }

    }

    private void setListener() {
        tv_title.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        ibtn_report_1.setOnClickListener(this);
        ibtn_report_2.setOnClickListener(this);
        ibtn_report_3.setOnClickListener(this);
        ibtn_report_4.setOnClickListener(this);
        ibtn_report_5.setOnClickListener(this);
        ibtn_report_6.setOnClickListener(this);
        ibtn_report_7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title:
                finish();
                break;
            case R.id.btn_report:
                // ToDo发送举报
                String content = "";
                switch (checkSelect()) {
                    case 0:
                        WeiShootToast.makeErrorText(this, "请选择举报内容", WeiShootToast.LENGTH_SHORT)
                                .show();
                        return;
                    case 1:
                        content = REPORT_1;
                        break;
                    case 2:
                        content = REPORT_2;
                        break;
                    case 3:
                        content = REPORT_3;
                        break;
                    case 4:
                        content = REPORT_4;
                        break;
                    case 5:
                        content = REPORT_5;
                        break;
                    case 6:
                        content = REPORT_6;
                        break;
                    case 7:
                        content = REPORT_7;
                        break;

                    default:
                        break;
                }

                requestReport(content);
                break;

            case R.id.ibtn_report_1:
                if (ibtn_report_1.isSelected()) {
                    return;
                }
                setSelectIbtn(ibtn_report_1);

                break;
            case R.id.ibtn_report_2:
                if (ibtn_report_2.isSelected()) {
                    return;
                }
                setSelectIbtn(ibtn_report_2);
                break;
            case R.id.ibtn_report_3:
                if (ibtn_report_3.isSelected()) {
                    return;
                }
                setSelectIbtn(ibtn_report_3);
                break;
            case R.id.ibtn_report_4:
                if (ibtn_report_4.isSelected()) {
                    return;
                }
                setSelectIbtn(ibtn_report_4);
                break;
            case R.id.ibtn_report_5:
                if (ibtn_report_5.isSelected()) {
                    return;
                }
                setSelectIbtn(ibtn_report_5);
                break;
            case R.id.ibtn_report_6:
                if (ibtn_report_6.isSelected()) {
                    return;
                }
                setSelectIbtn(ibtn_report_6);
                break;
            case R.id.ibtn_report_7:
                if (ibtn_report_7.isSelected()) {
                    return;
                }
                setSelectIbtn(ibtn_report_7);
                break;

            default:
                break;
        }
    }

    private void setSelectIbtn(ImageButton ibtn_report) {
        ibtn_report_1.setSelected(false);
        ibtn_report_2.setSelected(false);
        ibtn_report_3.setSelected(false);
        ibtn_report_4.setSelected(false);
        ibtn_report_5.setSelected(false);
        ibtn_report_6.setSelected(false);
        ibtn_report_7.setSelected(false);
        ibtn_report.setSelected(true);
    }

    private int checkSelect() {
        if (ibtn_report_1.isSelected()) {
            return 1;
        } else if (ibtn_report_2.isSelected()) {
            return 2;
        } else if (ibtn_report_3.isSelected()) {
            return 3;
        } else if (ibtn_report_4.isSelected()) {
            return 4;
        } else if (ibtn_report_5.isSelected()) {
            return 5;
        } else if (ibtn_report_6.isSelected()) {
            return 6;
        } else if (ibtn_report_7.isSelected()) {
            return 7;
        } else {
            return 0;
        }
    }

    private void requestReport(String content) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("errorType", "内容举报");
        params.addBodyParameter("errorConetnt", content);
        params.addBodyParameter("contact", UserInfo.getInstance(this).getUserTelephone());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AddErrorBack, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        WeiShootToast.makeErrorText(ReportActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        Log.v("TAG", "======" + temp);
                        if (!TextUtils.isEmpty(temp)) {

                            try {
                                JSONObject object = new JSONObject(temp);
                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {
                                    WeiShootToast.makeText(ReportActivity.this, "举报成功",
                                            WeiShootToast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    WeiShootToast.makeErrorText(ReportActivity.this,
                                            obj.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(ReportActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

}
